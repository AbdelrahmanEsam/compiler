package com.moj.najiz.nodes

import com.moj.najiz.lexicalAnalyzer.SyntaxKind

sealed class SyntaxNode {
    abstract val kind: SyntaxKind
    abstract val children: List<SyntaxNode>
}

