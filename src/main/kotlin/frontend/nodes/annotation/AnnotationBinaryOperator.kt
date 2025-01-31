package com.moj.najiz.frontend.nodes.annotation

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import kotlin.reflect.typeOf

class AnnotationBinaryOperator(
    private val _syntaxKind: SyntaxKind,
    private val _annotationKind: AnnotationBinaryOperatorKind,
    private val _operandKind: Any? = null,
    private val _resultKind: Any,
    private val _type: List<Any>,
) {


    companion object {
        private val binaryOperators =
            listOf(
                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.Math.OperatorsTokens.PlusToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Addition,
                    _operandKind = typeOf<Int>(),
                    _resultKind = typeOf<Int>(),
                    _type = listOf(Int),
                ),
                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.Math.OperatorsTokens.MinusToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Subtraction,
                    _operandKind = typeOf<Int>(),
                    _resultKind = typeOf<Int>(),
                    _type = listOf(Int),
                ),
                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.Math.OperatorsTokens.SlashToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Division,
                    _operandKind = typeOf<Int>(),
                    _resultKind = typeOf<Int>(),
                    _type = listOf(Int),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.Math.OperatorsTokens.MultiplyToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Multiplication,
                    _operandKind = typeOf<Int>(),
                    _resultKind = typeOf<Int>(),
                    _type = listOf(Int),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.Math.EqualityTokens.GreaterThanToken,
                    _annotationKind = AnnotationBinaryOperatorKind.GreaterThan,
                    _operandKind = typeOf<Int>(),
                    _resultKind = true,
                    _type = listOf(Int),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.Math.EqualityTokens.GreaterThanOrEqualToken,
                    _annotationKind = AnnotationBinaryOperatorKind.GreaterThanOrEqual,
                    _operandKind = typeOf<Int>(),
                    _resultKind = true,
                    _type = listOf(Int),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.Math.EqualityTokens.LessThanToken,
                    _annotationKind = AnnotationBinaryOperatorKind.LessThan,
                    _operandKind = typeOf<Int>(),
                    _resultKind = true,
                    _type = listOf(Int),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.Math.EqualityTokens.LessThanOrEqualToken,
                    _annotationKind = AnnotationBinaryOperatorKind.LessThanOrEqual,
                    _operandKind = typeOf<Int>(),
                    _resultKind = true,
                    _type = listOf(Int),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.Math.EqualityTokens.EqualityToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Equality,
                    _operandKind = typeOf<Int>(),
                    _resultKind = true,
                    _type = listOf(Int, Boolean),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.Math.EqualityTokens.NotEqualityToken,
                    _annotationKind = AnnotationBinaryOperatorKind.NotEquality,
                    _operandKind = typeOf<Int>(),
                    _resultKind = true,
                    _type = listOf(Int, Boolean),
                ),


                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.BooleanOperatorToken.OrToken,
                    _annotationKind = AnnotationBinaryOperatorKind.LogicalOr,
                    _operandKind = typeOf<Boolean>(),
                    _resultKind = typeOf<Boolean>(),
                    _type = listOf(Boolean),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.BooleanOperatorToken.AndToken,
                    _annotationKind = AnnotationBinaryOperatorKind.LogicalAnd,
                    _operandKind = typeOf<Boolean>(),
                    _resultKind = typeOf<Boolean>(),
                    _type = listOf(Boolean),
                ),
            )

        fun annotate(syntaxKind: SyntaxKind, leftType: Any, rightType: Any): AnnotationBinaryOperator? {

            binaryOperators.forEach { operand ->
                if (operand._syntaxKind == syntaxKind &&
                    leftType in operand._type && rightType in operand._type
                ) {
                    return operand
                }
            }

            return null
        }
    }


    val annotationKind: AnnotationBinaryOperatorKind
        get() = this._annotationKind

    val operandKind: Any?
        get() = _operandKind

    val resultKind: Any
        get() = _resultKind

    val type: Any
        get() = _type


}