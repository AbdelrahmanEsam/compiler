package com.moj.najiz.frontend.parser

import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.syntax.*
import com.moj.najiz.frontend.nodes.syntax.expressions.NumberExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.AssignmentExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.BinaryExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.CompilationUnitSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.ExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.LiteralExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.NameExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.ParenthesizedExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.UnaryExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.BlockStatementSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.ElseClauseSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.ExpressionStatementSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.ForStatementSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.IfStatementSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.WhileStatementSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.StatementSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.VariableDeclarationStatementSyntax

class Parser(private val lexicalAnalyzer: LexicalAnalyzer) {
    private var position = 0
    private val tokens = mutableListOf<SyntaxToken>()

    private val current: SyntaxToken
        get() = peek(0)

    private val diagnostics: Diagnostic.DiagnosticsBag = Diagnostic.DiagnosticsBag()

    val diagnosticsBag: Diagnostic.DiagnosticsBag
        get() = diagnostics

    private fun getTokens() {
        tokens.clear()
        do {
            val token = lexicalAnalyzer.lex()
            if (token.kind != SyntaxKind.Token.BadToken && token.kind != SyntaxKind.Token.WhiteSpaceToken) {
                tokens.add(
                    token
                )
            }
        } while (token.kind != SyntaxKind.Token.EndOfFileToken)
    }

    private fun peek(offset: Int): SyntaxToken {
        val index = position + offset
        return if (index >= tokens.size) tokens.last() else tokens[index]
    }

    private fun nextToken(): SyntaxToken {
        val current = current
        position++
        return current
    }


    private fun match(kind: SyntaxKind): SyntaxToken {
        return if (current.kind == kind) nextToken() else {

            diagnostics.reportUnExpectedError(current.textSpan, "error unexpected token ${current.kind} expected $kind")
            SyntaxToken(
                kind = kind,
                position = current.position,
                null,
                null
            )
        }


    }

    fun parseCompilationUnit(): CompilationUnitSyntax {
        val statement = parseStatement()
        val endOfFileToken = match(SyntaxKind.Token.EndOfFileToken)
        return CompilationUnitSyntax(statement, endOfFileToken)
    }

    private fun parseStatement(): StatementSyntax {
        return when (current.kind) {
            SyntaxKind.Token.BracesToken.OpenBracesToken -> parseBlockStatement()


            SyntaxKind.Token.VariableDeclaration.ValToken, SyntaxKind.Token.VariableDeclaration.VarToken -> parseVariableExpression()


            SyntaxKind.Token.StatementKeywords.IfToken -> parseIfStatement()


            SyntaxKind.Token.StatementKeywords.WhileToken -> parseWhileStatement()
            SyntaxKind.Token.StatementKeywords.ForToken -> parseForStatement()


            else -> parseExpressionStatement()
        }


    }

    private fun parseIfStatement(): IfStatementSyntax {
        match(SyntaxKind.Token.StatementKeywords.IfToken)
        val condition = parseExpression()
        val thenStatement = parseStatement()
        val elseClause = parseElseClause()

        return IfStatementSyntax(condition, thenStatement, elseClause)
    }

    private fun parseWhileStatement(): WhileStatementSyntax {
        match(SyntaxKind.Token.StatementKeywords.WhileToken)
        val condition = parseExpression()
        val thenStatement = parseStatement()
        return WhileStatementSyntax(condition, thenStatement)
    }


    private fun parseForStatement(): ForStatementSyntax {
        match(SyntaxKind.Token.StatementKeywords.ForToken)
        nextToken() // here we need to ignore our parenthesized
        val variableDeclaration = parseExpression()
        nextToken()
        match(SyntaxKind.Token.SemiColon)
        val condition = parseExpression()
        nextToken()
        match(SyntaxKind.Token.SemiColon)
        val trailingStatement = parseStatement()
        nextToken() // here we need to ignore our parenthesized
        val thenStatement = parseStatement()

        return ForStatementSyntax(variableDeclaration, condition, trailingStatement, thenStatement)
    }

    private fun parseElseClause(): ElseClauseSyntax? {

        if (current.kind != SyntaxKind.Token.StatementKeywords.ElseToken) return null

        val keyword = nextToken()
        val statement = parseStatement()

        return ElseClauseSyntax(keyword, statement)

    }


    private fun parseVariableExpression(): VariableDeclarationStatementSyntax {
        val expectedKind =
            if (current.kind == SyntaxKind.Token.VariableDeclaration.ValToken) SyntaxKind.Token.VariableDeclaration.ValToken else SyntaxKind.Token.VariableDeclaration.VarToken
        val modifier = match(expectedKind)
        val identifier = match(SyntaxKind.Token.IdentifierToken)
        val equality = match(SyntaxKind.Token.AssignmentToken)
        val expression = parseExpression()
        return VariableDeclarationStatementSyntax(modifier, identifier, equality, expression)
    }

    private fun parseBlockStatement(): BlockStatementSyntax {
        val statements = mutableListOf<StatementSyntax>()
        val openBracesToken = match(SyntaxKind.Token.BracesToken.OpenBracesToken)
        while (current.kind != SyntaxKind.Token.EndOfFileToken && current.kind != SyntaxKind.Token.BracesToken.CloseBracesToken) {
            statements.add(parseStatement())
        }
        val closeBracesToken = match(SyntaxKind.Token.BracesToken.CloseBracesToken)

        return BlockStatementSyntax(openBracesToken, statements.toTypedArray(), closeBracesToken)
    }

    private fun parseExpressionStatement(): ExpressionStatementSyntax = ExpressionStatementSyntax(parseExpression())

    private fun parseExpression(): ExpressionSyntax {
        if (peek(0).kind == SyntaxKind.Token.IdentifierToken && peek(1).kind == SyntaxKind.Token.AssignmentToken) {
            val identifierToken = nextToken()
            val syntaxToken = nextToken()
            val rightExpression = parseBinaryExpression()
            return AssignmentExpressionSyntax(identifierToken, syntaxToken, rightExpression)
        }

        return parseBinaryExpression()
    }


    private fun parseBinaryExpression(parentPriority: Int = 0): ExpressionSyntax {
        var left: ExpressionSyntax
        val unaryOperatorPriority = current.kind.getUnaryPriority()
        if (unaryOperatorPriority != 0 && unaryOperatorPriority > parentPriority) {

            val operatorToken = nextToken()
            val operand = parsePrimaryExpression()
            left = UnaryExpressionSyntax(operand, operatorToken)

        } else {
            //if your priority is 0 you are in binary
            // expression so your parse will be your left and
            // below we will parse the right part

            left = parsePrimaryExpression()
        }
        while (true) {
            val tokenPriority = current.kind.getBinaryPriority()
            if (tokenPriority == 0 || tokenPriority <= parentPriority) break
            val operatorToken = nextToken()
            val right = parseBinaryExpression(tokenPriority)
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }
        return left
    }


    private fun parsePrimaryExpression(): ExpressionSyntax {
        when (current.kind) {
            SyntaxKind.Token.ParenthesisToken.OpenParenthesisToken -> {
                val left = nextToken()
                val expression = parseExpression()
                val right = match(SyntaxKind.Token.ParenthesisToken.CloseParenthesisToken)
                val parenthesizedExpressionSyntax = ParenthesizedExpressionSyntax(left, expression, right)
                return parenthesizedExpressionSyntax
            }

            SyntaxKind.Token.BooleanToken.TrueToken,
            SyntaxKind.Token.BooleanToken.FalseToken
                -> {
                val value = current.kind == SyntaxKind.Token.BooleanToken.TrueToken
                val keywordToken = nextToken()
                return LiteralExpressionSyntax(keywordToken, value)
            }

            SyntaxKind.Token.IdentifierToken -> {
                val identifierToken = nextToken()
                return NameExpressionSyntax(identifierToken)
            }

            SyntaxKind.Token.NumberToken -> {
                val numberToken = match(SyntaxKind.Token.NumberToken)
                return NumberExpressionSyntax(numberToken)
            }

            else -> {
                val literalToken = match(SyntaxKind.Expression.LiteralExpressionSyntax)
                return LiteralExpressionSyntax(literalToken)
            }
        }
    }

    init {
        getTokens()
    }

}