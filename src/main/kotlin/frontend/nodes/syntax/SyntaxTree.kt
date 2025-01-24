package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.di.DIContainer
import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.nodes.syntax.CompilationUnitSyntax

class SyntaxTree {

    var diagnosticsBag: Diagnostic.DiagnosticsBag
        private set
    var root: CompilationUnitSyntax
        private set

    private constructor(
        text: String
    ) {

        val parser = DIContainer.getParser(DIContainer.getLexer().apply { setCodeInput(text) })
        root = parser.parseCompilationUnit()
        diagnosticsBag = parser.diagnosticsBag
    }

    companion object {
        fun parse(text: String): SyntaxTree {
            return SyntaxTree(text)
        }
    }


}