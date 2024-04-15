package com.moj.najiz.parser

import com.moj.najiz.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.parser.nodes.SyntaxToken
import com.moj.najiz.parser.nodes.BinaryExpressionSyntax
import com.moj.najiz.parser.nodes.ExpressionSyntax
import com.moj.najiz.parser.nodes.NumberExpressionSyntax

class Parser(private val lexicalAnalyzer: LexicalAnalyzer) {
    private var position = 0
    private val tokens = mutableListOf<SyntaxToken>()

    private val current: SyntaxToken
        get() = peek(0)


    private fun getTokens() {

        do {
            val token = lexicalAnalyzer.nextToken()
            if (token.kind != SyntaxKind.BadToken && token.kind != SyntaxKind.WhiteSpace) tokens.add(token)
        } while (token.kind != SyntaxKind.EndOfFileToken)
    }

    private fun peek(offset: Int): SyntaxToken {
        val index = position + offset
        return if (index >= tokens.size) tokens.last() else tokens[index]
    }

    private fun nextToken(): SyntaxToken {
        val current = current
        position++
        return current
    }


    private fun match(kind: SyntaxKind): SyntaxToken {
        return if (current.kind == kind) nextToken() else SyntaxToken(
            kind = kind,
            position = current.position,
            null,
            null
        )
    }

    fun parse(): ExpressionSyntax {
        var left = parsePrimaryExpression()
        while (current.kind == SyntaxKind.PlusToken || current.kind == SyntaxKind.MinusToken) {
            val operator = nextToken()
            val right = parsePrimaryExpression()
            left = BinaryExpressionSyntax(left, operator, right)
        }
        return left
    }

    private fun parsePrimaryExpression(): ExpressionSyntax {
        val numberToken = match(SyntaxKind.Number)
        return NumberExpressionSyntax(numberToken)
    }

    init {
        getTokens()
    }

}