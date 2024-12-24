package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.nodes.binding.AnnotationExpression
import com.moj.najiz.frontend.nodes.binding.Annotator

abstract class ExpressionSyntax : SyntaxNode()
{

   abstract fun annotate(annotator: Annotator) : AnnotationExpression

}