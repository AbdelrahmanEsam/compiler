package com.moj.najiz.frontend.compilation

import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.diagnostics.plus
import com.moj.najiz.frontend.expressionEvaluator.EvaluationResult
import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.binding.Annotator
import com.moj.najiz.frontend.nodes.syntax.SyntaxTree

class Compiler(private val _syntaxTree: SyntaxTree) {
    private val syntaxTree: SyntaxTree
        get() = _syntaxTree


    fun evaluate(annotator: Annotator, evaluator: Evaluator): EvaluationResult {

        val annotatedResult = syntaxTree.root.annotate(annotator)
        val evaluatorDiagnostics: Diagnostic.DiagnosticsBag = syntaxTree.diagnosticsBag + annotator.diagnostics
        if (evaluatorDiagnostics.distinct().isNotEmpty()) return EvaluationResult(evaluatorDiagnostics, null)

        val evaluatedResult = annotatedResult.evaluate(evaluator)
        return EvaluationResult(Diagnostic.DiagnosticsBag(), evaluatedResult)

    }

}