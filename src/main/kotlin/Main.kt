package com.moj.najiz

import com.moj.najiz.compilation.Compiler
import com.moj.najiz.expressionEvaluator.Evaluator
import com.moj.najiz.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.nodes.binding.Annotator
import com.moj.najiz.parser.Parser
import com.moj.najiz.nodes.syntax.SyntaxNode
import com.moj.najiz.nodes.syntax.SyntaxToken

fun main() {

    val code = readlnOrNull()?.trim()
    code?.let {
        val lexicalAnalyzer = LexicalAnalyzer(code)
        val parser = Parser(lexicalAnalyzer)
        val syntaxTree = parser.parse()
        printTree(syntaxTree.root)
        if (syntaxTree.diagnosticsBag.diagnostics.isNotEmpty()) {
            syntaxTree.diagnosticsBag.forEach {
                println(it.message)
            }
        } else {
            val compiler = Compiler(syntaxTree)
            val evaluationResult = compiler.evaluate(Annotator(), Evaluator())
            if (evaluationResult.diagnostics.diagnostics.isNotEmpty()) {
                evaluationResult.diagnostics.diagnostics.forEach {
                    println(it.message)
                }
                return
            }
            println(evaluationResult.value)
        }

    }


}

private fun printTree(node: SyntaxNode, indent: String = "") {
    print(indent)
    print(node.kind)
    if (node is SyntaxToken) {
        print(" ")
        print(node.actualValue)
    }
    println()
    node.children.forEach { myNode ->
        printTree(myNode, "$indent      ")
    }
}