package com.moj.najiz

import com.moj.najiz.expressionEvaluator.Evaluator
import com.moj.najiz.lexicalAnalyzer.LexicalAnalyzer
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
        if (syntaxTree.diagnostics.isNotEmpty()) {
            syntaxTree.diagnostics.forEach {
                println(it)
            }
        }else{
            val evaluator = Evaluator(syntaxTree.root)
            val evaluationResult = evaluator.getEvaluator()
            println(evaluationResult)
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