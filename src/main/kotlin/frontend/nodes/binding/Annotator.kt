package com.moj.najiz.frontend.nodes.binding

import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind.*
import com.moj.najiz.frontend.nodes.syntax.*
import kotlin.reflect.typeOf

class Annotator {

    private val _diagnostics: Diagnostic.DiagnosticsBag = Diagnostic.DiagnosticsBag()
    val diagnostics: Diagnostic.DiagnosticsBag
        get() = _diagnostics

    fun annotate(expression: BinaryExpressionSyntax): AnnotationExpression {
        val leftAnnotatedExpression = expression.leftExpression.annotate(this)
        val rightAnnotatedExpression = expression.rightExpression.annotate(this)
        val annotatedBinaryKind = this.annotateBinaryExpressionKind(
            expression.operator.kind,
            leftAnnotatedExpression.value,
            rightAnnotatedExpression.value
        )

        annotatedBinaryKind?.let {

            return AnnotationBinaryExpression(
                _left = leftAnnotatedExpression,
                _right = rightAnnotatedExpression,
                _operatorKind = annotatedBinaryKind,
            )

        }

        _diagnostics.reportUndefinedBinaryOperator(
            expression.operator.textSpan,
            expression.operator.text,
            leftAnnotatedExpression.value::class,
            rightAnnotatedExpression.value::class
        )


        return leftAnnotatedExpression
    }


    fun annotate(expression: LiteralExpressionSyntax): AnnotationExpression {
        expression.apply {
            return AnnotationLiteralExpression(if (value != null) value!! else 0)
        }
    }


    fun annotate(expression: UnaryExpressionSyntax): AnnotationExpression {
        expression.apply {
            val annotatedOperand = this.operandExpression.annotate(this@Annotator)
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
    }


    fun annotate(expressions: ParenthesizedExpressionSyntax): AnnotationExpression {
        expressions.apply {


            val annotatedExpression = this.coreExpression.annotate(this@Annotator)
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