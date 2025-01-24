package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import com.moj.najiz.frontend.nodes.syntax.expressions.ExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.StatementSyntax
import frontend.nodes.annotation.statements.AnnotationStatement

class CompilationUnitSyntax(private val _root : StatementSyntax, private val _token : SyntaxToken) : SyntaxNode()
{
    override val kind: SyntaxKind
        get() = SyntaxKind.CompilationUnit

    val root : StatementSyntax
        get() = _root

    val token : SyntaxToken
        get() = _token

    override val children: List<SyntaxNode>
        get() = listOf(_root)

    fun annotate(annotator: Annotator) : AnnotationStatement = this.root.annotate(annotator)

}