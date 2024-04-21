package com.moj.najiz.parser

import com.moj.najiz.lexicalAnalyzer.SyntaxKind


 fun SyntaxKind.getBinaryPriority(): Int {
    return when (this) {
        SyntaxKind.Token.PlusToken, SyntaxKind.Token.MinusToken -> 1
        SyntaxKind.Token.MultiplyToken, SyntaxKind.Token.SlashToken -> 2
        else -> 0
    }
}

fun SyntaxKind.getUnaryPriority(): Int {
    return when (this) {
        SyntaxKind.Token.PlusToken, SyntaxKind.Token.MinusToken -> 3
        else -> 0
    }
}