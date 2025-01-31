package com.moj.najiz.frontend.annotator

import com.moj.najiz.di.DIContainer
import com.moj.najiz.frontend.VariableSymbol
import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind.Token.VariableDeclaration
import com.moj.najiz.frontend.nodes.annotation.*
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationAssignmentExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationBinaryExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationLiteralExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationNameExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationNumberExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationParenthesizedExpression
import com.moj.najiz.frontend.nodes.annotation.expressions.AnnotationUnaryExpression
import com.moj.najiz.frontend.nodes.annotation.scope.AnnotatedGlobalScope
import com.moj.najiz.frontend.nodes.annotation.scope.AnnotatedScope
import com.moj.najiz.frontend.nodes.annotation.statements.AnnotationBlockStatement
import com.moj.najiz.frontend.nodes.annotation.statements.AnnotationDeclarationVariableStatement
import com.moj.najiz.frontend.nodes.annotation.statements.AnnotationExpressionStatement
import com.moj.najiz.frontend.nodes.syntax.expressions.AssignmentExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.BinaryExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.CompilationUnitSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.LiteralExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.NameExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.NumberExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.ParenthesizedExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.UnaryExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.BlockStatementSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.ExpressionStatementSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.VariableDeclarationStatementSyntax
import com.moj.najiz.utils.getKType
import com.moj.najiz.utils.isNumeric
import frontend.nodes.annotation.statements.AnnotationStatement
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
                return AnnotationLiteralExpression(0)
            }

            val value = _annotatedScope.get(expression.identifier.text!!)
            value?.let {

                val operandKind = value::class.java
                return AnnotationNameExpression(
                    value,
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
            val variable = VariableSymbol(name ?: "", getKType(annotatedValue), false)
            if (!annotatedScope.tryLookUp(name)) {
                annotatedScope.tryDeclare(variable, annotatedValue.value)
            }

            if(annotatedScope.get(name ?: "")?.isReadOnly == true){
                _diagnostics.reportValCannotBeReAssigned(expression.operator.textSpan,name)
                return AnnotationLiteralExpression(0)
            }



            return AnnotationAssignmentExpression(variable, annotatedValue)
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
        } else {
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

    fun annotate(statementSyntax: ExpressionStatementSyntax): AnnotationStatement =
        AnnotationExpressionStatement(statementSyntax.expression.annotate(this))


    fun annotate(statement: VariableDeclarationStatementSyntax): AnnotationStatement {
        val name = statement.identifier.text
        val isReadOnly = statement.modifier.kind == VariableDeclaration.ValToken
        val initializer = statement.expression.annotate(this)



        if (!_annotatedScope.tryLookUp(name)) {
            _diagnostics.reportVariableIsAlreadyDeclared(statement.identifier.textSpan, name)
        }

        val variable = VariableSymbol(name!!, getKType(initializer), isReadOnly)

        return AnnotationDeclarationVariableStatement(
            variable,
            initializer
        )
    }


    fun annotate(statementSyntax: BlockStatementSyntax): AnnotationStatement {
        val annotatedStatements = mutableListOf<AnnotationStatement>()

        statementSyntax.statements.forEach {
            annotatedStatements.add(it.annotate(this@Annotator))
        }
        return AnnotationBlockStatement(annotatedStatements.toTypedArray())
    }


    private fun annotateUnaryOperatorKind(
        operandKind: SyntaxKind,
    ): AnnotationUnaryOperator? {

        return when (operandKind) {
            SyntaxKind.Token.Math.OperatorsTokens.PlusToken -> AnnotationUnaryOperator.Companion.annotate(
                syntaxKind = SyntaxKind.Token.Math.OperatorsTokens.PlusToken,
                operandKind = typeOf<Int>()
            )

            SyntaxKind.Token.Math.OperatorsTokens.MinusToken -> AnnotationUnaryOperator.Companion.annotate(
                syntaxKind = SyntaxKind.Token.Math.OperatorsTokens.MinusToken,
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
                SyntaxKind.Token.Math.OperatorsTokens.PlusToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.OperatorsTokens.PlusToken,
                    leftType = Int,
                    rightType = Int
                )

                SyntaxKind.Token.Math.OperatorsTokens.MinusToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.OperatorsTokens.MinusToken,
                    leftType = Int,
                    rightType = Int
                )

                SyntaxKind.Token.Math.OperatorsTokens.MultiplyToken -> AnnotationBinaryOperator.Companion.annotate(

                    syntaxKind = SyntaxKind.Token.Math.OperatorsTokens.MultiplyToken,
                    leftType = Int,
                    rightType = Int
                )

                SyntaxKind.Token.Math.OperatorsTokens.SlashToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.OperatorsTokens.SlashToken,
                    leftType = Int,
                    rightType = Int
                )

                SyntaxKind.Token.Math.EqualityTokens.EqualityToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.EqualityTokens.EqualityToken,
                    leftType = Int,
                    rightType = Int,
                )

                SyntaxKind.Token.Math.EqualityTokens.NotEqualityToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.EqualityTokens.NotEqualityToken,
                    leftType = Int,
                    rightType = Int,
                )


                SyntaxKind.Token.Math.EqualityTokens.GreaterThanToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.EqualityTokens.GreaterThanToken,
                    leftType = Int,
                    rightType = Int,
                )

                SyntaxKind.Token.Math.EqualityTokens.GreaterThanOrEqualToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.EqualityTokens.GreaterThanOrEqualToken,
                    leftType = Int,
                    rightType = Int,
                )


                SyntaxKind.Token.Math.EqualityTokens.LessThanToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.EqualityTokens.LessThanToken,
                    leftType = Int,
                    rightType = Int,
                )


                SyntaxKind.Token.Math.EqualityTokens.LessThanOrEqualToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.EqualityTokens.LessThanOrEqualToken,
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

                SyntaxKind.Token.Math.EqualityTokens.EqualityToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.EqualityTokens.EqualityToken,
                    leftType = Boolean,
                    rightType = Boolean
                )

                SyntaxKind.Token.Math.EqualityTokens.NotEqualityToken -> AnnotationBinaryOperator.Companion.annotate(
                    syntaxKind = SyntaxKind.Token.Math.EqualityTokens.NotEqualityToken,
                    leftType = Boolean,
                    rightType = Boolean
                )

                else -> throw Exception("unexpected token $operandKind")
            }
        }

        return null
    }


}