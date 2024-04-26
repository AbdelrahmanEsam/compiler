package com.moj.najiz.nodes.binding

class AnnotationUnaryExpression(
    private val _operatorKind: AnnotationUnaryOperatorKind,
    private val _annotationExpression: AnnotationExpression,
) : AnnotationExpression() {

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.UnaryExpression

    val operandKind: AnnotationUnaryOperatorKind
        get() = _operatorKind

    val annotationExpression: AnnotationExpression
        get() = _annotationExpression
}