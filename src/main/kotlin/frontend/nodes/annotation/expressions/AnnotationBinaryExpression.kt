package com.moj.najiz.frontend.nodes.annotation.expressions

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationBinaryOperator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind

class AnnotationBinaryExpression(
    private val _operatorKind: AnnotationBinaryOperator,
    private val _left: AnnotationExpression,
    private val _right: AnnotationExpression,
    private val _value : Any?
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
        get() = _value ?: _left.value

    override fun evaluate(evaluator: Evaluator): Any? = evaluator.evaluate(this)


}