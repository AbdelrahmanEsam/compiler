package com.moj.najiz.nodes.syntax

import com.moj.najiz.lexicalAnalyzer.SyntaxKind

class LiteralExpressionSyntax(private val number: SyntaxToken) : ExpressionSyntax() {

    val literalToken: SyntaxToken
        get() {
            return number
        }

    override val kind: SyntaxKind
        get() = SyntaxKind.Expression.LiteralExpressionSyntax


    override val children: List<SyntaxNode> = listOf(number)

}