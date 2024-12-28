package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.binding.AnnotationExpression
import com.moj.najiz.frontend.nodes.binding.Annotator

class NameExpressionSyntax(
    private val identifierToken: SyntaxToken,
) : ExpressionSyntax() {
    override fun annotate(annotator: Annotator): AnnotationExpression {
        return annotator.annotate(this)
    }

    override val kind: SyntaxKind
        get() = SyntaxKind.Expression.NameExpressionSyntax

    override val children: List<SyntaxNode> = listOf(identifierToken)

    val identifier: SyntaxToken
        get() = identifierToken
}
