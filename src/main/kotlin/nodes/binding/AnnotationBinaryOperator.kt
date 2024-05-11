package com.moj.najiz.nodes.binding

import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import kotlin.reflect.typeOf

class AnnotationBinaryOperator(
    private val _syntaxKind: SyntaxKind,
    private val _annotationKind: AnnotationBinaryOperatorKind,
    private val _operandKind: List<Any>? = null,
    private val _type: List<Any>,
) {


    companion object {
        private val binaryOperators =
            listOf(
                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.MathOperatorsToken.PlusToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Addition,
                    _operandKind = listOf(typeOf<Int>()),
                    _type = listOf(Int),
                ),
                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.MathOperatorsToken.MinusToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Subtraction,
                    _operandKind = listOf(typeOf<Int>()),
                    _type = listOf(Int),
                ),
                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.MathOperatorsToken.SlashToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Division,
                    _operandKind = listOf(typeOf<Int>()),
                    _type = listOf(Int),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.MathOperatorsToken.MultiplyToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Multiplication,
                    _operandKind = listOf(typeOf<Int>()),
                    _type = listOf(Int),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.EqualityToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Equality,
                    _operandKind = listOf(typeOf<Int>(), typeOf<Boolean>()),
                    _type = listOf(Int, Boolean),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.NotEqualityToken,
                    _annotationKind = AnnotationBinaryOperatorKind.NotEquality,
                    _operandKind = listOf(typeOf<Int>(), typeOf<Boolean>()),
                    _type = listOf(Int, Boolean),
                ),


                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.BooleanOperatorToken.OrToken,
                    _annotationKind = AnnotationBinaryOperatorKind.LogicalOr,
                    _operandKind = listOf(typeOf<Boolean>()),
                    _type = listOf(Boolean),
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.BooleanOperatorToken.AndToken,
                    _annotationKind = AnnotationBinaryOperatorKind.LogicalAnd,
                    _operandKind = listOf(typeOf<Boolean>()),
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

    val type: Any
        get() = _type


}