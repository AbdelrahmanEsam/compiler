package com.moj.najiz.lexicalAnalyzer

import com.moj.najiz.nodes.syntax.SyntaxToken

class LexicalAnalyzer(private val text: String) {

    private val specialTokens = mapOf(
        '+' to SyntaxKind.Token.PlusToken,
        '-' to SyntaxKind.Token.MinusToken,
        '/' to SyntaxKind.Token.SlashToken,
        '*' to SyntaxKind.Token.MultiplyToken,
        '(' to SyntaxKind.Token.OpenBracketToken,
        ')' to SyntaxKind.Token.CloseBracketToken,
    )
    private var position: Int = 0

    private val currentChar: Char?
        get() {
            return if (position >= text.length) null else text[position]
        }

    private val diagnostics : MutableList<String> = mutableListOf()

    private fun next() {
        position++
    }

    fun lex(): SyntaxToken {

        if (position >= text.length) {
            return SyntaxToken(
                SyntaxKind.Token.EndOfFileToken,
                position,
                "\u0000",
                null
            )
        }

        if (currentChar?.isDigit() == true) {
            val start = position
            while (currentChar?.isDigit() == true)
                next()
            val length = position
            val currentToken = text.substring(start, length)
            val actualValue = currentToken.runCatching {
                this.toInt()
            }.getOrNull()
            return SyntaxToken(
                SyntaxKind.Expression.LiteralExpressionSyntax,
                position,
                currentToken,
                actualValue
            )
        }

        if (currentChar?.isWhitespace() == true) {
            val start = position
            while (currentChar?.isWhitespace() == true) next()
            val length = position
            val currentToken = text.substring(start, length)
            return SyntaxToken(
                SyntaxKind.Token.WhiteSpace,
                position,
                currentToken,
                "space"
            )
        }

        if (specialTokens.keys.contains(currentChar)) {
            val currentToken = currentChar
            return SyntaxToken(
                specialTokens[currentToken] ?: SyntaxKind.Token.BadToken,
                position++,
                currentToken.toString(),
                null
            )
        }



        diagnostics.add("error bad character in input : $currentChar")
        return SyntaxToken(
            SyntaxKind.Token.BadToken,
            position++,
            currentChar.toString(),
            null
        )
    }

}