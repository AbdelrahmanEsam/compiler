package com.moj.najiz.lexicalAnalyzer

import com.moj.najiz.diagnostics.Diagnostic
import com.moj.najiz.diagnostics.TextSpan
import com.moj.najiz.nodes.syntax.SyntaxToken
import com.moj.najiz.nodes.syntax.getKeywordKind
import com.moj.najiz.nodes.syntax.toBoolean
import kotlin.reflect.typeOf

class LexicalAnalyzer(private val text: String) {


    private var position: Int = 0

    private val currentChar: Char?
        get() {
            return if (position >= text.length) null else text[position]
        }

    private val diagnostics: Diagnostic.DiagnosticsBag = Diagnostic.DiagnosticsBag()

    private fun next() {
        position++
    }

    fun lex(): SyntaxToken {

        val spaceToken = spaceToken()
        spaceToken?.let {
            return spaceToken
        }

        val isDigit = isDigit()
        isDigit?.let {
            return isDigit
        }


        val isWeightSpace = isWeightSpace()
        isWeightSpace?.let {
            return isWeightSpace
        }

        val isLetter = isLetter()
        isLetter?.let {
            return isLetter
        }

        return isSpecialChar()

    }


    private fun spaceToken(): SyntaxToken? {
        return if (position >= text.length) {
            return SyntaxToken(
                SyntaxKind.Token.EndOfFileToken,
                position,
                "\u0000",
                "\u0000"
            )
        } else null
    }

    private fun isDigit(): SyntaxToken? {
        return if (currentChar?.isDigit() == true) {
            val start = position
            while (currentChar?.isDigit() == true || currentChar == '.')
                next()
            val length = position
            val currentToken = text.substring(start, length)
            val actualValue = currentToken.runCatching {
                if (currentToken.contains(".")) {
                    return@runCatching this.toDouble()
                }
                this.toInt()
            }.getOrNull()
            if (actualValue == null) {
                if (currentToken.contains(".")) {
                    diagnostics.reportInvalidDouble(
                        textSpan = TextSpan(_start = start, _length = length),
                        text = text,
                        type = typeOf<Double>()
                    )
                }
                diagnostics.reportInvalidInt(
                    textSpan = TextSpan(_start = start, _length = length),
                    text = text,
                    type = typeOf<Int>()
                )
            }
            SyntaxToken(
                SyntaxKind.Expression.LiteralExpressionSyntax,
                position,
                currentToken,
                actualValue
            )
        } else null
    }

    private fun isWeightSpace(): SyntaxToken? {
        return if (currentChar?.isWhitespace() == true) {
            val start = position
            while (currentChar?.isWhitespace() == true) next()
            val length = position
            val currentToken = text.substring(start, length)
            SyntaxToken(
                SyntaxKind.Token.WhiteSpaceToken,
                position,
                currentToken,
                "space"
            )
        } else null
    }

    private fun isLetter(): SyntaxToken? {
        return if (currentChar?.isLetter() == true) {
            val start = position
            while (currentChar?.isLetter() == true) next()
            val length = position
            val currentToken = text.substring(start, length)
            val kind = currentToken.getKeywordKind()
            val actualValue = if (kind != SyntaxKind.Token.IdentifierToken) {
                currentToken.toBoolean()
            } else null
            SyntaxToken(
                kind,
                position,
                currentToken,
                actualValue ?: currentToken
            )
        } else null
    }

    private fun isSpecialChar(): SyntaxToken {
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
                SyntaxKind.Token.BracketsToken.OpenBracketToken,
                position++,
                "(",
                "("
            )


            ')' -> SyntaxToken(
                SyntaxKind.Token.BracketsToken.CloseBracketToken,
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
                diagnostics.reportBadCharacter(position = position, char = "currentChar")
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