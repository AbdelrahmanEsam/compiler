package com.moj.najiz.frontend.nodes.binding

import com.moj.najiz.frontend.expressionEvaluator.Evaluator

class AnnotationLiteralExpression(private val _value: Any) : AnnotationExpression() {
    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.LiteralExpression


   override val value: Any
        get() = _value

    override fun evaluate(evaluator: Evaluator): Any {
        return evaluator.evaluate(this)
    }


}