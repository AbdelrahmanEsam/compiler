package com.moj.najiz.frontend.nodes.syntax.expressions

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken

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
    override val token: SyntaxToken
        get() = expression.token

    override fun annotate(annotator: Annotator): AnnotationExpression {
        return annotator.annotate(this)
    }

    override val kind: SyntaxKind = SyntaxKind.Expression.ParenthesizedExpressionSyntax
}