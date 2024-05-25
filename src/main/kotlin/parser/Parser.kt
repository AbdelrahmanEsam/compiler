package com.moj.najiz.parser

import com.moj.najiz.diagnostics.Diagnostic
import com.moj.najiz.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.nodes.syntax.*

class Parser(private val lexicalAnalyzer: LexicalAnalyzer) {
    private var position = 0
    private val tokens = mutableListOf<SyntaxToken>()

    private val current: SyntaxToken
        get() = peek(0)

    private val diagnostics: Diagnostic.DiagnosticsBag = Diagnostic.DiagnosticsBag()

    private fun getTokens() {

        do {
            val token = lexicalAnalyzer.lex()
            if (token.kind != SyntaxKind.Token.BadToken && token.kind != SyntaxKind.Token.WhiteSpaceToken) tokens.add(
                token
            )
        } while (token.kind != SyntaxKind.Token.EndOfFileToken)
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

            diagnostics.reportUnExpectedError(current.textSpan, "error unexpected token ${current.kind} expected $kind")
            SyntaxToken(
                kind = kind,
                position = current.position,
                null,
                null
            )
        }


    }

    fun parse(): SyntaxTree {
        val expression = parseExpression()
        val endOfFileToken = match(SyntaxKind.Token.EndOfFileToken)
        return SyntaxTree(diagnostics, expression, endOfFileToken)
    }

    private fun parseExpression(parentPriority: Int = 0): ExpressionSyntax {
        var left: ExpressionSyntax
        val unaryOperatorPriority = current.kind.getUnaryPriority()
        if (unaryOperatorPriority != 0 && unaryOperatorPriority > parentPriority) {

            val operatorToken = nextToken()
            val operand = parsePrimaryExpression()
            left = UnaryExpressionSyntax(operand, operatorToken)

        } else {
            left = parsePrimaryExpression()
        }
        while (true) {
            val tokenPriority = current.kind.getBinaryPriority()
            if (tokenPriority == 0 || tokenPriority <= parentPriority) break
            val operatorToken = nextToken()
            val right = parseExpression(tokenPriority)
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }
        return left
    }


    private fun parsePrimaryExpression(): ExpressionSyntax {
        when (current.kind) {
            SyntaxKind.Token.OpenBracketToken -> {
                val left = nextToken()
                val expression = parseExpression()
                val right = match(SyntaxKind.Token.CloseBracketToken)
                val parenthesizedExpressionSyntax = ParenthesizedExpressionSyntax(left, expression, right)
                return parenthesizedExpressionSyntax
            }

            SyntaxKind.Token.BooleanToken.TrueToken,
            SyntaxKind.Token.BooleanToken.FalseToken
            -> {
                val value = current.kind == SyntaxKind.Token.BooleanToken.TrueToken
                val keywordToken = nextToken()
                return LiteralExpressionSyntax(keywordToken, value)
            }

            else -> {
                val literalToken = match(SyntaxKind.Expression.LiteralExpressionSyntax)
                return LiteralExpressionSyntax(literalToken)
            }
        }
    }

    init {
        getTokens()
    }

}