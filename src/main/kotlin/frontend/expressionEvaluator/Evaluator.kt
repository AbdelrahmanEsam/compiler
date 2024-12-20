package com.moj.najiz.frontend.expressionEvaluator

import com.moj.najiz.frontend.nodes.binding.*

class Evaluator {


    fun evaluateExpression(expression: AnnotationExpression): Any {
        if (expression is AnnotationLiteralExpression) {
            return expression.value
        }

        if (expression is AnnotationBinaryExpression) {
            val leftEvaluation = evaluateExpression(expression.leftExpression)
            val rightEvaluation = evaluateExpression(expression.rightExpression)


            if (expression.operand.annotationKind == AnnotationBinaryOperatorKind.Equality) {
                return leftEvaluation == rightEvaluation
            }

            if (expression.operand.annotationKind == AnnotationBinaryOperatorKind.NotEquality) {
                return leftEvaluation != rightEvaluation
            }


            if (leftEvaluation is Int && rightEvaluation is Int) {
                return when (expression.operand.annotationKind) {
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


                    else -> throw Exception("unExpected binary operator ${expression.operand}")
                }
            }


            if (leftEvaluation is Boolean && rightEvaluation is Boolean) {
                return when (expression.operand.annotationKind) {
                    AnnotationBinaryOperatorKind.LogicalOr -> {
                        leftEvaluation || rightEvaluation
                    }

                    AnnotationBinaryOperatorKind.LogicalAnd -> {
                        leftEvaluation && rightEvaluation
                    }


                    else -> throw Exception("unExpected binary operator ${expression.operand}")
                }
            }


        }

        if (expression is AnnotationUnaryExpression) {
            val operand = evaluateExpression(expression.annotationExpression)
            return when (expression.operand.annotationKind) {
                AnnotationUnaryOperatorKind.Identity -> {
                    operand as Int
                }

                AnnotationUnaryOperatorKind.Negation -> {
                    -(operand as Int)
                }


                AnnotationUnaryOperatorKind.LogicalNegation -> {
                    !(operand as Boolean)
                }

                else -> throw Exception("unExpected unary  operator ${expression.operand}")
            }
        }

        if (expression is AnnotationParenthesizedExpression) {
            return evaluateExpression(expression.coreExpression)
        }

        throw Exception("unExpected node operator kind ${expression.annotationKind}")
    }
}