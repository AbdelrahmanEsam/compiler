package com.moj.najiz.nodes.binding

import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.nodes.syntax.*

class Annotator(expression: ExpressionSyntax) {

    private fun annotate(expression: ExpressionSyntax): AnnotationExpression {
        return when (expression.kind) {
            SyntaxKind.Expression.BinaryExpressionSyntax -> (expression as BinaryExpressionSyntax).annotateBinaryExpression()
            SyntaxKind.Expression.LiteralExpressionSyntax -> (expression as LiteralExpressionSyntax).annotateLiteralExpression()
            SyntaxKind.Expression.UnaryExpressionSyntax -> (expression as UnaryExpressionSyntax).annotateUnaryExpression()
            else -> throw Exception("unexpected token ${expression.kind}")
        }
    }

    private fun BinaryExpressionSyntax.annotateBinaryExpression(): AnnotationExpression {
        val leftAnnotatedExpression = annotate(this.leftExpression)
        val rightAnnotatedExpression = annotate(this.rightExpression)
        val annotatedUnaryKind = annotateBinaryExpressionKind(this.operator.kind)
        return AnnotationBinaryExpression(
            _left = leftAnnotatedExpression,
            _right = rightAnnotatedExpression,
            _operatorKind = annotatedUnaryKind,
        )

    }

    private fun LiteralExpressionSyntax.annotateLiteralExpression(): AnnotationExpression {
        return AnnotationLiteralExpression(if (literalToken.actualValue != null) literalToken.actualValue!! else 0)
    }


    private fun UnaryExpressionSyntax.annotateUnaryExpression(): AnnotationExpression {
        val annotatedOperand = annotate(this.operandExpression)
        val annotatedUnaryKind = annotateUnaryOperatorKind(this.operandExpression.kind)
        return AnnotationUnaryExpression(_operatorKind = annotatedUnaryKind, _annotationExpression = annotatedOperand)
    }

    private fun annotateUnaryOperatorKind(operandKind: SyntaxKind): AnnotationUnaryOperatorKind {

        return when (operandKind) {
            SyntaxKind.Token.PlusToken -> AnnotationUnaryOperatorKind.Identity
            SyntaxKind.Token.MinusToken -> AnnotationUnaryOperatorKind.Negation
            else -> throw Exception("unexpected token $operandKind")

        }
    }

    private fun annotateBinaryExpressionKind(operandKind: SyntaxKind): AnnotationBinaryOperatorKind {

        return when (operandKind) {
            SyntaxKind.Token.PlusToken -> AnnotationBinaryOperatorKind.Addition
            SyntaxKind.Token.MinusToken -> AnnotationBinaryOperatorKind.Subtraction
            SyntaxKind.Token.MultiplyToken -> AnnotationBinaryOperatorKind.Multiplication
            SyntaxKind.Token.SlashToken -> AnnotationBinaryOperatorKind.Division
            else -> throw Exception("unexpected token $operandKind")
        }
    }

    init {
        annotate(expression)
    }
}