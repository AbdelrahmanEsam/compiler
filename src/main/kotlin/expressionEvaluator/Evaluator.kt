package com.moj.najiz.expressionEvaluator

import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.nodes.syntax.*

class Evaluator(private val root: ExpressionSyntax) {

    fun getEvaluator(): Int {
        return evaluateExpression(root)
    }


    private fun evaluateExpression(expression: ExpressionSyntax): Int {
        if (expression is LiteralExpressionSyntax) {
            return expression.literalToken.actualValue as Int
        }

        if (expression is BinaryExpressionSyntax) {
            val leftEvaluation = evaluateExpression(expression.leftExpression)
            val rightEvaluation = evaluateExpression(expression.rightExpression)

            return when (expression.operator.kind) {
                SyntaxKind.Token.PlusToken -> {
                    leftEvaluation + rightEvaluation
                }

                SyntaxKind.Token.MinusToken -> {
                    leftEvaluation - rightEvaluation
                }

                SyntaxKind.Token.SlashToken -> {
                    leftEvaluation / rightEvaluation
                }

                SyntaxKind.Token.MultiplyToken -> {
                    leftEvaluation * rightEvaluation
                }


                else -> throw Exception("unExpected binary operator ${expression.kind}")
            }


        }

        if (expression is UnaryExpressionSyntax) {
            val operand = evaluateExpression(expression.operandExpression)
            return when (expression.operator.kind) {
                SyntaxKind.Token.PlusToken -> {
                    operand
                }

                SyntaxKind.Token.MinusToken -> {
                    -operand
                }

                else -> throw Exception("unExpected unary  operator ${expression.kind}")
            }
        }

        if (expression is ParenthesizedExpressionSyntax) {
            return evaluateExpression(expression.coreExpression)
        }
        throw Exception("unExpected node operator kind ${expression.kind}")
    }
}