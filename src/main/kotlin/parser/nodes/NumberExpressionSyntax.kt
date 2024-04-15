package com.moj.najiz.parser.nodes

import com.moj.najiz.lexicalAnalyzer.SyntaxKind

class NumberExpressionSyntax(number: SyntaxToken) : ExpressionSyntax() {

    override val kind: SyntaxKind
        get() = SyntaxKind.Number

    override val children: List<SyntaxNode> = listOf(number)

}