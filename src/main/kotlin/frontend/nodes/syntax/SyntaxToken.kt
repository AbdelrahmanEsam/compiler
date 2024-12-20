package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.diagnostics.TextSpan
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind

data class SyntaxToken(
    override val kind: SyntaxKind,
    val position: Int,
    val text: String?,
    val actualValue: Any?,
    override val children: List<SyntaxNode> = emptyList(),
    val textSpan: TextSpan = TextSpan(position, text?.length ?: 0)
) : SyntaxNode()