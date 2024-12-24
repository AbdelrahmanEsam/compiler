package com.moj.najiz.frontend.expressionEvaluator

import com.moj.najiz.frontend.nodes.binding.*

class Evaluator {


    fun evaluate(expression: AnnotationLiteralExpression): Any {
        return expression.value
    }

    fun evaluate(expression: AnnotationParenthesizedExpression): Any {
        return expression.coreExpression.evaluate(this)
    }

    fun evaluate(expression: AnnotationBinaryExpression): Any {
        val leftEvaluation = expression.leftExpression.evaluate(this)
        val rightEvaluation = expression.rightExpression.evaluate(this)



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
        throw Exception("unExpected node operator kind ${expression.annotationKind}")
    }




    fun evaluate(expression: AnnotationUnaryExpression): Any {
        val operand = expression.annotationExpression.evaluate(this)
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
}