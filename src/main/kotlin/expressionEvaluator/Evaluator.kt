package com.moj.najiz.expressionEvaluator

import com.moj.najiz.nodes.binding.*

class Evaluator(private val root: AnnotationExpression) {

    fun getEvaluator(): Int {
        return evaluateExpression(root)
    }


    private fun evaluateExpression(expression: AnnotationExpression): Int {
        if (expression is AnnotationLiteralExpression) {
            return expression.value as Int
        }

        if (expression is AnnotationBinaryExpression) {
            val leftEvaluation = evaluateExpression(expression.leftExpression)
            val rightEvaluation = evaluateExpression(expression.rightExpression)

            return when (expression.operandKind) {
                AnnotationBinaryOperatorKind.Addition -> {
                    leftEvaluation + rightEvaluation
                }

                AnnotationBinaryOperatorKind.Subtraction -> {
                    leftEvaluation - rightEvaluation
                }

                AnnotationBinaryOperatorKind.Division -> {
                    leftEvaluation / rightEvaluation
                }

                AnnotationBinaryOperatorKind.Multiplication -> {
                    leftEvaluation * rightEvaluation
                }


                else -> throw Exception("unExpected binary operator ${expression.operandKind}")
            }


        }

        if (expression is AnnotationUnaryExpression) {
            val operand = evaluateExpression(expression.annotationExpression)
            return when (expression.operandKind) {
                AnnotationUnaryOperatorKind.Identity -> {
                    operand
                }

                AnnotationUnaryOperatorKind.Negation -> {
                    -operand
                }

                else -> throw Exception("unExpected unary  operator ${expression.operandKind}")
            }
        }

//
        throw Exception("unExpected node operator kind ${expression.annotationKind}")
    }
}