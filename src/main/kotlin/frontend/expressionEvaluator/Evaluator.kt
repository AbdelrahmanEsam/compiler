package com.moj.najiz.frontend.expressionEvaluator

import com.moj.najiz.frontend.VariableSymbol
import com.moj.najiz.frontend.nodes.annotation.*
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationAssignmentExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationBinaryExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationLiteralExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationNumberExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationParenthesizedExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationUnaryExpression
import com.moj.najiz.frontend.nodes.annotation.scope.AnnotatedGlobalScope
import com.moj.najiz.frontend.nodes.annotation.statements.AnnotationBlockStatement
import com.moj.najiz.frontend.nodes.annotation.statements.AnnotationExpressionStatement
import com.moj.najiz.frontend.nodes.annotation.statements.AnnotationDeclarationVariableStatement
import com.moj.najiz.frontend.nodes.annotation.statements.AnnotationIfStatement
import com.moj.najiz.frontend.nodes.annotation.statements.AnnotationWhileStatement
import com.moj.najiz.utils.isNumeric

class Evaluator(private val annotatedScope: AnnotatedGlobalScope? = null) {


    fun evaluate(expression: AnnotationLiteralExpression): Any = expression.value
    fun evaluate(expression: AnnotationDeclarationVariableStatement) {
        val value = expression.expression.evaluate(this)
        value?.let {
            annotatedScope?.tryDeclare(expression.variable, value)
        }
    }

    fun evaluate(expression: AnnotationNumberExpression): Any = expression.value


    fun evaluate(expression: AnnotationParenthesizedExpression): Any? = expression.coreExpression.evaluate(this)


    fun evaluate(expression: AnnotationBinaryExpression): Any {
        val rightEvaluation = expression.rightExpression.evaluate(this)
        val leftEvaluation = expression.leftExpression.evaluate(this)



        if (expression.operand.annotationKind == AnnotationBinaryOperatorKind.Equality) {
            return leftEvaluation == rightEvaluation
        }

        if (expression.operand.annotationKind == AnnotationBinaryOperatorKind.NotEquality) {
            return leftEvaluation != rightEvaluation
        }

        if (expression.operand.annotationKind == AnnotationBinaryOperatorKind.GreaterThan) {
            return leftEvaluation.toString().toInt() > rightEvaluation.toString().toInt()
        }

        if (expression.operand.annotationKind == AnnotationBinaryOperatorKind.GreaterThanOrEqual) {
            return leftEvaluation.toString().toInt() >= rightEvaluation.toString().toInt()
        }

        if (expression.operand.annotationKind == AnnotationBinaryOperatorKind.LessThan) {
            return leftEvaluation.toString().toInt() < rightEvaluation.toString().toInt()
        }

        if (expression.operand.annotationKind == AnnotationBinaryOperatorKind.LessThanOrEqual) {
            return leftEvaluation.toString().toInt() <= rightEvaluation.toString().toInt()
        }


        if (leftEvaluation.toString().isNumeric() && rightEvaluation.toString().isNumeric()) {
            val leftExpressionValue = leftEvaluation.toString().toDouble()
            val rightEvaluationValue = rightEvaluation.toString().toDouble()
            val expressionResult = when (expression.operand.annotationKind) {
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


            if (leftEvaluation is Int) {
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

    fun evaluate(expression: AnnotationAssignmentExpression): Any? {
        val evaluatedValue = expression.expression.evaluate(this)
        evaluatedValue?.let {
            annotatedScope?.tryDeclare(expression.variable, evaluatedValue)
        }
        return annotatedScope?.get(expression.variable)
    }

    fun evaluate(variable: VariableSymbol): Any? = annotatedScope?.get(variable)?.second

    fun evaluate(statement: AnnotationBlockStatement) = statement.statements.forEach {
        it.evaluate(this)
    }

    fun evaluate(statement: AnnotationExpressionStatement): Any? = statement.expression.evaluate(this)


    fun evaluate(statement: AnnotationIfStatement): Any? {

        if (statement.ifCondition.evaluate(this) == true) {
            return statement.thenStatement.evaluate(this)
        }

        return statement.elseStatement?.evaluate(this)
    }

    fun evaluate(statement: AnnotationWhileStatement): Any? {
        statement.headStatements.forEach {
            it.evaluate(this)
        }
        while (statement.condition.evaluate(this) == true) {
            statement.thenStatements.forEach {
                it.evaluate(this)
            }
        }

        return null
    }
}