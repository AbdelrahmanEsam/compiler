package com.moj.najiz.frontend.annotator

import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.*
import com.moj.najiz.frontend.nodes.syntax.AssignmentExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.BinaryExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.LiteralExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.NameExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.NumberExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.ParenthesizedExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.UnaryExpressionSyntax
import kotlin.collections.get
import kotlin.reflect.typeOf

class Annotator(private val variables : Map<String,Any>) {

    private val _diagnostics: Diagnostic.DiagnosticsBag = Diagnostic.DiagnosticsBag()
    val diagnostics: Diagnostic.DiagnosticsBag
        get() = _diagnostics


    fun annotate(expression: NameExpressionSyntax): AnnotationExpression {
        expression.apply {
            if(!variables.containsKey(expression.identifier.text))
            {
                diagnostics.reportUndefinedName(expression.identifier.textSpan,expression.identifier.text)
            }
           val value = variables[expression.identifier.text]
            val operandKind = value!!::class.java
            return AnnotationNameExpression(
                name = expression.identifier.text!!,
                type = operandKind
            )
        }
    }

    fun annotate(expression: AssignmentExpressionSyntax): AnnotationExpression {
        expression.apply {
            val name = this.identifier.text
            variables
            return AnnotationAssignmentExpression(name ?: "", expression.expression.annotate(this@Annotator))
        }
    }

    fun annotate(expression: BinaryExpressionSyntax): AnnotationExpression {
        val leftAnnotatedExpression = expression.leftExpression.annotate(this)
        val rightAnnotatedExpression = expression.rightExpression.annotate(this)
        val actualLeftValue =  if(leftAnnotatedExpression is AnnotationNameExpression){
             variables[leftAnnotatedExpression.value.toString()]
        } else {
            leftAnnotatedExpression.value
        }

        val actualRightValue =  if(rightAnnotatedExpression is AnnotationNameExpression){
            variables[rightAnnotatedExpression.value.toString()]
        } else {
            rightAnnotatedExpression.value
        }
        val annotatedBinaryKind = this.annotateBinaryExpressionKind(
            expression.operator.kind,
            actualLeftValue!!,
            actualRightValue!!
        )

        annotatedBinaryKind?.let {

            return AnnotationBinaryExpression(
                _left = leftAnnotatedExpression,
                _right = rightAnnotatedExpression,
                _operatorKind = annotatedBinaryKind,
            )

        }

        _diagnostics.reportUndefinedBinaryOperator(
            textSpan = expression.operator.textSpan,
            operator = expression.operator.text,
            rightExpressionType = leftAnnotatedExpression.value::class,
            leftExpressionType = rightAnnotatedExpression.value::class
        )


        return leftAnnotatedExpression
    }


    fun annotate(expression: LiteralExpressionSyntax): AnnotationExpression {
        expression.apply {
            return AnnotationLiteralExpression(if (value != null) value!! else 0)
        }
    }

    fun annotate(expression: NumberExpressionSyntax): AnnotationExpression {
        expression.apply {
            return AnnotationNumberExpression(if (value != null) value!! else 0)
        }
    }




    fun annotate(expression: UnaryExpressionSyntax): AnnotationExpression {
        expression.apply {
            val annotatedOperand = this.operandExpression.annotate(this@Annotator)
            val annotatedUnaryKind = annotateUnaryOperatorKind(this.operator.kind)


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
        }
    }


    private fun annotateUnaryOperatorKind(
        operandKind: SyntaxKind,
    ): AnnotationUnaryOperator? {

            return when (operandKind) {
                SyntaxKind.Token.MathOperatorsToken.PlusToken -> AnnotationUnaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.MathOperatorsToken.PlusToken,
                    operandKind = typeOf<Int>()
                )

                SyntaxKind.Token.MathOperatorsToken.MinusToken -> AnnotationUnaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.MathOperatorsToken.MinusToken,
                    operandKind = typeOf<Int>()
                )

                SyntaxKind.Token.BooleanOperatorToken.NotToken -> AnnotationUnaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.BooleanOperatorToken.NotToken,
                    operandKind = typeOf<Boolean>()
                )

                else -> throw Exception("unexpected token $operandKind")

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
                SyntaxKind.Token.MathOperatorsToken.PlusToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.MathOperatorsToken.PlusToken,
                    leftType = Int,
                    rightType = Int
                )

                SyntaxKind.Token.MathOperatorsToken.MinusToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.MathOperatorsToken.MinusToken,
                    leftType = Int,
                    rightType = Int
                )

                SyntaxKind.Token.MathOperatorsToken.MultiplyToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.MathOperatorsToken.MultiplyToken,
                    leftType = Int,
                    rightType = Int
                )

                SyntaxKind.Token.MathOperatorsToken.SlashToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.MathOperatorsToken.SlashToken,
                    leftType = Int,
                    rightType = Int
                )

                SyntaxKind.Token.EqualityToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.EqualityToken,
                    leftType = Int,
                    rightType = Int,
                )

                SyntaxKind.Token.NotEqualityToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.NotEqualityToken,
                    leftType = Int,
                    rightType = Int,
                )

                else -> throw Exception("unexpected token $operandKind")
            }
        }

        if (leftValue is Boolean || rightValue is Boolean) {
            return when (operandKind) {
                SyntaxKind.Token.BooleanOperatorToken.AndToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.BooleanOperatorToken.AndToken,
                    leftType = Boolean,
                    rightType = Boolean
                )

                SyntaxKind.Token.BooleanOperatorToken.OrToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.BooleanOperatorToken.OrToken,
                    leftType = Boolean,
                    rightType = Boolean
                )

                SyntaxKind.Token.EqualityToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.EqualityToken,
                    leftType = Boolean,
                    rightType = Boolean
                )

                SyntaxKind.Token.NotEqualityToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.NotEqualityToken,
                    leftType = Boolean,
                    rightType = Boolean
                )

                else -> throw Exception("unexpected token $operandKind")
            }
        }

        return null
    }


}