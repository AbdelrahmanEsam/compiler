package com.moj.najiz.frontend.parser

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind.Token.*

internal  fun SyntaxKind.getBinaryPriority(): Int {
    return when (this) {
        Math.OperatorsTokens.MultiplyToken, Math.OperatorsTokens.SlashToken -> 6
        Math.OperatorsTokens.PlusToken, Math.OperatorsTokens.MinusToken -> 5
        is Math.EqualityTokens -> 3
        BooleanOperatorToken.AndToken -> 2
        BooleanOperatorToken.OrToken -> 1
        else -> 0
    }
}

internal fun SyntaxKind.getUnaryPriority(): Int {
    return when (this) {
        Math.OperatorsTokens.PlusToken, Math.OperatorsTokens.MinusToken -> 6
        BooleanOperatorToken.NotToken -> 6
        else -> 0
    }
}