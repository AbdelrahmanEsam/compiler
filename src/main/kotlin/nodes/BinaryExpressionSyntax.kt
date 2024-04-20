package com.moj.najiz.nodes

import com.moj.najiz.lexicalAnalyzer.SyntaxKind

class BinaryExpressionSyntax(
    private val left: ExpressionSyntax,
    private val operatorToken: SyntaxToken,
    private val right: ExpressionSyntax,
) : ExpressionSyntax() {

    override val kind: SyntaxKind
        get() = SyntaxKind.BinaryExpressionSyntax

    override val children: List<SyntaxNode> = listOf(left, operatorToken, right)

    val leftExpression: ExpressionSyntax
        get() = left

    val rightExpression: ExpressionSyntax
        get() = right

    val operator: SyntaxToken
        get() = operatorToken
}
