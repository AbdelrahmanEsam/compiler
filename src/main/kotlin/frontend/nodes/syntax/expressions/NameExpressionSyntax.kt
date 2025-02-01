package com.moj.najiz.frontend.nodes.syntax.expressions

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken

class NameExpressionSyntax(
    private val identifierToken: SyntaxToken,
) : ExpressionSyntax() {
    override val token: SyntaxToken
        get() = identifierToken

    override fun annotate(annotator: Annotator): AnnotationExpression  = annotator.annotate(this)

    override val kind: SyntaxKind
        get() = SyntaxKind.Expression.NameExpressionSyntax

    override val children: List<SyntaxNode> = listOf(identifierToken)

    val identifier: SyntaxToken
        get() = identifierToken
}
