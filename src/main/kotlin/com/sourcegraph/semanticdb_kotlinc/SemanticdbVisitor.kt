package com.sourcegraph.semanticdb_kotlinc

import com.sourcegraph.semanticdb_kotlinc.Semanticdb.SymbolOccurrence.Role
import java.nio.file.Path
import kotlin.contracts.ExperimentalContracts
import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.KtSourceFile
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.references.FirResolvedNamedReference
import org.jetbrains.kotlin.fir.symbols.FirBasedSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirCallableSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.name.FqName

@ExperimentalContracts
class SemanticdbVisitor(
    sourceroot: Path,
    file: KtSourceFile,
    lineMap: LineMap,
    globals: GlobalSymbolsCache,
    locals: LocalSymbolsCache = LocalSymbolsCache()
) {
    private val cache = SymbolsCache(globals, locals)
    private val documentBuilder = SemanticdbTextDocumentBuilder(sourceroot, file, lineMap, cache)

    private data class SymbolDescriptorPair(
        val firBasedSymbol: FirBasedSymbol<*>?,
        val symbol: Symbol
    )

    fun build(): Semanticdb.TextDocument {
        return documentBuilder.build()
    }

    context(context: CheckerContext)
    private fun Sequence<SymbolDescriptorPair>?.emitAll(
        element: KtSourceElement,
        role: Role,
        enclosingSource: KtSourceElement? = null,
    ): List<Symbol>? =
        this?.onEach { (firBasedSymbol, symbol) ->
            documentBuilder.emitSemanticdbData(firBasedSymbol, symbol, element, role, enclosingSource)
        }
            ?.map { it.symbol }
            ?.toList()

    private fun Sequence<Symbol>.with(firBasedSymbol: FirBasedSymbol<*>?) =
        this.map { SymbolDescriptorPair(firBasedSymbol, it) }

    context(context: CheckerContext)
    fun visitPackage(pkg: FqName, element: KtSourceElement) {
        cache[pkg].with(null).emitAll(element, Role.REFERENCE)
    }

    context(context: CheckerContext)
    fun visitClassReference(firClassSymbol: FirClassLikeSymbol<*>, element: KtSourceElement) {
        cache[firClassSymbol].with(firClassSymbol).emitAll(element, Role.REFERENCE)
    }

    context(context: CheckerContext)
    fun visitCallableReference(firClassSymbol: FirCallableSymbol<*>, element: KtSourceElement) {
        cache[firClassSymbol].with(firClassSymbol).emitAll(element, Role.REFERENCE)
    }

    context(context: CheckerContext)
    fun visitClassOrObject(firClass: FirClassLikeDeclaration, element: KtSourceElement, enclosingSource: KtSourceElement? = null) {
        cache[firClass.symbol].with(firClass.symbol).emitAll(element, Role.DEFINITION, enclosingSource)
    }

    context(context: CheckerContext)
    fun visitPrimaryConstructor(firConstructor: FirConstructor, source: KtSourceElement, enclosingSource: KtSourceElement? = null) {
        // if the constructor is not denoted by the 'constructor' keyword, we want to link it to the
        // class ident
        cache[firConstructor.symbol].with(firConstructor.symbol).emitAll(source, Role.DEFINITION, enclosingSource)
    }

    context(context: CheckerContext)
    fun visitSecondaryConstructor(firConstructor: FirConstructor, source: KtSourceElement, enclosingSource: KtSourceElement? = null) {
        cache[firConstructor.symbol].with(firConstructor.symbol).emitAll(source, Role.DEFINITION, enclosingSource)
    }

    context(context: CheckerContext)
    fun visitNamedFunction(firFunction: FirFunction, source: KtSourceElement, enclosingSource: KtSourceElement? = null) {
        cache[firFunction.symbol].with(firFunction.symbol).emitAll(source, Role.DEFINITION, enclosingSource)
    }

    context(context: CheckerContext)
    fun visitProperty(firProperty: FirProperty, source: KtSourceElement, enclosingSource: KtSourceElement? = null) {
        cache[firProperty.symbol].with(firProperty.symbol).emitAll(source, Role.DEFINITION, enclosingSource)
    }

    context(context: CheckerContext)
    fun visitParameter(firParameter: FirValueParameter, source: KtSourceElement, enclosingSource: KtSourceElement? = null) {
        cache[firParameter.symbol].with(firParameter.symbol).emitAll(source, Role.DEFINITION, enclosingSource)
    }

    context(context: CheckerContext)
    fun visitTypeParameter(firTypeParameter: FirTypeParameter, source: KtSourceElement, enclosingSource: KtSourceElement? = null) {
        cache[firTypeParameter.symbol]
            .with(firTypeParameter.symbol)
            .emitAll(source, Role.DEFINITION, enclosingSource)
    }

    context(context: CheckerContext)
    fun visitTypeAlias(firTypeAlias: FirTypeAlias, source: KtSourceElement, enclosingSource: KtSourceElement? = null) {
        cache[firTypeAlias.symbol].with(firTypeAlias.symbol).emitAll(source, Role.DEFINITION, enclosingSource)
    }

    context(context: CheckerContext)
    fun visitPropertyAccessor(firPropertyAccessor: FirPropertyAccessor, source: KtSourceElement, enclosingSource: KtSourceElement? = null) {
        cache[firPropertyAccessor.symbol]
            .with(firPropertyAccessor.symbol)
            .emitAll(source, Role.DEFINITION, enclosingSource)
    }

    context(context: CheckerContext)
    fun visitEnumEntry(firEnumEntry: FirEnumEntry, source: KtSourceElement, enclosingSource: KtSourceElement? = null) {
        cache[firEnumEntry.symbol].with(firEnumEntry.symbol).emitAll(source, Role.DEFINITION, enclosingSource)
    }

    context(context: CheckerContext)
    fun visitSimpleNameExpression(
        firResolvedNamedReference: FirResolvedNamedReference,
        source: KtSourceElement,
    ) {
        cache[firResolvedNamedReference.resolvedSymbol]
            .with(firResolvedNamedReference.resolvedSymbol)
            .emitAll(source, Role.REFERENCE)
    }
}

