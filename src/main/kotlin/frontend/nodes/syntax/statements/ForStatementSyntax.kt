package com.moj.najiz.frontend.nodes.syntax.statements

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.syntax.expressions.ExpressionSyntax
import frontend.nodes.annotation.statements.AnnotationStatement

class ForStatementSyntax(
    private val _variableDeclaration: ExpressionSyntax,
    private val _condition: ExpressionSyntax,
    private val _trailingStatement: StatementSyntax,
    private val _thenStatement: StatementSyntax
) : StatementSyntax() {

    val condition: ExpressionSyntax
        get() = _condition

    val thenStatement: StatementSyntax
        get() = _thenStatement

    val trailingStatement: StatementSyntax
        get() = _trailingStatement

    val variableDeclaration: ExpressionSyntax
        get() = _variableDeclaration


    override fun annotate(annotator: Annotator): AnnotationStatement = annotator.annotate(this)

    override val kind: SyntaxKind
        get() = SyntaxKind.Statement.ForStatement
}

