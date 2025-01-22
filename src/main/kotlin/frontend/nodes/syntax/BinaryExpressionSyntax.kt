package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.AnnotationExpression
import com.moj.najiz.frontend.annotator.Annotator

class BinaryExpressionSyntax(
    private val left: ExpressionSyntax,
    private val operatorToken: SyntaxToken,
    private val right: ExpressionSyntax,
) : ExpressionSyntax() {
    override fun annotate(annotator: Annotator): AnnotationExpression = annotator.annotate(this)


    override val kind: SyntaxKind
        get() = SyntaxKind.Expression.BinaryExpressionSyntax

    override val children: List<SyntaxNode> = listOf(left, operatorToken, right)

    val leftExpression: ExpressionSyntax
        get() = left

    val rightExpression: ExpressionSyntax
        get() = right

    val operator: SyntaxToken
        get() = operatorToken
}
