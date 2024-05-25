package com.moj.najiz.expressionEvaluator

import com.moj.najiz.diagnostics.Diagnostic

class EvaluationResult(private val _diagnostics: Diagnostic.DiagnosticsBag, private val _value: Any?) {

    val diagnostics:  Diagnostic.DiagnosticsBag
        get() = _diagnostics

    val value: Any?
        get() = _value

}