package com.moj.najiz.expressionEvaluator

import com.moj.najiz.nodes.binding.*

class Evaluator(private val root: AnnotationExpression) {

    fun getEvaluator(): Any {
        return evaluateExpression(root)
    }


    private fun evaluateExpression(expression: AnnotationExpression): Any {
        if (expression is AnnotationLiteralExpression) {
            return expression.value
        }

        if (expression is AnnotationBinaryExpression) {
            val leftEvaluation = evaluateExpression(expression.leftExpression)
            val rightEvaluation = evaluateExpression(expression.rightExpression)

            if (leftEvaluation is Int && rightEvaluation is Int) {
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


            if (leftEvaluation is Boolean && rightEvaluation is Boolean) {
                return when (expression.operandKind) {
                    AnnotationBinaryOperatorKind.LogicalOr -> {
                        leftEvaluation || rightEvaluation
                    }

                    AnnotationBinaryOperatorKind.LogicalAnd -> {
                        leftEvaluation && rightEvaluation
                    }


                    else -> throw Exception("unExpected binary operator ${expression.operandKind}")
                }
            }

        }

        if (expression is AnnotationUnaryExpression) {
            val operand = evaluateExpression(expression.annotationExpression)
            return when (expression.operandKind) {
                AnnotationUnaryOperatorKind.Identity -> {
                    operand as Int
                }

                AnnotationUnaryOperatorKind.Negation -> {
                    -(operand as Int)
                }


                AnnotationUnaryOperatorKind.LogicalNegation -> {
                    !(operand as Boolean)
                }

                else -> throw Exception("unExpected unary  operator ${expression.operandKind}")
            }
        }

        throw Exception("unExpected node operator kind ${expression.annotationKind}")
    }
}