package com.moj.najiz.frontend.nodes.annotation.expressions

import com.moj.najiz.frontend.VariableSymbol
import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind

class AnnotationNameExpression(
    private val variable: VariableSymbol,
    private val type : Any,
) : AnnotationExpression() {

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.AnnotationNameExpression


    override val value: VariableSymbol
        get() = variable

    override fun evaluate(evaluator: Evaluator): Any? {
        return evaluator.evaluate(variable)
    }
}