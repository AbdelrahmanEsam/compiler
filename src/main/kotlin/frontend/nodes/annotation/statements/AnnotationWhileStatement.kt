package com.moj.najiz.frontend.nodes.annotation.statements

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import frontend.nodes.annotation.statements.AnnotationStatement

class AnnotationWhileStatement(
    private val _condition: AnnotationExpression,
    private val _thenStatements: Array<AnnotationStatement>,
    private val _headStatement : Array<AnnotationExpression> = emptyArray<AnnotationExpression>(),
) : AnnotationStatement() {

    val condition : AnnotationExpression
        get() = _condition
    val headStatements : Array<AnnotationExpression>
        get() = _headStatement

    val thenStatements : Array<AnnotationStatement>
        get() = _thenStatements





    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationStatementKind.WhileStatement

    override fun evaluate(evaluator: Evaluator): Any?  = evaluator.evaluate(this)

}