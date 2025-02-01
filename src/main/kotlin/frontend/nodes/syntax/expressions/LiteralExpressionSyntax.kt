package com.moj.najiz.frontend.nodes.syntax.expressions

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken

class LiteralExpressionSyntax(private val _token: SyntaxToken, private val _value: Any?) : ExpressionSyntax() {

    constructor(number: SyntaxToken) : this(number, number.actualValue)

    override val token: SyntaxToken
        get() = _token


    override fun annotate(annotator: Annotator): AnnotationExpression {
        return annotator.annotate(this)
    }

    override val kind: SyntaxKind
        get() = SyntaxKind.Expression.LiteralExpressionSyntax

    val value: Any?
        get() = _value


    override val children: List<SyntaxNode> = listOf(_token)

}