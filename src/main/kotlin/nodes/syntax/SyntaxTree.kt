package com.moj.najiz.nodes.syntax

data class SyntaxTree(
    val diagnostics: List<String>,
    val root: ExpressionSyntax,
    val endOfFile: SyntaxToken,
)