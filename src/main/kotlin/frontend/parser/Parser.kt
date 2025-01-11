package com.moj.najiz.frontend.parser

import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.syntax.*
import com.moj.najiz.frontend.nodes.syntax.frontend.nodes.syntax.NumberExpressionSyntax

class Parser(private val lexicalAnalyzer: LexicalAnalyzer) {
    private var position = 0
    private val tokens = mutableListOf<SyntaxToken>()

    private val current: SyntaxToken
        get() = peek(0)

    private val diagnostics: Diagnostic.DiagnosticsBag = Diagnostic.DiagnosticsBag()

    private fun getTokens() {
        do {
            val token = lexicalAnalyzer.lex()
            if (token.kind != SyntaxKind.Token.BadToken && token.kind != SyntaxKind.Token.WhiteSpaceToken) {
                tokens.add(
                    token
                )
            }
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

    private fun parseExpression() : ExpressionSyntax
    {
        if(peek(0).kind == SyntaxKind.Token.IdentifierToken && peek(1).kind == SyntaxKind.Token.AssignmentToken){
        val  identifierToken = nextToken()
        val syntaxToken =     nextToken()
        val rightExpression = parseBinaryExpression()
        return  AssignmentExpressionSyntax(identifierToken,syntaxToken,rightExpression)
        }

        return  parseBinaryExpression()
    }



    private fun parseBinaryExpression(parentPriority: Int = 0): ExpressionSyntax {
        var left: ExpressionSyntax
        val unaryOperatorPriority = current.kind.getUnaryPriority()
        if (unaryOperatorPriority != 0 && unaryOperatorPriority > parentPriority) {

            val operatorToken = nextToken()
            val operand = parsePrimaryExpression()
            left = UnaryExpressionSyntax(operand, operatorToken)

        } else {
            //if your priority is 0 you are in binary
            // expression so your parse will be your left and
            // below we will parse the right part

            left = parsePrimaryExpression()
        }
        while (true) {
            val tokenPriority = current.kind.getBinaryPriority()
            if (tokenPriority == 0 || tokenPriority <= parentPriority) break
            val operatorToken = nextToken()
            val right = parseBinaryExpression(tokenPriority)
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }
        return left
    }


    private fun parsePrimaryExpression(): ExpressionSyntax {
        when (current.kind) {
            SyntaxKind.Token.BracketsToken.OpenBracketToken -> {
                val left = nextToken()
                val expression = parseBinaryExpression()
                val right = match(SyntaxKind.Token.BracketsToken.CloseBracketToken)
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

            SyntaxKind.Token.IdentifierToken -> {
                val identifierToken = nextToken()
                return  NameExpressionSyntax(identifierToken)
            }

            SyntaxKind.Token.NumberToken -> {
                val numberToken = match(SyntaxKind.Token.NumberToken)
                return  NumberExpressionSyntax(numberToken)
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