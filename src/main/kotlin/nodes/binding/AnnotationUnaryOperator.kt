package com.moj.najiz.nodes.binding

import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import kotlin.reflect.typeOf

class AnnotationUnaryOperator(
    private val _syntaxKind: SyntaxKind,
    private val _annotationKind: AnnotationUnaryOperatorKind,
    private val _operandKind: Any,
    private val _type: Any? = null,
) {

    companion object {
        private val unaryOperators =
            listOf(
                AnnotationUnaryOperator(
                    _syntaxKind = SyntaxKind.Token.BooleanOperatorToken.NotToken,
                    _annotationKind = AnnotationUnaryOperatorKind.LogicalNegation,
                    _operandKind = typeOf<Boolean>()
                ),
                AnnotationUnaryOperator(
                    _syntaxKind = SyntaxKind.Token.MathOperatorsToken.PlusToken,
                    _annotationKind = AnnotationUnaryOperatorKind.Identity,
                    _operandKind = typeOf<Int>()
                ),
                AnnotationUnaryOperator(
                    _syntaxKind = SyntaxKind.Token.MathOperatorsToken.MinusToken,
                    _annotationKind = AnnotationUnaryOperatorKind.Negation,
                    _operandKind = typeOf<Int>()
                ),
            )

        fun annotate(syntaxKind: SyntaxKind, operandKind: Any): AnnotationUnaryOperator? {

            unaryOperators.forEach { operand ->
                if (operand._syntaxKind == syntaxKind && operand._operandKind == operandKind) {
                    return operand

                }
            }

            return null
        }
    }

    val annotationKind: AnnotationUnaryOperatorKind
        get() = this._annotationKind

    val operandKind: Any
        get() = _operandKind

    val type: Any?
        get() = _type

}