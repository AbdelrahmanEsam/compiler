package com.moj.najiz.frontend.nodes.syntax.statements

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.syntax.expressions.ExpressionSyntax
import frontend.nodes.annotation.statements.AnnotationStatement

class ExpressionStatementSyntax(private val _expressionSyntax: ExpressionSyntax) : StatementSyntax() {
    override fun annotate(annotator: Annotator): AnnotationStatement = annotator.annotate(this)

     val expression : ExpressionSyntax
        get() = _expressionSyntax

    override val kind: SyntaxKind
        get() = SyntaxKind.Statement.ExpressionStatementStatement
}