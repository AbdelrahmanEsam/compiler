package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.binding.AnnotationExpression
import com.moj.najiz.frontend.nodes.binding.Annotator

class ParenthesizedExpressionSyntax(
    openToken: SyntaxToken,
    private val expression: ExpressionSyntax,
    closeToken: SyntaxToken,
) : ExpressionSyntax() {

    val coreExpression: ExpressionSyntax
        get() {
            return expression
        }

    override val children: List<SyntaxNode> = listOf(openToken, expression, closeToken)
    override fun annotate(annotator: Annotator): AnnotationExpression {
        return annotator.annotate(this)
    }

    override val kind: SyntaxKind = SyntaxKind.Expression.ParenthesizedExpressionSyntax
}