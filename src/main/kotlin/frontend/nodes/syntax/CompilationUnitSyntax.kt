package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.AnnotationExpression


class CompilationUnitSyntax(private val _root : ExpressionSyntax,private val _token : SyntaxToken) : SyntaxNode()
{
    override val kind: SyntaxKind
        get() = SyntaxKind.CompilationUnit

    val root : ExpressionSyntax
        get() = _root

    val token : SyntaxToken
        get() = _token

    override val children: List<SyntaxNode>
        get() = listOf(_root)

    fun annotate(annotator: Annotator) : AnnotationExpression = this.root.annotate(annotator)

}