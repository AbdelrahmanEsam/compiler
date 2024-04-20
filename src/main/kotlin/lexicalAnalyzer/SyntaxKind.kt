package com.moj.najiz.lexicalAnalyzer

enum class SyntaxKind {
    Number, WhiteSpace, PlusToken, MinusToken, MultiplyToken,
    SlashToken, OpenBracketToken, CloseBracketToken, BadToken, EndOfFileToken, BinaryExpressionSyntax,
    ParenthesizedExpressionSyntax
}