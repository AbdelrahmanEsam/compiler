package com.moj.najiz.frontend.nodes.annotation.expressions

import com.moj.najiz.frontend.VariableSymbol
import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind

class AnnotationAssignmentExpression(
    private val _variable: VariableSymbol,
    private val _expression : AnnotationExpression,
) : AnnotationExpression() {

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.AnnotationAssignmentExpression

    val expression: AnnotationExpression
        get() = _expression



    override val value: Any
        get() = _variable

     val variable: VariableSymbol
        get() = _variable

    override fun evaluate(evaluator: Evaluator): Any? {
        return evaluator.evaluate(this)
    }
}