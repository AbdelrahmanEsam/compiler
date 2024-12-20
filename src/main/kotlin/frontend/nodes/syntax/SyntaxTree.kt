package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.diagnostics.Diagnostic

data class SyntaxTree(
    val diagnosticsBag: Diagnostic.DiagnosticsBag,
    val root: ExpressionSyntax,
    val endOfFile: SyntaxToken,
)