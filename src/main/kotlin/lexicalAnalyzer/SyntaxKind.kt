package com.moj.najiz.lexicalAnalyzer


sealed interface SyntaxKind {

    sealed interface Token : SyntaxKind {

        data object EqualityToken : Token
        data object NotEqualityToken : Token

        //mathematical
        sealed interface MathOperatorsToken : SyntaxKind {
            data object PlusToken : MathOperatorsToken
            data object MinusToken : MathOperatorsToken
            data object MultiplyToken :MathOperatorsToken
            data object SlashToken : MathOperatorsToken
        }

        //booleans
        sealed interface BooleanToken : SyntaxKind {
            data object TrueToken : BooleanToken
            data object FalseToken : BooleanToken
        }

        sealed interface BooleanOperatorToken : SyntaxKind {
            data object AndToken : BooleanOperatorToken
            data object OrToken : BooleanOperatorToken
            data object NotToken : BooleanOperatorToken
        }


        //brackets
        data object OpenBracketToken : Token
        data object CloseBracketToken : Token

        //special
        data object WhiteSpaceToken : Token
        data object BadToken : Token
        data object IdentifierToken : Token
        data object EndOfFileToken : Token

    }

    sealed interface Expression : SyntaxKind {
        data object ParenthesizedExpressionSyntax : Expression
        data object BinaryExpressionSyntax : Expression
        data object UnaryExpressionSyntax : Expression
        data object LiteralExpressionSyntax : Expression
    }

}