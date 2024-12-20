package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind

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

    override val kind: SyntaxKind = SyntaxKind.Expression.ParenthesizedExpressionSyntax
}