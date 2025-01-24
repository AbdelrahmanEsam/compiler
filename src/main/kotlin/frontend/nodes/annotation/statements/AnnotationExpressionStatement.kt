package com.moj.najiz.frontend.nodes.annotation.statements

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import frontend.nodes.annotation.statements.AnnotationStatement

class AnnotationExpressionStatement(private val _expression : AnnotationExpression) : AnnotationStatement() {
     val expression : AnnotationExpression
        get() = _expression

    override fun evaluate(evaluator: Evaluator): Any? = evaluator.evaluate(this)

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationStatementKind.ExpressionStatement
}