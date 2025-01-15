package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.binding.AnnotationBinaryExpression
import com.moj.najiz.frontend.nodes.binding.AnnotationExpression
import com.moj.najiz.frontend.annotator.Annotator

class AssignmentExpressionSyntax(
    private val identifierToken: SyntaxToken,
    private val operatorToken: SyntaxToken,
    private val expressionSyntax: ExpressionSyntax,
) : ExpressionSyntax() {

    override fun annotate(annotator: Annotator): AnnotationExpression {
        return annotator.annotate(this)
    }

    override val kind: SyntaxKind
        get() = SyntaxKind.Expression.AssignmentExpressionSyntax

    override val children: List<SyntaxNode> = listOf(identifierToken,expressionSyntax)

    val identifier: SyntaxToken
        get() = identifierToken

    val operator: SyntaxToken
        get() = operatorToken

    val expression: ExpressionSyntax
        get() = expressionSyntax


}
