package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.nodes.annotation.AnnotationExpression
import com.moj.najiz.frontend.annotator.Annotator

abstract class ExpressionSyntax : SyntaxNode()
{

   abstract fun annotate(annotator: Annotator) : AnnotationExpression

}