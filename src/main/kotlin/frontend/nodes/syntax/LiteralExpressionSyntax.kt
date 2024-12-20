package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind

class LiteralExpressionSyntax(private val token: SyntaxToken, private val _value: Any?) : ExpressionSyntax() {

    constructor(number: SyntaxToken) : this(number, number.actualValue)

    val literalToken: SyntaxToken
        get() {
            return token
        }

    override val kind: SyntaxKind
        get() = SyntaxKind.Expression.LiteralExpressionSyntax

    val value: Any?
        get() = _value


    override val children: List<SyntaxNode> = listOf(token)

}