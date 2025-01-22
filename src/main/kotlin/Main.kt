package com.moj.najiz

import com.moj.najiz.frontend.compilation.Compiler
import com.moj.najiz.frontend.nodes.syntax.SyntaxNode
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken
import com.moj.najiz.frontend.nodes.syntax.SyntaxTree

fun main() {
    var previousCompiler  : Compiler? = null
    while (true) {


        val code = readlnOrNull()?.trim()
        if(code == "#reset"){
            previousCompiler = null
            continue
        }
        code?.let {
            val syntaxTree = SyntaxTree.parse(it)
            printTree(syntaxTree.root)
            if (syntaxTree.diagnosticsBag.diagnostics.isNotEmpty()) {
                syntaxTree.diagnosticsBag.forEach {
                    println(it.message)
                }
            } else {
                previousCompiler = previousCompiler?.continueWith(syntaxTree) ?: Compiler().apply { setSyntaxTree(syntaxTree) }
                val evaluationResult = previousCompiler.compile()
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