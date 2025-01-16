package com.moj.najiz.frontend.nodes.annotation

import com.moj.najiz.frontend.expressionEvaluator.Evaluator

class AnnotationNumberExpression(private val _value: Any) : AnnotationExpression() {
    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.NumberExpression


   override val value: Any
        get() = _value

    override fun evaluate(evaluator: Evaluator): Any {
        return evaluator.evaluate(this)
    }


}