package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind

sealed class SyntaxNode {
    abstract val kind: SyntaxKind
    abstract val children: List<SyntaxNode>
}

