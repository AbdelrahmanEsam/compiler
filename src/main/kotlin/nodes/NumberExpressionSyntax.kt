package com.moj.najiz.nodes

import com.moj.najiz.lexicalAnalyzer.SyntaxKind

class NumberExpressionSyntax(private val number: SyntaxToken) : ExpressionSyntax() {

    val numberToken: SyntaxToken
        get() {
            return number
        }

    override val kind: SyntaxKind
        get() = SyntaxKind.Number


    override val children: List<SyntaxNode> = listOf(number)

}