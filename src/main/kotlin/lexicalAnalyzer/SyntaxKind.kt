package com.moj.najiz.lexicalAnalyzer


sealed interface SyntaxKind {

    sealed interface Token : SyntaxKind {
        data object WhiteSpace : Token
        data object PlusToken : Token
        data object MinusToken : Token
        data object MultiplyToken : Token
        data object SlashToken : Token
        data object OpenBracketToken : Token
        data object CloseBracketToken : Token
        data object BadToken : Token
        data object EndOfFileToken : Token

    }

    sealed interface Expression : SyntaxKind {
        data object ParenthesizedExpressionSyntax : Expression
        data object BinaryExpressionSyntax : Expression
        data object UnaryExpressionSyntax : Expression
        data object LiteralExpressionSyntax : Expression
    }

}