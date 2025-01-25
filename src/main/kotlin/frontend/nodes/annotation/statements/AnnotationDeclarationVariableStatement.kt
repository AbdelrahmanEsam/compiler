package com.moj.najiz.frontend.nodes.annotation.statements

import com.moj.najiz.frontend.VariableSymbol
import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotationKind
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken
import frontend.nodes.annotation.statements.AnnotationStatement

class AnnotationDeclarationVariableStatement(private val _variable : VariableSymbol  , private val _expression : AnnotationExpression) : AnnotationStatement() {
     val expression : AnnotationExpression
        get() = _expression

    val variable : VariableSymbol
        get() = _variable

    override fun evaluate(evaluator: Evaluator): Any? = evaluator.evaluate(this)

    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationStatementKind.VariableDeclarationStatement
}