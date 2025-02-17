package com.moj.najiz.frontend.nodes.annotation.statements

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import frontend.nodes.annotation.statements.AnnotationStatement

class AnnotationForStatement(
    private val _variableDeclaration : AnnotationExpression,
    private val _condition: AnnotationExpression,
    private val _trailingExpression: AnnotationStatement,
    private val _thenStatements: Array<AnnotationStatement>,
) : AnnotationStatement() {


    val variableDeclaration : AnnotationExpression
        get() = _variableDeclaration

    val condition : AnnotationExpression
        get() = _condition


    val trailingExpression : AnnotationStatement
        get() = _trailingExpression

    val thenStatements : Array<AnnotationStatement>
        get() = _thenStatements





    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationStatementKind.ForStatement

    override fun evaluate(evaluator: Evaluator): Any?  = evaluator.evaluate(this)

}