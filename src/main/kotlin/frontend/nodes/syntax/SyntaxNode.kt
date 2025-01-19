package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind

abstract class SyntaxNode {
    abstract val kind: SyntaxKind
    open val children: List<SyntaxNode>  = listOf()
}

