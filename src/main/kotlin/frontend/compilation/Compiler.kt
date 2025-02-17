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
    private var previousCompilation : Compiler? = null


    fun setSyntaxTree(syntaxTree: SyntaxTree)
    {
        this.syntaxTree = syntaxTree
    }


    constructor(compilation : Compiler , syntaxTree : SyntaxTree) : this() {
        this.syntaxTree = syntaxTree
        this.previousCompilation = compilation
    }

    fun continueWith(syntaxTree : SyntaxTree) : Compiler = Compiler(this,syntaxTree)

     val  globalScope: AnnotatedGlobalScope by lazy{ Annotator.annotateGlobalScope(previousCompilation?.globalScope,syntaxTree.root) }




    fun compile(): EvaluationResult {
        val evaluatorDiagnostics: Diagnostic.DiagnosticsBag = syntaxTree.diagnosticsBag + globalScope.diagnostics
        if (evaluatorDiagnostics.distinct().isNotEmpty()) return EvaluationResult(evaluatorDiagnostics, null)
        val evaluator = DIContainer.getEvaluator(globalScope)
        val evaluatedResult = globalScope.statement.evaluate(evaluator)
        return EvaluationResult(evaluatorDiagnostics, evaluatedResult)
    }

}