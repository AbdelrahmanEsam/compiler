package com.moj.najiz.frontend.nodes.annotation

import com.moj.najiz.frontend.expressionEvaluator.Evaluator

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

    override fun evaluate(evaluator: Evaluator): Any {
        return evaluator.evaluate(this)
    }
}