package com.moj.najiz.frontend.lexicalAnalyzer

import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.diagnostics.TextSpan
import com.moj.najiz.frontend.nodes.syntax.SyntaxToken
import com.moj.najiz.frontend.nodes.syntax.getKeywordKind
import com.moj.najiz.frontend.nodes.syntax.toBoolean
import kotlin.reflect.typeOf

class LexicalAnalyzer {

    private var code: String = ""

    fun setCodeInput(code: String) {
        this.code = code
    }


    private var position: Int = 0
    private  var syntaxKind : SyntaxKind? = null
    private  var syntaxPosition : Int?  = null
    private  var syntaxValue  : String?  = null

    private val currentChar: Char?
        get() {
            return if (position >= code.length) null else code[position]
        }

    private val lookAhead: Char?
        get() {
            return if (position + 1 >= code.length) null else code[position + 1]
        }

    private val diagnostics: Diagnostic.DiagnosticsBag = Diagnostic.DiagnosticsBag()

    private fun next() {
        position++
    }

    fun lex(): SyntaxToken {


        when (currentChar) {
            '+' -> {
                syntaxKind =   SyntaxKind.Token.MathOperatorsToken.PlusToken
                this.syntaxPosition = position++
                this.syntaxValue = "+"
            }



            '-' -> {
                syntaxKind =   SyntaxKind.Token.MathOperatorsToken.MinusToken
                this.syntaxPosition = position++
                this.syntaxValue = "-"
            }



            '*' -> {
                syntaxKind =   SyntaxKind.Token.MathOperatorsToken.MultiplyToken
                this.syntaxPosition = position++
                this.syntaxValue = "*"
            }



            '/' -> {
                syntaxKind =   SyntaxKind.Token.MathOperatorsToken.SlashToken
                this.syntaxPosition = position++
                this.syntaxValue = "/"
            }


            '(' -> {
                syntaxKind =    SyntaxKind.Token.ParenthesisToken.OpenParenthesisToken
                this.syntaxPosition = position++
                this.syntaxValue = "("
            }


            ')' -> {
                syntaxKind = SyntaxKind.Token.ParenthesisToken.CloseParenthesisToken
                this.syntaxPosition = position++
                this.syntaxValue = ")"
            }

            '{' -> {
                syntaxKind =    SyntaxKind.Token.BracesToken.OpenBracesToken
                this.syntaxPosition = position++
                this.syntaxValue = "{"
            }


            '}' -> {
                syntaxKind = SyntaxKind.Token.BracesToken.CloseBracesToken
                this.syntaxPosition = position++
                this.syntaxValue = "}"
            }

            '!'  if(lookAhead == '=') -> {

                syntaxKind =     SyntaxKind.Token.NotEqualityToken
                this.syntaxPosition = position.also {
                    position += 2
                }
                this.syntaxValue = "\"!="

            }

            '!' if(lookAhead?.isWhitespace() == true || lookAhead == null || (lookAhead?.isLetter() ==true) ) -> {
                syntaxKind =   SyntaxKind.Token.BooleanOperatorToken.NotToken
                this.syntaxPosition = position++
                this.syntaxValue = "!"
            }


            '|' -> {
                syntaxKind =   SyntaxKind.Token.BooleanOperatorToken.OrToken
                this.syntaxPosition = position.also {
                    position += 2
                }
                this.syntaxValue = "||"
            }

            '&' ->  {
                syntaxKind =    SyntaxKind.Token.BooleanOperatorToken.AndToken
                this.syntaxPosition = position.also {
                    position += 2
                }
                this.syntaxValue = "&&"
            }


            '=' if (lookAhead == '=') -> {
                syntaxKind =   SyntaxKind.Token.EqualityToken
                this.syntaxPosition = position.also {
                    position += 2
                }
                this.syntaxValue = "=="
            }




            '=' -> {
                syntaxKind =   SyntaxKind.Token.AssignmentToken
                this.syntaxPosition = position++
                this.syntaxValue = "="
            }


            else -> {
                val spaceToken = spaceToken()
                spaceToken?.let {
                    return spaceToken
                }

                val isDigit = isDigit()
                isDigit?.let {
                    return isDigit
                }


                val isWeightSpace = isWhiteSpace()
                isWeightSpace?.let {
                    return isWeightSpace
                }

                val isLetter = isLetter()
                isLetter?.let {
                    return isLetter
                }

                diagnostics.reportBadCharacter(position = position, char = "currentChar")
                syntaxKind =   SyntaxKind.Token.BadToken
                this.syntaxPosition = position++
                this.syntaxValue = currentChar.toString()
            }


        }
        return  SyntaxToken(
            syntaxKind ?: SyntaxKind.Token.BadToken,
            syntaxPosition ?: position,
            syntaxValue,
            syntaxValue
        )

    }


    private fun spaceToken(): SyntaxToken? {
        return if (position >= code.length) {
            return SyntaxToken(
                SyntaxKind.Token.EndOfFileToken,
                position,
                "\u0000",
                "\u0000"
            )
        } else null
    }

    private fun isDigit(): SyntaxToken? {
        return if (currentChar?.isDigit() == true || (currentChar == '.' && code.getOrNull(position + 1)?.isDigit() == true)) {
            val start = position
            while (currentChar?.isDigit() == true || currentChar == '.')
                next()
            val length = position
            val currentToken = code.substring(start, length)
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
                        text = code,
                        type = typeOf<Double>()
                    )
                }
                diagnostics.reportInvalidInt(
                    textSpan = TextSpan(_start = start, _length = length),
                    text = code,
                    type = typeOf<Int>()
                )
            }
            SyntaxToken(
                SyntaxKind.Token.NumberToken,
                position,
                currentToken,
                actualValue
            )
        } else null
    }

    private fun isWhiteSpace(): SyntaxToken? {
        return if (currentChar?.isWhitespace() == true) {
            val start = position
            while (currentChar?.isWhitespace() == true) next()
            val length = position
            val currentToken = code.substring(start, length)
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
            val currentToken = code.substring(start, length)
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


}