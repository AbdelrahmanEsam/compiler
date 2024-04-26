package com.moj.najiz.nodes.syntax

import com.moj.najiz.lexicalAnalyzer.SyntaxKind

sealed class SyntaxNode {
    abstract val kind: SyntaxKind
    abstract val children: List<SyntaxNode>
}

