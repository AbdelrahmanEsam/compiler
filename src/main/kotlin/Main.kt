package com.moj.najiz

import com.moj.najiz.frontend.compilation.Compiler
import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.parser.Parser
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken

fun main() {
    val variables = mutableMapOf<String, Any>()
    while (true) {


        val code = readlnOrNull()?.trim()
        code?.let {
            val lexicalAnalyzer = LexicalAnalyzer()
            lexicalAnalyzer.setCodeInput(code)
            val parser = Parser(lexicalAnalyzer)
            val syntaxTree = parser.parse()
            printTree(syntaxTree.root)
            if (syntaxTree.diagnosticsBag.diagnostics.isNotEmpty()) {
                syntaxTree.diagnosticsBag.forEach {
                    println(it.message)
                }
            } else {
                val compiler = Compiler(syntaxTree)

                val evaluationResult = compiler.compile(Annotator(variables), Evaluator(variables))
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