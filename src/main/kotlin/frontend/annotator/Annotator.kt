package com.moj.najiz.frontend.annotator

import com.moj.najiz.di.DIContainer
import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.annotation.*
import com.moj.najiz.frontend.nodes.annotation.scope.AnnotatedGlobalScope
import com.moj.najiz.frontend.nodes.annotation.scope.AnnotatedScope
import com.moj.najiz.frontend.nodes.syntax.*
import com.moj.najiz.utils.isNumeric
import java.util.*
import kotlin.reflect.typeOf

class Annotator(parentScope: AnnotatedScope? = null) {

    private val _diagnostics: Diagnostic.DiagnosticsBag = Diagnostic.DiagnosticsBag()
    private var _annotatedScope: AnnotatedScope = DIContainer.getAnnotatedScope(parentScope)
    val annotatedScope: AnnotatedScope
        get() = _annotatedScope
    val diagnostics: Diagnostic.DiagnosticsBag
        get() = _diagnostics

    companion object {
        fun annotateGlobalScope(
            previousGlobalScope: AnnotatedGlobalScope?,
            syntax: CompilationUnitSyntax
        ): AnnotatedGlobalScope {
            val parentScope = createParentScope(previousGlobalScope)
            DIContainer.getAnnotator(parentScope).apply {
                return AnnotatedGlobalScope(
                    previousGlobalScope,
                    this.diagnostics,
                    this.annotatedScope.variables.toMutableMap(),
                    syntax.annotate(this)
                )
            }
        }

        fun createParentScope(previousGlobalScope: AnnotatedGlobalScope?): AnnotatedScope? {

            var previousScope = previousGlobalScope
            var parentScope: AnnotatedScope? = null

            val scopesStack = Stack<AnnotatedGlobalScope>()
            while (previousScope != null) {
                scopesStack.push(previousScope)
                previousScope = previousScope.previousScope
            }

            while (scopesStack.count() > 0) {
                previousScope = scopesStack.pop()
                val scope = AnnotatedScope(parentScope)
                previousScope.variables.forEach { scope.tryDeclare(it.key, it.value) }
                parentScope = scope
            }
            return parentScope
        }

    }


    fun annotate(expression: NameExpressionSyntax): AnnotationExpression {
        expression.apply {
            if (!_annotatedScope.tryLookUp(expression.identifier.text)) {
                _diagnostics.reportUndefinedName(expression.identifier.textSpan, expression.identifier.text)
            }
            val value = _annotatedScope.get(expression.identifier.text)
            value?.let {

                val operandKind = value::class.java
                return AnnotationNameExpression(
                    name = expression.identifier.text!!,
                    type = operandKind
                )
            }
        }
        return AnnotationLiteralExpression(
            0
        )
    }

    fun annotate(expression: AssignmentExpressionSyntax): AnnotationExpression {
        expression.apply {
            val name = this.identifier.text
            val annotatedValue = expression.expression.annotate(this@Annotator)
                if(!annotatedScope.tryLookUp(name)){
                    annotatedScope.tryDeclare(name ?: "", annotatedValue.value)
                }



            return AnnotationAssignmentExpression(name ?: "", annotatedValue)
        }

    }


    fun annotate(expression: BinaryExpressionSyntax): AnnotationExpression {
        val rightAnnotatedExpression = expression.rightExpression.annotate(this)

        val actualRightValue = if (rightAnnotatedExpression is AnnotationNameExpression) {
            _annotatedScope.get(rightAnnotatedExpression.value.toString())
        } else if (rightAnnotatedExpression is AnnotationParenthesizedExpression) {
            _annotatedScope.get(rightAnnotatedExpression.coreExpression.value.toString())
        } else {
            rightAnnotatedExpression.value
        }



        val leftAnnotatedExpression = expression.leftExpression.annotate(this)
        val actualLeftValue = if (leftAnnotatedExpression is AnnotationNameExpression) {
            _annotatedScope.get(leftAnnotatedExpression.value.toString())
        } else if (rightAnnotatedExpression is AnnotationParenthesizedExpression) {
            _annotatedScope.get(rightAnnotatedExpression.coreExpression.value.toString())
            }else {
            leftAnnotatedExpression.value
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


    fun annotate(expression: ParenthesizedExpressionSyntax): AnnotationExpression {
        expression.apply {


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
        if (leftValue.toString().isNumeric() && rightValue.toString().isNumeric()) {
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