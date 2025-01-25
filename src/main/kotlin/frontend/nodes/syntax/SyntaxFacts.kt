package com.moj.najiz.frontend.nodes.syntax

import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind


fun String.getKeywordKind(): SyntaxKind {
    return when (this.lowercase()) {
        "true" -> SyntaxKind.Token.BooleanToken.TrueToken
        "false" -> SyntaxKind.Token.BooleanToken.FalseToken
        "val" -> SyntaxKind.Token.VariableDeclaration.ValToken
        "var" -> SyntaxKind.Token.VariableDeclaration.VarToken

        else -> {
            SyntaxKind.Token.IdentifierToken
        }
    }
}

fun String.toBoolean() = equals("true", ignoreCase = true)