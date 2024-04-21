package com.moj.najiz.nodes

import com.moj.najiz.lexicalAnalyzer.SyntaxKind

class LiteralExpressionSyntax(private val number: SyntaxToken) : ExpressionSyntax() {

    val numberToken: SyntaxToken
        get() {
            return number
        }

    override val kind: SyntaxKind
        get() = SyntaxKind.Expression.LiteralExpressionSyntax


    override val children: List<SyntaxNode> = listOf(number)

}