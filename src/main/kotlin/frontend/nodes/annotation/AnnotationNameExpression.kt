package com.moj.najiz.frontend.nodes.annotation

import com.moj.najiz.frontend.expressionEvaluator.Evaluator

class AnnotationNameExpression(
    private val name: String,
    private val type : Any,
) : AnnotationExpression() {

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.AnnotationNameExpression

    val variableType: Any
        get() = type


    override val value: Any
        get() = name

    override fun evaluate(evaluator: Evaluator): Any {
        return evaluator.evaluate(name)
    }
}