package frontend.lexicalAnalyzer

import com.moj.najiz.frontend.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class LexicalAnalyzerTest {

    private var lexicalAnalyzer : LexicalAnalyzer = LexicalAnalyzer()


    @Test
    fun inputWhiteSpaceShouldReturnWhiteSpaceToken()
    {
        lexicalAnalyzer.setCodeInput(" ")
        return assertEquals(SyntaxKind.Token.WhiteSpaceToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputBackSlashWithNLetterShouldReturnWhiteSpaceToken()
    {
        lexicalAnalyzer.setCodeInput("\n")
        return assertEquals(SyntaxKind.Token.WhiteSpaceToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputBackSlashWithRLetterShouldReturnWhiteSpaceToken()
    {
        lexicalAnalyzer.setCodeInput("\r")
        return assertEquals(SyntaxKind.Token.WhiteSpaceToken,lexicalAnalyzer.lex().kind)
    }


    @Test
    fun inputIsThreeShouldReturnNumberToken()
    {
        lexicalAnalyzer.setCodeInput("3")
        return assertEquals(SyntaxKind.Token.NumberToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsTrueLowerCaseShouldReturnBooleanTokenTrueToken()
    {
        lexicalAnalyzer.setCodeInput("true")
        return assertEquals(SyntaxKind.Token.BooleanToken.TrueToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsTrueUpperCaseShouldReturnBooleanTokenTrueToken()
    {
        lexicalAnalyzer.setCodeInput("True")
        return assertEquals(SyntaxKind.Token.BooleanToken.TrueToken,lexicalAnalyzer.lex().kind)
    }


    @Test
    fun inputIsFalseUpperCaseShouldReturnBooleanTokenFalseToken()
    {
        lexicalAnalyzer.setCodeInput("False")
        return assertEquals(SyntaxKind.Token.BooleanToken.FalseToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsFalseLowerCaseShouldReturnBooleanTokenFalseToken()
    {
        lexicalAnalyzer.setCodeInput("false")
        return assertEquals(SyntaxKind.Token.BooleanToken.FalseToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsPlusLowerCaseShouldReturnPlusToken()
    {
        lexicalAnalyzer.setCodeInput("+")
        return assertEquals(SyntaxKind.Token.MathOperatorsToken.PlusToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsMinusLowerCaseShouldReturnMinusToken()
    {
        lexicalAnalyzer.setCodeInput("-")
        return assertEquals(SyntaxKind.Token.MathOperatorsToken.MinusToken,lexicalAnalyzer.lex().kind)
    }


    @Test
    fun inputIsMultiplyLowerCaseShouldReturnMultiplyToken()
    {
        lexicalAnalyzer.setCodeInput("*")
        return assertEquals(SyntaxKind.Token.MathOperatorsToken.MultiplyToken,lexicalAnalyzer.lex().kind)
    }


    @Test
    fun inputIsDivideLowerCaseShouldReturnSlashToken()
    {
        lexicalAnalyzer.setCodeInput("/")
        return assertEquals(SyntaxKind.Token.MathOperatorsToken.SlashToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsOpenBracketLowerCaseShouldReturnOpenBracketToken()
    {
        lexicalAnalyzer.setCodeInput("(")
        return assertEquals(SyntaxKind.Token.ParenthesisToken.OpenParenthesisToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsCloseBracketLowerCaseShouldReturnCloseBracketToken()
    {
        lexicalAnalyzer.setCodeInput(")")
        return assertEquals(SyntaxKind.Token.ParenthesisToken.CloseParenthesisToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsNotSymbolLowerCaseShouldReturnNotToken()
    {
        lexicalAnalyzer.setCodeInput("!")
        return assertEquals(SyntaxKind.Token.BooleanOperatorToken.NotToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsNotEqualSymbolLowerCaseShouldReturnNotEqualToken()
    {
        lexicalAnalyzer.setCodeInput("!=")
        return assertEquals(SyntaxKind.Token.NotEqualityToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsOrSymbolLowerCaseShouldReturnOrToken()
    {
        lexicalAnalyzer.setCodeInput("||")
        return assertEquals(SyntaxKind.Token.BooleanOperatorToken.OrToken,lexicalAnalyzer.lex().kind)
    }


    @Test
    fun inputIsAndSymbolLowerCaseShouldReturnAndToken()
    {
        lexicalAnalyzer.setCodeInput("&&")
        return assertEquals(SyntaxKind.Token.BooleanOperatorToken.AndToken,lexicalAnalyzer.lex().kind)
    }


    @Test
    fun inputIsEqualSymbolLowerCaseShouldReturnAssignmentToken()
    {
        lexicalAnalyzer.setCodeInput("=")
        return assertEquals(SyntaxKind.Token.AssignmentToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsEqualitySymbolLowerCaseShouldReturnEqualityToken()
    {
        lexicalAnalyzer.setCodeInput("==")
        return assertEquals(SyntaxKind.Token.EqualityToken,lexicalAnalyzer.lex().kind)
    }

    @Test
    fun inputIsUnKnownSymbolLowerCaseShouldReturnBadToken()
    {
        lexicalAnalyzer.setCodeInput("!%!")
        return assertEquals(SyntaxKind.Token.BadToken,lexicalAnalyzer.lex().kind)
    }



    @AfterEach
    fun afterEach()
    {
        lexicalAnalyzer.setCodeInput("")
    }

}