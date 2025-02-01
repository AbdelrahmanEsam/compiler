package com.moj.najiz.frontend.nodes.syntax.statements

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken
import com.moj.najiz.frontend.nodes.syntax.expressions.ExpressionSyntax
import frontend.nodes.annotation.statements.AnnotationStatement

class IfStatementSyntax(private val _condition: ExpressionSyntax, private val _thenStatement : StatementSyntax ,private val _elseClause : ElseClauseSyntax?) : StatementSyntax() {

    val condition : ExpressionSyntax
        get() = _condition

    val thenStatement : StatementSyntax
        get() = _thenStatement

    val elseClause : ElseClauseSyntax?
        get() = _elseClause


    override fun annotate(annotator: Annotator): AnnotationStatement  = annotator.annotate(this)

    override val kind: SyntaxKind
        get() = SyntaxKind.Statement.IfStatement
}

class ElseClauseSyntax(private val _elseToken : SyntaxToken ,private val  _elseStatement : StatementSyntax) : SyntaxNode()
{
    override val kind: SyntaxKind
        get() = SyntaxKind.Token.StatementKeywords.ElseToken

    val elseToken : SyntaxToken
        get() = _elseToken

    val elseStatement : StatementSyntax
        get() = _elseStatement
}