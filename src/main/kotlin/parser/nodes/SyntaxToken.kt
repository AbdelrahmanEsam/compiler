package com.moj.najiz.parser.nodes

import com.moj.najiz.lexicalAnalyzer.SyntaxKind

data class SyntaxToken(
    override val kind: SyntaxKind,
    val position: Int,
    val text: String?,
    val actualValue: Any?,
    override val children: List<SyntaxNode> = emptyList(),
) : SyntaxNode()