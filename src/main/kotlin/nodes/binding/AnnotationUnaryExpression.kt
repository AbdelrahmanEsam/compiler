package com.moj.najiz.nodes.binding

class AnnotationUnaryExpression(
    private val _operatorKind: AnnotationUnaryOperator,
    private val _annotationExpression: AnnotationExpression,
) : AnnotationExpression() {

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.UnaryExpression

    val operand: AnnotationUnaryOperator
        get() = _operatorKind

    val annotationExpression: AnnotationExpression
        get() = _annotationExpression


    override val value: Any
        get() = _operatorKind
}