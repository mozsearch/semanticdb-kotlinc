package com.sourcegraph.semanticdb_kotlinc

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.KtDiagnosticsContainer
import org.jetbrains.kotlin.diagnostics.KtSourcelessDiagnosticFactory
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.rendering.BaseSourcelessDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.strongWarningWithoutSource

object SemanticDbKotlincDiagnostics : KtDiagnosticsContainer() {
    val EXCEPTION_WARNING: KtSourcelessDiagnosticFactory by strongWarningWithoutSource()

    override fun getRendererFactory(): BaseDiagnosticRendererFactory = Messages

    object Messages : BaseSourcelessDiagnosticRendererFactory() {
        override val MAP: KtDiagnosticFactoryToRendererMap by KtDiagnosticFactoryToRendererMap("semanticdb-kotlinc") { map ->
            map.put(EXCEPTION_WARNING, MESSAGE_PLACEHOLDER)
        }
    }
}
