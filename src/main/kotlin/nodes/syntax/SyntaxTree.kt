package com.moj.najiz.nodes.syntax

import com.moj.najiz.diagnostics.Diagnostic

data class SyntaxTree(
    val diagnosticsBag: Diagnostic.DiagnosticsBag,
    val root: ExpressionSyntax,
    val endOfFile: SyntaxToken,
)