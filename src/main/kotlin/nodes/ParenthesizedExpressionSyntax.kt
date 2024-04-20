package com.moj.najiz.nodes

import com.moj.najiz.lexicalAnalyzer.SyntaxKind

class ParenthesizedExpressionSyntax(
    openToken: SyntaxToken,
    private val expression: ExpressionSyntax,
    closeToken: SyntaxToken,
) : ExpressionSyntax() {

    val coreExpression: ExpressionSyntax
        get() {
            return expression
        }

    override val children: List<SyntaxNode> = listOf(openToken, expression, closeToken)

    override val kind: SyntaxKind = SyntaxKind.ParenthesizedExpressionSyntax
}