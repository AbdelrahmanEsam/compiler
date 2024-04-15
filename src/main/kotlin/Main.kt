package com.moj.najiz

import com.moj.najiz.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.parser.Parser
import com.moj.najiz.parser.nodes.SyntaxNode
import com.moj.najiz.parser.nodes.SyntaxToken

fun main() {

    val code = readlnOrNull()?.trim()
    code?.let {
        val lexicalAnalyzer = LexicalAnalyzer(code)
        val parser = Parser(lexicalAnalyzer)
        val expression = parser.parse()
        printTree(expression)


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