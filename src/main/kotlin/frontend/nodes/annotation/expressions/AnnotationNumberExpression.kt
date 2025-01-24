package com.moj.najiz.frontend.nodes.annotation.expressions

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind

class AnnotationNumberExpression(private val _value: Any) : AnnotationExpression() {
    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.NumberExpression


   override val value: Any
        get() = _value

    override fun evaluate(evaluator: Evaluator): Any? {
        return evaluator.evaluate(this)
    }


}