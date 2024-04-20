package com.moj.najiz.expressionEvaluator

import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.nodes.BinaryExpressionSyntax
import com.moj.najiz.nodes.ExpressionSyntax
import com.moj.najiz.nodes.NumberExpressionSyntax
import com.moj.najiz.nodes.ParenthesizedExpressionSyntax

class Evaluator(private val root: ExpressionSyntax) {

    fun getEvaluator(): Int {
        return evaluateExpression(root)
    }


    private fun evaluateExpression(expression: ExpressionSyntax): Int {
        if (expression is NumberExpressionSyntax) {
            return expression.numberToken.actualValue as Int
        }

        if (expression is BinaryExpressionSyntax) {
            val leftEvaluation = evaluateExpression(expression.leftExpression)
            val rightEvaluation = evaluateExpression(expression.rightExpression)

            return when (expression.operator.kind) {
                SyntaxKind.PlusToken -> {
                    leftEvaluation + rightEvaluation
                }

                SyntaxKind.MinusToken -> {
                    leftEvaluation - rightEvaluation
                }

                SyntaxKind.SlashToken -> {
                    leftEvaluation / rightEvaluation
                }

                SyntaxKind.MultiplyToken -> {
                    leftEvaluation * rightEvaluation
                }


                else -> throw Exception("unExpected binary operator ${expression.kind}")
            }


        }

        if (expression is ParenthesizedExpressionSyntax) {
            return evaluateExpression(expression.coreExpression)
        }
        throw Exception("unExpected node operator kind ${expression.kind}")
    }
}