package com.moj.najiz.frontend.nodes.annotation.expressions

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind

class AnnotationAssignmentExpression(
    private val _name: String,
    private val _expression : AnnotationExpression,
) : AnnotationExpression() {

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.AnnotationAssignmentExpression

    val expression: AnnotationExpression
        get() = _expression



    override val value: Any
        get() = _name

    override fun evaluate(evaluator: Evaluator): Any? {
        return evaluator.evaluate(this,_name)
    }
}