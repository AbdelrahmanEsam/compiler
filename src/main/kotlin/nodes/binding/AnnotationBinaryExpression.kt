package com.moj.najiz.nodes.binding

class AnnotationBinaryExpression(
    private val _operatorKind: AnnotationBinaryOperatorKind,
    private val _left: AnnotationExpression,
    private val _right: AnnotationExpression,
) : AnnotationExpression() {

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.BinaryExpression

    val operandKind: AnnotationBinaryOperatorKind
        get() = _operatorKind

    val rightExpression: AnnotationExpression
        get() = _right

    val leftExpression: AnnotationExpression
        get() = _left
}