package com.moj.najiz.nodes.binding

import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import kotlin.reflect.typeOf

class AnnotationBinaryOperator(
    private val _syntaxKind: SyntaxKind,
    private val _annotationKind: AnnotationBinaryOperatorKind,
    private val _operandKind: Any? = null,
    private val _type: Any,
) {


    companion object {
        private val binaryOperators =
            listOf(
                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.MathOperatorsToken.PlusToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Addition,
                    _operandKind = typeOf<Int>(),
                    _type = Int,
                ),
                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.MathOperatorsToken.MinusToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Subtraction,
                    _operandKind = typeOf<Int>(),
                    _type = Int,
                ),
                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.MathOperatorsToken.SlashToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Division,
                    _operandKind = typeOf<Int>(),
                    _type = Int,
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.MathOperatorsToken.MultiplyToken,
                    _annotationKind = AnnotationBinaryOperatorKind.Multiplication,
                    _operandKind = typeOf<Int>(),
                    _type = Int,
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.BooleanOperatorToken.OrToken,
                    _annotationKind = AnnotationBinaryOperatorKind.LogicalOr,
                    _operandKind = typeOf<Boolean>(),
                    _type = Boolean,
                ),

                AnnotationBinaryOperator(
                    _syntaxKind = SyntaxKind.Token.BooleanOperatorToken.AndToken,
                    _annotationKind = AnnotationBinaryOperatorKind.LogicalAnd,
                    _operandKind = typeOf<Boolean>(),
                    _type = Boolean,
                ),
            )

        fun annotate(syntaxKind: SyntaxKind, leftType: Any, rightType: Any): AnnotationBinaryOperator? {

            binaryOperators.forEach { operand ->
                if (operand._syntaxKind == syntaxKind &&
                    operand._type == leftType && operand._type == rightType
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