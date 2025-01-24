package com.moj.najiz.frontend.nodes.syntax.expressions

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode

abstract class ExpressionSyntax : SyntaxNode()
{

   abstract fun annotate(annotator: Annotator) : AnnotationExpression

}