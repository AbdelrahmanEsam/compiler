package com.moj.najiz.frontend.nodes.annotation.statements

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import frontend.nodes.annotation.statements.AnnotationStatement

class AnnotationIfStatement(
    private val _ifCondition: AnnotationExpression,
    private val _thenStatement: AnnotationStatement,
    private val _elseStatement: AnnotationStatement?
) : AnnotationStatement() {

    val ifCondition : AnnotationExpression
        get() = _ifCondition


    val thenStatement : AnnotationStatement
        get() = _thenStatement


    val elseStatement : AnnotationStatement?
        get() = _elseStatement


    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationStatementKind.IfStatement

    override fun evaluate(evaluator: Evaluator): Any?  = evaluator.evaluate(this)

}