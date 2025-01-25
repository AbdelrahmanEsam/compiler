package com.moj.najiz.frontend.nodes.syntax.statements

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken
import com.moj.najiz.frontend.nodes.syntax.expressions.ExpressionSyntax
import frontend.nodes.annotation.statements.AnnotationStatement

class VariableDeclarationStatementSyntax(
    private val _modifier: SyntaxToken,
    private val _identifier: SyntaxToken,
    private val _equalToken: SyntaxToken,
    private val _expression : ExpressionSyntax
) : StatementSyntax() {

     val modifier: SyntaxToken
        get() = _modifier

    val identifier: SyntaxToken
        get() = _identifier

    val equalToken: SyntaxToken
        get() = _equalToken

    val expression: ExpressionSyntax
        get() = _expression

    override fun annotate(annotator: Annotator): AnnotationStatement = annotator.annotate(this)

    override val kind: SyntaxKind
        get() = SyntaxKind.Statement.VariableDeclarationStatementStatement
}