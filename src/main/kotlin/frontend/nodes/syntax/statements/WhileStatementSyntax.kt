package com.moj.najiz.frontend.nodes.syntax.statements

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.syntax.expressions.ExpressionSyntax
import frontend.nodes.annotation.statements.AnnotationStatement

class WhileStatementSyntax(private val _condition: ExpressionSyntax, private val _thenStatement : StatementSyntax) : StatementSyntax() {

    val condition : ExpressionSyntax
        get() = _condition

    val thenStatement : StatementSyntax
        get() = _thenStatement


    override fun annotate(annotator: Annotator): AnnotationStatement  = annotator.annotate(this)

    override val kind: SyntaxKind
        get() = SyntaxKind.Statement.WhileStatement
}

