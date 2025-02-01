package com.moj.najiz.frontend.nodes.syntax.expressions

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.*
import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken

class AssignmentExpressionSyntax(
    private val identifierToken: SyntaxToken,
    private val operatorToken: SyntaxToken,
    private val expressionSyntax: ExpressionSyntax,
) : ExpressionSyntax() {

    override fun annotate(annotator: Annotator): AnnotationExpression = annotator.annotate(this)

    override val kind: SyntaxKind
        get() = SyntaxKind.Expression.AssignmentExpressionSyntax

    override val children: List<SyntaxNode> = listOf(identifierToken, expressionSyntax)

    override val token: SyntaxToken
        get() = identifierToken

    val operator: SyntaxToken
        get() = operatorToken

    val expression: ExpressionSyntax
        get() = expressionSyntax


}
