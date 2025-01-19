package com.moj.najiz.frontend.nodes.annotation

import com.moj.najiz.frontend.expressionEvaluator.Evaluator

class AnnotationParenthesizedExpression(
    private val openToken: AnnotationParenthesizedOperatorKind,
    private val _coreExpression: AnnotationExpression,
    private val closeToken: AnnotationParenthesizedOperatorKind
) : AnnotationExpression() {
    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.AnnotationParenthesizedExpression

    override val value: Any
        get() = _coreExpression.value

    override fun evaluate(evaluator: Evaluator): Any? {
        return evaluator.evaluate(this)
    }

    val coreExpression: AnnotationExpression
        get() {
            return _coreExpression
        }
}