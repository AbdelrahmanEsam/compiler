package com.moj.najiz.frontend.nodes.syntax.statements

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode
import frontend.nodes.annotation.statements.AnnotationStatement

abstract class StatementSyntax : SyntaxNode()
{

   abstract fun annotate(annotator: Annotator) : AnnotationStatement

}