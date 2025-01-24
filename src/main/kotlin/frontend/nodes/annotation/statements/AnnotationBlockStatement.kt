package com.moj.najiz.frontend.nodes.annotation.statements

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind
import frontend.nodes.annotation.statements.AnnotationStatement

class AnnotationBlockStatement(private val _statements : Array<AnnotationStatement>) : AnnotationStatement() {
     val statements: Array<AnnotationStatement>
        get() = _statements

    override fun evaluate(evaluator: Evaluator): Any?  = evaluator.evaluate(this)

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationStatementKind.BlockStatement
}