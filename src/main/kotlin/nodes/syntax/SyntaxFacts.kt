package com.moj.najiz.nodes.syntax

import com.moj.najiz.lexicalAnalyzer.SyntaxKind


fun String.getKeywordKind(): SyntaxKind {
    return when (this) {
        "true" -> SyntaxKind.Token.BooleanToken.TrueToken
        "false" -> SyntaxKind.Token.BooleanToken.FalseToken

        else -> {
            SyntaxKind.Token.IdentifierToken
        }
    }
}