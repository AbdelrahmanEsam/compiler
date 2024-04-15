package com.moj.najiz.parser.nodes

import com.moj.najiz.lexicalAnalyzer.SyntaxKind

class BinaryExpressionSyntax(
    private val left: SyntaxNode,
    private val operatorToken: SyntaxToken,
    private val right: SyntaxNode,
) : ExpressionSyntax() {

    override val kind: SyntaxKind
        get() = SyntaxKind.BinaryExpressionSyntax

    override val children: List<SyntaxNode> = listOf(left, operatorToken, right)

    val leftExpression: SyntaxNode
        get() = left

    val rightExpression: SyntaxNode
        get() = right

    val operator: SyntaxToken
        get() = operatorToken
}
