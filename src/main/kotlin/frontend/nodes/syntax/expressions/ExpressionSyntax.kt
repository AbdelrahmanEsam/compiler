package com.moj.najiz.frontend.nodes.syntax.expressions

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken

abstract class ExpressionSyntax() : SyntaxNode()
{
   abstract val token: SyntaxToken
   abstract fun annotate(annotator: Annotator) : AnnotationExpression

}