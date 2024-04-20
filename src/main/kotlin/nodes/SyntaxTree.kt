package com.moj.najiz.nodes

data class SyntaxTree(
    val diagnostics: List<String>,
    val root: ExpressionSyntax,
    val endOfFile: SyntaxToken,
)