package com.sourcegraph.semanticdb_kotlinc

import com.sourcegraph.semanticdb_kotlinc.Semanticdb.SymbolInformation.Kind
import com.sourcegraph.semanticdb_kotlinc.Semanticdb.SymbolOccurrence.Role
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import kotlin.contracts.ExperimentalContracts
import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.KtSourceFile
import org.jetbrains.kotlin.com.intellij.lang.java.JavaLanguage
import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.FirPackageDirective
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.directOverriddenSymbolsSafe
import org.jetbrains.kotlin.fir.analysis.checkers.toClassLikeSymbol
import org.jetbrains.kotlin.fir.analysis.getChild
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.utils.isInterface
import org.jetbrains.kotlin.fir.renderer.*
import org.jetbrains.kotlin.fir.symbols.FirBasedSymbol
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.types.impl.FirImplicitAnyTypeRef
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi
import org.jetbrains.kotlin.text

@ExperimentalContracts
class SemanticdbTextDocumentBuilder(
    private val sourceroot: Path,
    private val file: KtSourceFile,
    private val lineMap: LineMap,
    private val cache: SymbolsCache,
) {
    private val occurrences = mutableListOf<Semanticdb.SymbolOccurrence>()
    private val symbols = mutableListOf<Semanticdb.SymbolInformation>()
    private val fileText = file.getContentsAsStream().reader().readText()
    private val semanticMd5 = semanticdbMD5()

    fun build() = TextDocument {
        this.text = fileText
        this.uri = semanticdbURI()
        this.md5 = semanticMd5
        this.schema = Semanticdb.Schema.SEMANTICDB4
        this.language = Semanticdb.Language.KOTLIN
        occurrences.sortWith(compareBy({ it.range.startLine }, { it.range.startCharacter }))
        this.addAllOccurrences(occurrences)
        this.addAllSymbols(symbols)
    }

    context(context: CheckerContext)
    fun emitSemanticdbData(
        firBasedSymbol: FirBasedSymbol<*>?,
        symbol: Symbol,
        element: KtSourceElement,
        role: Role,
    ) {
        symbolOccurrence(symbol, element, role).let {
            if (!occurrences.contains(it)) {
                occurrences.add(it)
            }
        }
        val symbolInformation = symbolInformation(firBasedSymbol, symbol, element)
        if (role == Role.DEFINITION && !symbols.contains(symbolInformation))
            symbols.add(symbolInformation)
    }

    @OptIn(SymbolInternals::class)
    context(context: CheckerContext)
    private fun symbolInformation(
        firBasedSymbol: FirBasedSymbol<*>?,
        symbol: Symbol,
        element: KtSourceElement,
    ): Semanticdb.SymbolInformation {
        val supers =
            when (firBasedSymbol) {
                is FirClassSymbol ->
                    firBasedSymbol
                        .resolvedSuperTypeRefs
                        .filter { it !is FirImplicitAnyTypeRef }
                        .mapNotNull { it.toClassLikeSymbol(firBasedSymbol.moduleData.session) }
                        .flatMap { cache[it] }

                is FirFunctionSymbol<*> ->
                    firBasedSymbol.directOverriddenSymbolsSafe().flatMap { cache[it] }

                else -> emptyList<Symbol>().asIterable()
            }
        return SymbolInformation {
            this.symbol = symbol.toString()
            this.displayName = firBasedSymbol?.let { displayName(it) } ?: element.text.toString()
            this.documentation = semanticdbDocumentation(firBasedSymbol?.fir)
            this.kind = semanticdbKind(firBasedSymbol?.fir)
            this.enclosingSymbol =
                context.containingDeclarations.lastOrNull()?.let { cache[it].last().toString() } ?: ""
            this.addAllOverriddenSymbols(supers.map { it.toString() })
            this.language =
                when (element.psi?.language ?: KotlinLanguage.INSTANCE) {
                    is KotlinLanguage -> Semanticdb.Language.KOTLIN
                    is JavaLanguage -> Semanticdb.Language.JAVA
                    else -> throw IllegalArgumentException("unexpected language")
                }
        }
    }

    private fun symbolOccurrence(
        symbol: Symbol,
        element: KtSourceElement,
        role: Role
    ): Semanticdb.SymbolOccurrence =
        SymbolOccurrence {
            this.symbol = symbol.toString()
            this.role = role
            this.range = semanticdbRange(element)
        }

    private fun semanticdbRange(element: KtSourceElement): Semanticdb.Range =
        Range {
            startCharacter = lineMap.startCharacter(element)
            startLine = lineMap.lineNumber(element) - 1
            endCharacter = lineMap.endCharacter(element)
            endLine = lineMap.lineNumber(element) - 1
        }

    private fun semanticdbURI(): String {
        // TODO: unix-style only
        val relative = sourceroot.relativize(Paths.get(file.path))
        return relative.toString()
    }

    private fun semanticdbMD5(): String =
        MessageDigest.getInstance("MD5")
            .digest(file.getContentsAsStream().readBytes())
            .joinToString("") { "%02X".format(it) }

    @OptIn(SymbolInternals::class, RenderingInternals::class)
    private fun displayName(firBasedSymbol: FirBasedSymbol<*>): String =
        when (firBasedSymbol) {
            is FirClassLikeSymbol -> firBasedSymbol.classId.shortClassName.asString()
            is FirPropertyAccessorSymbol -> firBasedSymbol.fir.propertySymbol.name.asString()
            is FirFunctionSymbol -> firBasedSymbol.callableId.callableName.asString()
            is FirPropertySymbol -> firBasedSymbol.callableIdForRendering.callableName.asString()
            is FirVariableSymbol -> firBasedSymbol.name.asString()
            is FirTypeParameterSymbol -> firBasedSymbol.name.asString()
            else -> firBasedSymbol.toString()
        }

    private fun semanticdbDocumentation(element: FirElement?): Semanticdb.Documentation = Documentation {
        format = Semanticdb.Documentation.Format.MARKDOWN
        message = element?.let {
            // Like FirRenderer().forReadability, but using FirAllModifierRenderer instead of FirPartialModifierRenderer
            val renderer = FirRenderer(
                typeRenderer = ConeTypeRenderer(),
                idRenderer = ConeIdShortRenderer(),
                classMemberRenderer = FirNoClassMemberRenderer(),
                bodyRenderer = null,
                propertyAccessorRenderer = null,
                callArgumentsRenderer = FirCallNoArgumentsRenderer(),
                modifierRenderer = FirAllModifierRenderer(),
                callableSignatureRenderer = FirCallableSignatureRendererForReadability(),
                declarationRenderer = FirDeclarationRenderer("local "),
            )
            val renderOutput = renderer.renderElementAsString(it)
            val kdoc = it.source?.getChild(KtTokens.DOC_COMMENT)?.text?.toString() ?: ""
            "```kotlin\n$renderOutput\n```${stripKDocAsterisks(kdoc)}"
        } ?: ""
    }

    private fun semanticdbKind(element: FirElement?): Kind =
        when (element) {
            is FirClass if element.isInterface -> Kind.INTERFACE
            is FirClassLikeDeclaration -> Kind.CLASS
            is FirConstructor -> Kind.CONSTRUCTOR
            is FirTypeParameter -> Kind.TYPE_PARAMETER
            is FirValueParameter -> Kind.PARAMETER
            is FirField -> Kind.FIELD
            is FirProperty -> Kind.FIELD
            is FirEnumEntry -> Kind.FIELD
            is FirVariable -> Kind.LOCAL
            is FirCallableDeclaration -> Kind.METHOD
            is FirPackageDirective -> Kind.PACKAGE
            else -> Kind.UNKNOWN_KIND
        }

    // Returns the kdoc string with all leading and trailing "/*" tokens removed. Naive
    // implementation that can
    // be replaced with a utility method from the compiler in the future, if one exists.
    private fun stripKDocAsterisks(kdoc: String): String {
        if (kdoc.isEmpty()) return kdoc
        val out = StringBuilder().append("\n\n").append("----").append("\n")
        kdoc.lineSequence().forEach { line ->
            if (line.isEmpty()) return@forEach
            var start = 0
            while (start < line.length && line[start].isWhitespace()) {
                start++
            }
            if (start < line.length && line[start] == '/') {
                start++
            }
            while (start < line.length && line[start] == '*') {
                start++
            }
            var end = line.length - 1
            if (end > start && line[end] == '/') {
                end--
            }
            while (end > start && line[end] == '*') {
                end--
            }
            while (end > start && line[end].isWhitespace()) {
                end--
            }
            start = minOf(start, line.length - 1)
            if (end > start) {
                end++
            }
            out.append("\n").append(line, start, end)
        }
        return out.toString()
    }
}
