package com.moj.najiz.frontend.nodes.binding

import com.moj.najiz.frontend.expressionEvaluator.Evaluator

class AnnotationBinaryExpression(
    private val _operatorKind: AnnotationBinaryOperator,
    private val _left: AnnotationExpression,
    private val _right: AnnotationExpression,
) : AnnotationExpression() {

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.BinaryExpression

    val operand: AnnotationBinaryOperator
        get() = _operatorKind

    val rightExpression: AnnotationExpression
        get() = _right

    val leftExpression: AnnotationExpression
        get() = _left

    override val value: Any
        get() = _left.value

    override fun evaluate(evaluator: Evaluator): Any {
        return evaluator.evaluate(this)
    }

}