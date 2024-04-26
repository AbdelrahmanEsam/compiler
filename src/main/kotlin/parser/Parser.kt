package com.moj.najiz.parser

import com.moj.najiz.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.nodes.syntax.*

class Parser(private val lexicalAnalyzer: LexicalAnalyzer) {
    private var position = 0
    private val tokens = mutableListOf<SyntaxToken>()

    private val current: SyntaxToken
        get() = peek(0)

    private val diagnostics: MutableList<String> = mutableListOf()

    private fun getTokens() {

        do {
            val token = lexicalAnalyzer.lex()
            if (token.kind != SyntaxKind.Token.BadToken && token.kind != SyntaxKind.Token.WhiteSpace) tokens.add(token)
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
        val expression = parseExpression()
        val endOfFileToken = match(SyntaxKind.Token.EndOfFileToken)
        return SyntaxTree(diagnostics, expression, endOfFileToken)
    }

    private fun parseExpression(parentPriority: Int = 0): ExpressionSyntax {
        var left : ExpressionSyntax
        var unaryOperatorPriority = current.kind.getUnaryPriority()
        if(unaryOperatorPriority !=  0 && unaryOperatorPriority > parentPriority)
        {

            val operatorToken = nextToken()
            val operand =  parsePrimaryExpression()
            left = UnaryExpressionSyntax(operand,operatorToken )

        }else{
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
        if (current.kind == SyntaxKind.Token.OpenBracketToken) {
            val left = nextToken()
            val expression = parseExpression()
            val right = match(SyntaxKind.Token.CloseBracketToken)
            val parenthesizedExpressionSyntax = ParenthesizedExpressionSyntax(left, expression, right)
            return parenthesizedExpressionSyntax
        }
        val literalToken = match(SyntaxKind.Expression.LiteralExpressionSyntax)
        return LiteralExpressionSyntax(literalToken)
    }

    init {
        getTokens()
    }

}