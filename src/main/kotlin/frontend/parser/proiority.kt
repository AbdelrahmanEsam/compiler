package com.moj.najiz.frontend.parser

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind.Token.*

internal  fun SyntaxKind.getBinaryPriority(): Int {
    return when (this) {
        MathOperatorsToken.MultiplyToken, MathOperatorsToken.SlashToken -> 6
        MathOperatorsToken.PlusToken, MathOperatorsToken.MinusToken -> 5
        EqualityToken, NotEqualityToken -> 3
        BooleanOperatorToken.AndToken -> 2
        BooleanOperatorToken.OrToken -> 1
        else -> 0
    }
}

internal fun SyntaxKind.getUnaryPriority(): Int {
    return when (this) {
        MathOperatorsToken.PlusToken, MathOperatorsToken.MinusToken -> 6
        BooleanOperatorToken.NotToken -> 6
        else -> 0
    }
}