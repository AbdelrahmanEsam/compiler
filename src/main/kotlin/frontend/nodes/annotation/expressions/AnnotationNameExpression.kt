package com.moj.najiz.frontend.nodes.annotation.expressions

import com.moj.najiz.frontend.VariableSymbol
import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind

class AnnotationNameExpression(
    private val _variable: VariableSymbol,
    private val type : Any,
    private val _value : Any
) : AnnotationExpression() {

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.AnnotationNameExpression

     val variable: VariableSymbol
        get() = _variable

    override val value: Any
        get() = _value

    override fun evaluate(evaluator: Evaluator): Any? {
        return evaluator.evaluate(variable)
    }
}