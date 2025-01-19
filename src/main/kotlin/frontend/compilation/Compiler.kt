package com.moj.najiz.frontend.compilation

import com.moj.najiz.di.DIContainer
import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.diagnostics.plus
import com.moj.najiz.frontend.expressionEvaluator.EvaluationResult
import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.nodes.annotation.scope.AnnotatedGlobalScope
import com.moj.najiz.frontend.nodes.syntax.SyntaxTree

class Compiler() {
    private lateinit  var syntaxTree: SyntaxTree


    fun setSyntaxTree(syntaxTree: SyntaxTree)
    {
        this.syntaxTree = syntaxTree
    }

    private lateinit var globalScope: AnnotatedGlobalScope


    fun compile(): EvaluationResult {
        //todo
       globalScope = Annotator.annotateGlobalScope(syntaxTree.root)
        val evaluatorDiagnostics: Diagnostic.DiagnosticsBag = syntaxTree.diagnosticsBag + globalScope.diagnostics
        if (evaluatorDiagnostics.distinct().isNotEmpty()) return EvaluationResult(evaluatorDiagnostics, null)
        val evaluator = DIContainer.getEvaluator(globalScope)
        val evaluatedResult = globalScope.expression.evaluate(evaluator)
        return EvaluationResult(Diagnostic.DiagnosticsBag(), evaluatedResult)

    }

}