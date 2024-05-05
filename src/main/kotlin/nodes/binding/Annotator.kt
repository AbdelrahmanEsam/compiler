package com.moj.najiz.nodes.binding

import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.lexicalAnalyzer.SyntaxKind.*
import com.moj.najiz.nodes.syntax.*

class Annotator {

    private val diagnostics: MutableList<String> = mutableListOf()


    fun annotate(expression: ExpressionSyntax): AnnotationExpression {
        return when (expression.kind) {
            Expression.BinaryExpressionSyntax -> (expression as BinaryExpressionSyntax).annotateBinaryExpression()
            Expression.LiteralExpressionSyntax -> (expression as LiteralExpressionSyntax).annotateLiteralExpression()
            Expression.UnaryExpressionSyntax -> (expression as UnaryExpressionSyntax).annotateUnaryExpression()
            else -> throw Exception("unexpected token ${expression.kind}")
        }
    }

    private fun BinaryExpressionSyntax.annotateBinaryExpression(): AnnotationExpression {
        val leftAnnotatedExpression = annotate(this.leftExpression)
        val rightAnnotatedExpression = annotate(this.rightExpression)
        val annotatedUnaryKind = annotateBinaryExpressionKind(
            this.operator.kind,
            leftAnnotatedExpression.value,
            rightAnnotatedExpression.value
        )

        annotatedUnaryKind?.let {

            return AnnotationBinaryExpression(
                _left = leftAnnotatedExpression,
                _right = rightAnnotatedExpression,
                _operatorKind = annotatedUnaryKind,
            )

        }

        diagnostics.add(
            "binary operator ${operator.actualValue}" +
                    " isn't defined as ${operator.text} and ${rightAnnotatedExpression.value::class}"
        )
        return leftAnnotatedExpression
    }

    private fun LiteralExpressionSyntax.annotateLiteralExpression(): AnnotationExpression {
        return AnnotationLiteralExpression(if (value != null) value!! else 0)
    }


    private fun UnaryExpressionSyntax.annotateUnaryExpression(): AnnotationExpression {


        val annotatedOperand = annotate(this.operandExpression)
        val annotatedUnaryKind = annotateUnaryOperatorKind(this.operandExpression.kind, annotatedOperand.annotationKind)


        annotatedUnaryKind?.let {
            return AnnotationUnaryExpression(
                _operatorKind = annotatedUnaryKind,
                _annotationExpression = annotatedOperand
            )
        }


        diagnostics.add(
            "binary operator ${operator.actualValue}" +
                    " isn't defined as ${this.operator.text} and ${annotatedOperand.value::class}"
        )
        return annotatedOperand
    }

    //todo fix issue here
    private fun annotateUnaryOperatorKind(
        operandKind: SyntaxKind,
        annotationKind: Any,
    ): AnnotationUnaryOperatorKind? {


        if (annotationKind is Int) {
            return when (operandKind) {
                Token.MathOperatorsToken.PlusToken -> AnnotationUnaryOperatorKind.Identity
                Token.MathOperatorsToken.MinusToken -> AnnotationUnaryOperatorKind.Negation
                else -> throw Exception("unexpected token $operandKind")

            }
        }

        if (annotationKind is Boolean) {
            return when (operandKind) {
                Token.BooleanOperatorToken.NotToken -> AnnotationUnaryOperatorKind.LogicalNegation
                else -> throw Exception("unexpected token $operandKind")
            }
        }
        return null
    }

    private fun annotateBinaryExpressionKind(
        operandKind: SyntaxKind,
        leftValue: Any,
        rightValue: Any
    ): AnnotationBinaryOperatorKind? {

        if (leftValue is Int || rightValue is Int) {
            return when (operandKind) {
                Token.MathOperatorsToken.PlusToken -> AnnotationBinaryOperatorKind.Addition
                Token.MathOperatorsToken.MinusToken -> AnnotationBinaryOperatorKind.Subtraction
                Token.MathOperatorsToken.MultiplyToken -> AnnotationBinaryOperatorKind.Multiplication
                Token.MathOperatorsToken.SlashToken -> AnnotationBinaryOperatorKind.Division
                else -> throw Exception("unexpected token $operandKind")
            }
        }

        if (leftValue is Boolean || rightValue is Boolean) {
            return when (operandKind) {
                Token.BooleanOperatorToken.AndToken -> AnnotationBinaryOperatorKind.LogicalAnd
                Token.BooleanOperatorToken.OrToken -> AnnotationBinaryOperatorKind.LogicalOr
                else -> throw Exception("unexpected token $operandKind")
            }
        }

        return null
    }

}