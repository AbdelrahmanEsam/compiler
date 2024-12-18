package com.moj.najiz.diagnostics

import com.moj.najiz.diagnostics.Diagnostic.DiagnosticsBag
import kotlin.reflect.KClass
import kotlin.reflect.KType

class Diagnostic(private val _textSpan: TextSpan, private val _message: String) {

    val textSpan: TextSpan
        get() = _textSpan


    val message: String
        get() = _message


    class DiagnosticsBag : Iterable<Diagnostic> {

        private val _diagnostics: MutableList<Diagnostic> = mutableListOf()
        val diagnostics: List<Diagnostic> = _diagnostics.toList()
        override fun iterator(): Iterator<Diagnostic> = _diagnostics.listIterator()

        fun addDiagnostics(diagnostics: List<Diagnostic>) {
            this._diagnostics.addAll(diagnostics)
        }


        private fun report(span: TextSpan, message: String) {
            _diagnostics.add(Diagnostic(span, message))
        }

        fun reportInvalidInt(textSpan: TextSpan, text: String, type: KType) {
            val message = "the number $text isn't valid type"
            report(span = textSpan, message)

        }

        fun reportInvalidDouble(textSpan: TextSpan, text: String, type: KType) {
            val message = "the number $text isn't valid type"
            report(span = textSpan, message)

        }

        fun reportBadCharacter(position: Int, char: String) {
            val message = "error bad character in input : $char"
            report(span = TextSpan(_start = position, _length = 1), message = message)

        }

        fun reportUnExpectedError(textSpan: TextSpan, message: String) {
            val message = "error unExpected error in input : $message"
            report(span = textSpan, message = message)
        }

        fun reportUndefinedUnaryOperator(textSpan: TextSpan, actualValue: String?, type: KClass<out Any>) {
            val message = "binary operator $actualValue +\n  isn't defined for $type"
            report(textSpan, message)

        }

        fun reportUndefinedBinaryOperator(
            textSpan: TextSpan,
            operator: String?,
            rightExpressionType: KClass<out Any>,
            leftExpressionType: KClass<out Any>,
        ) {

            val message =
                "binary operator $operator isn't defined as binary operator for $leftExpressionType and $rightExpressionType"
            report(textSpan, message)

        }


//        fun reportUndefinedParenthesizedOperator(
//            value: Any,
//            kind: SyntaxKind,
//            annotatedExpressionValue: KClass<out Any>
//        ) {
//            val message = "parenthesized operator $value isn't defined as $kind and $annotatedExpressionValue"
//            report(t)
//        }
    }


}

operator fun DiagnosticsBag.plus(secondBag: DiagnosticsBag): DiagnosticsBag {
    val newBag = DiagnosticsBag()
    newBag.addDiagnostics(this.diagnostics)
    newBag.addDiagnostics(secondBag.diagnostics)
    return newBag
}

