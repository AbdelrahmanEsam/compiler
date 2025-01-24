package com.moj.najiz.frontend.nodes.syntax.statements

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken
import frontend.nodes.annotation.statements.AnnotationStatement

class BlockStatementSyntax(
    openBraceToken: SyntaxToken,
    private val _statements: Array<StatementSyntax>,
    closeBraceToken: SyntaxToken,
) : StatementSyntax() {


     val statements:  Array<StatementSyntax>
        get() = _statements


    override val kind: SyntaxKind
        get() = SyntaxKind.Statement.BlockStatementStatement

    override fun annotate(annotator: Annotator): AnnotationStatement = annotator.annotate(this)


}