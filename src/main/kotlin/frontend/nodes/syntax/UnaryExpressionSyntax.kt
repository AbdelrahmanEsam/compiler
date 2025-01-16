package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.AnnotationExpression
import com.moj.najiz.frontend.annotator.Annotator

class UnaryExpressionSyntax(
    private val operand: ExpressionSyntax,
    private val operatorToken: SyntaxToken,
) : ExpressionSyntax() {
    override fun annotate(annotator: Annotator): AnnotationExpression {
        return annotator.annotate(this)
    }

    override val kind: SyntaxKind
        get() = SyntaxKind.Expression.UnaryExpressionSyntax

    override val children: List<SyntaxNode> = listOf(operatorToken, operand)

    val operandExpression: ExpressionSyntax
        get() = operand

    val operator: SyntaxToken
        get() = operatorToken
}
