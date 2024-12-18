package com.moj.najiz.nodes.binding

import com.moj.najiz.diagnostics.Diagnostic
import com.moj.najiz.diagnostics.TextSpan
import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.lexicalAnalyzer.SyntaxKind.*
import com.moj.najiz.nodes.syntax.*
import kotlin.reflect.typeOf

class Annotator {

    private val _diagnostics: Diagnostic.DiagnosticsBag = Diagnostic.DiagnosticsBag()
    val diagnostics: Diagnostic.DiagnosticsBag
        get() = _diagnostics


    fun annotate(expression: ExpressionSyntax): AnnotationExpression {
        return when (expression.kind) {
            Expression.BinaryExpressionSyntax -> (expression as BinaryExpressionSyntax).annotateBinaryExpression()
            Expression.LiteralExpressionSyntax -> (expression as LiteralExpressionSyntax).annotateLiteralExpression()
            Expression.UnaryExpressionSyntax -> (expression as UnaryExpressionSyntax).annotateUnaryExpression()
            Expression.ParenthesizedExpressionSyntax -> (expression as ParenthesizedExpressionSyntax).annotateParenthesizedExpression()
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

        _diagnostics.reportUndefinedBinaryOperator(
            operator.textSpan,
            operator.text,
            leftAnnotatedExpression.value::class,
            rightAnnotatedExpression.value::class
        )


        return leftAnnotatedExpression
    }

    private fun LiteralExpressionSyntax.annotateLiteralExpression(): AnnotationExpression {
        return AnnotationLiteralExpression(if (value != null) value!! else 0)
    }


    private fun UnaryExpressionSyntax.annotateUnaryExpression(): AnnotationExpression {


        val annotatedOperand = annotate(this.operandExpression)
        val annotatedUnaryKind = annotateUnaryOperatorKind(this.operator.kind, annotatedOperand.value)


        annotatedUnaryKind?.let {
            return AnnotationUnaryExpression(
                _operatorKind = annotatedUnaryKind,
                _annotationExpression = annotatedOperand
            )
        }


        _diagnostics.reportUndefinedUnaryOperator(operator.textSpan, operator.text, annotatedOperand.value::class)
        return annotatedOperand
    }


    private fun ParenthesizedExpressionSyntax.annotateParenthesizedExpression(): AnnotationExpression {
        val annotatedExpression = annotate(this.coreExpression)
        return AnnotationParenthesizedExpression(
            openToken = AnnotationParenthesizedOperatorKind.OpenBracket,
            _coreExpression = annotatedExpression,
            closeToken = AnnotationParenthesizedOperatorKind.CloseBracket,
        )


//        _diagnostics.reportUndefinedParenthesizedOperator(
//            coreExpression ,
//            annotatedExpression.value, kind, annotatedExpression.value::class
//        )
//
//        _diagnostics.add(
//            "parenthesized operator ${annotatedExpression.value}" +
//                    " isn't defined as ${this.kind} and ${annotatedExpression.value::class}"
//        )
        return annotatedExpression
    }


    private fun annotateUnaryOperatorKind(
        operandKind: SyntaxKind,
        annotationKind: Any,
    ): AnnotationUnaryOperator? {


        if (annotationKind is Int) {
            return when (operandKind) {
                Token.MathOperatorsToken.PlusToken -> AnnotationUnaryOperator.annotate(
                    syntaxKind = Token.MathOperatorsToken.PlusToken,
                    operandKind = typeOf<Int>()
                )

                Token.MathOperatorsToken.MinusToken -> AnnotationUnaryOperator.annotate(
                    syntaxKind = Token.MathOperatorsToken.MinusToken,
                    operandKind = typeOf<Int>()
                )

                else -> throw Exception("unexpected token $operandKind")

            }
        }

        if (annotationKind is Boolean) {
            return when (operandKind) {
                Token.BooleanOperatorToken.NotToken -> AnnotationUnaryOperator.annotate(
                    syntaxKind = Token.BooleanOperatorToken.NotToken,
                    operandKind = typeOf<Boolean>()
                )

                else -> throw Exception("unexpected token $operandKind")
            }
        }
        return null
    }

    private fun annotateBinaryExpressionKind(
        operandKind: SyntaxKind,
        leftValue: Any,
        rightValue: Any
    ): AnnotationBinaryOperator? {


        if (leftValue is Int || rightValue is Int) {
            return when (operandKind) {
                Token.MathOperatorsToken.PlusToken -> AnnotationBinaryOperator.annotate(
                    syntaxKind = Token.MathOperatorsToken.PlusToken,
                    leftType = Int,
                    rightType = Int
                )

                Token.MathOperatorsToken.MinusToken -> AnnotationBinaryOperator.annotate(
                    syntaxKind = Token.MathOperatorsToken.MinusToken,
                    leftType = Int,
                    rightType = Int
                )

                Token.MathOperatorsToken.MultiplyToken -> AnnotationBinaryOperator.annotate(
                    syntaxKind = Token.MathOperatorsToken.MultiplyToken,
                    leftType = Int,
                    rightType = Int
                )

                Token.MathOperatorsToken.SlashToken -> AnnotationBinaryOperator.annotate(
                    syntaxKind = Token.MathOperatorsToken.SlashToken,
                    leftType = Int,
                    rightType = Int
                )

                Token.EqualityToken -> AnnotationBinaryOperator.annotate(
                    syntaxKind = Token.EqualityToken,
                    leftType = Int,
                    rightType = Int,
                )

                Token.NotEqualityToken -> AnnotationBinaryOperator.annotate(
                    syntaxKind = Token.NotEqualityToken,
                    leftType = Int,
                    rightType = Int,
                )

                else -> throw Exception("unexpected token $operandKind")
            }
        }

        if (leftValue is Boolean || rightValue is Boolean) {
            return when (operandKind) {
                Token.BooleanOperatorToken.AndToken -> AnnotationBinaryOperator.annotate(
                    syntaxKind = Token.BooleanOperatorToken.AndToken,
                    leftType = Boolean,
                    rightType = Boolean
                )

                Token.BooleanOperatorToken.OrToken -> AnnotationBinaryOperator.annotate(
                    syntaxKind = Token.BooleanOperatorToken.OrToken,
                    leftType = Boolean,
                    rightType = Boolean
                )

                Token.EqualityToken -> AnnotationBinaryOperator.annotate(
                    syntaxKind = Token.EqualityToken,
                    leftType = Boolean,
                    rightType = Boolean
                )

                Token.NotEqualityToken -> AnnotationBinaryOperator.annotate(
                    syntaxKind = Token.NotEqualityToken,
                    leftType = Boolean,
                    rightType = Boolean
                )

                else -> throw Exception("unexpected token $operandKind")
            }
        }

        return null
    }


}