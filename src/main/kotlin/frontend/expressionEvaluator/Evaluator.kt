package com.moj.najiz.frontend.expressionEvaluator

import com.moj.najiz.frontend.nodes.annotation.*
import com.moj.najiz.frontend.nodes.annotation.scope.AnnotatedGlobalScope
import com.moj.najiz.utils.isNumeric

class Evaluator(private val annotatedScope: AnnotatedGlobalScope? = null) {


    fun evaluate(expression: AnnotationLiteralExpression): Any {
        return expression.value
    }

    fun evaluate(expression: AnnotationNumberExpression): Any = expression.value


    fun evaluate(expression: AnnotationParenthesizedExpression): Any? {
        return expression.coreExpression.evaluate(this)
    }


    fun evaluate(expression: AnnotationBinaryExpression): Any {
        val rightEvaluation = expression.rightExpression.evaluate(this)
        val leftEvaluation = expression.leftExpression.evaluate(this)



        if (expression.operand.annotationKind == AnnotationBinaryOperatorKind.Equality) {
            return leftEvaluation == rightEvaluation
        }

        if (expression.operand.annotationKind == AnnotationBinaryOperatorKind.NotEquality) {
            return leftEvaluation != rightEvaluation
        }


        if (leftEvaluation.toString().isNumeric() && rightEvaluation.toString().isNumeric()) {
            val leftExpressionValue = leftEvaluation.toString().toDouble()
            val rightEvaluationValue = rightEvaluation.toString().toDouble()
            val expressionResult =  when (expression.operand.annotationKind) {
                AnnotationBinaryOperatorKind.Addition -> {
                    leftExpressionValue + rightEvaluationValue
                }

                AnnotationBinaryOperatorKind.Subtraction -> {
                    leftExpressionValue - rightEvaluationValue
                }

                AnnotationBinaryOperatorKind.Division -> {
                    leftExpressionValue / rightEvaluationValue
                }

                AnnotationBinaryOperatorKind.Multiplication -> {
                    leftExpressionValue * rightEvaluationValue
                }




                else -> throw Exception("unExpected binary operator ${expression.operand}")
            }

            if(leftEvaluation is Int){
                return expressionResult.toInt()
            }

            return expressionResult
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

    fun evaluate(expression: AnnotationAssignmentExpression,name : String): Any? {
        val evaluatedValue = expression.expression.evaluate(this)
        evaluatedValue?.let {
            annotatedScope?.tryDeclare(name , evaluatedValue)
        }
        return annotatedScope?.get(name)
    }

    fun evaluate(name : String): Any? {
        return annotatedScope?.get(name)
    }
}