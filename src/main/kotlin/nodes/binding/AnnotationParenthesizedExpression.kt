package com.moj.najiz.nodes.binding

import com.moj.najiz.nodes.syntax.ExpressionSyntax

class AnnotationParenthesizedExpression(
    private val openToken: AnnotationParenthesizedOperatorKind,
    private val _coreExpression: AnnotationExpression,
    private val closeToken: AnnotationParenthesizedOperatorKind
) : AnnotationExpression() {
    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.AnnotationParenthesizedExpression

    override val value: Any
        get() = _coreExpression.value

    val coreExpression: AnnotationExpression
        get() {
            return _coreExpression
        }
}