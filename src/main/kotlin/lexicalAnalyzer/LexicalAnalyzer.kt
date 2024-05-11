package com.moj.najiz.lexicalAnalyzer

import com.moj.najiz.nodes.syntax.SyntaxToken
import com.moj.najiz.nodes.syntax.getKeywordKind

class LexicalAnalyzer(private val text: String) {


    private var position: Int = 0

    private val currentChar: Char?
        get() {
            return if (position >= text.length) null else text[position]
        }

    private val diagnostics: MutableList<String> = mutableListOf()

    private fun next() {
        position++
    }

    fun lex(): SyntaxToken {

        if (position >= text.length) {
            return SyntaxToken(
                SyntaxKind.Token.EndOfFileToken,
                position,
                "\u0000",
                "\u0000"
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
                SyntaxKind.Token.WhiteSpaceToken,
                position,
                currentToken,
                "space"
            )
        }

        if (currentChar?.isLetter() == true) {
            val start = position
            while (currentChar?.isLetter() == true) next()
            val length = position
            val currentToken = text.substring(start, length)
            val kind = currentToken.getKeywordKind()
            val actualValue = kind.runCatching {
                this as Boolean
            }.getOrNull()
            return SyntaxToken(
                kind,
                position,
                currentToken,
                actualValue ?: currentToken
            )
        }

        return when (currentChar) {
            '+' -> SyntaxToken(
                SyntaxKind.Token.MathOperatorsToken.PlusToken,
                position++,
                "+",
                "+"
            )

            '-' -> SyntaxToken(
                SyntaxKind.Token.MathOperatorsToken.MinusToken,
                position++,
                "-",
                "-"
            )


            '*' -> SyntaxToken(
                SyntaxKind.Token.MathOperatorsToken.MultiplyToken,
                position++,
                "*",
                "*"
            )

            '/' -> SyntaxToken(
                SyntaxKind.Token.MathOperatorsToken.SlashToken,
                position++,
                "/",
                "/"
            )

            '(' -> SyntaxToken(
                SyntaxKind.Token.OpenBracketToken,
                position++,
                "(",
                "("
            )


            ')' -> SyntaxToken(
                SyntaxKind.Token.CloseBracketToken,
                position++,
                ")",
                ")"
            )

            '!' -> return if (text[position + 1] == '=')
                SyntaxToken(
                    SyntaxKind.Token.NotEqualityToken,
                    position.also {
                        position += 2
                    },
                    "!=",
                    "!="
                )
            else SyntaxToken(
                SyntaxKind.Token.BooleanOperatorToken.NotToken,
                position++,
                "!",
                "!"
            )


            '|' -> {
                SyntaxToken(
                    SyntaxKind.Token.BooleanOperatorToken.OrToken,
                    position.also {
                        position += 2
                    },
                    "!!",
                    "!!"
                )

            }

            '&' -> SyntaxToken(
                SyntaxKind.Token.BooleanOperatorToken.AndToken,
                position.also {
                    position += 2
                },
                "&&",
                "&&"
            )


            '=' -> SyntaxToken(
                SyntaxKind.Token.EqualityToken,
                position.also {
                    position += 2
                },
                "==",
                "=="
            )


            else -> {
                diagnostics.add("error bad character in input : $currentChar")
                SyntaxToken(
                    SyntaxKind.Token.BadToken,
                    position++,
                    currentChar.toString(),
                    currentChar
                )
            }
        }


    }

}