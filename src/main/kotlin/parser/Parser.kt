package com.moj.najiz.parser

import com.moj.najiz.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.nodes.*

class Parser(private val lexicalAnalyzer: LexicalAnalyzer) {
    private var position = 0
    private val tokens = mutableListOf<SyntaxToken>()

    private val current: SyntaxToken
        get() = peek(0)

    private val diagnostics: MutableList<String> = mutableListOf()

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
        return if (current.kind == kind) nextToken() else {

            diagnostics.add("error unexpected token ${current.kind} expected $kind")
            SyntaxToken(
                kind = kind,
                position = current.position,
                null,
                null
            )
        }


    }

    fun parse(): SyntaxTree {
        val expression = parseTerm()
        val endOfFileToken = match(SyntaxKind.EndOfFileToken)
        return SyntaxTree(diagnostics, expression, endOfFileToken)
    }


    private fun parseTerm(): ExpressionSyntax {
        var left = parseMultiplicativeExpression()
        while (current.kind == SyntaxKind.PlusToken || current.kind == SyntaxKind.MinusToken) {
            val operator = nextToken()
            val right = parseMultiplicativeExpression()
            left = BinaryExpressionSyntax(left, operator, right)

        }
        return left
    }

    private fun parseMultiplicativeExpression(): ExpressionSyntax {
        var left = parsePrimaryExpression()
        while (current.kind == SyntaxKind.MultiplyToken || current.kind == SyntaxKind.SlashToken
        ) {
            val operator = nextToken()
            val right = parsePrimaryExpression()
            left = BinaryExpressionSyntax(left, operator, right)
        }
        return left
    }

    private fun parsePrimaryExpression(): ExpressionSyntax {
        if (current.kind == SyntaxKind.OpenBracketToken) {
            val left = nextToken()
            val expression = parseTerm()
            val right = match(SyntaxKind.CloseBracketToken)
            val parenthesizedExpressionSyntax = ParenthesizedExpressionSyntax(left, expression, right)
            return parenthesizedExpressionSyntax
        }
        val numberToken = match(SyntaxKind.Number)
        return NumberExpressionSyntax(numberToken)
    }

    init {
        getTokens()
    }

}