package com.moj.najiz.frontend.lexicalAnalyzer


sealed interface SyntaxKind {

    sealed interface Token : SyntaxKind {

        data object NumberToken : Token
        data object EqualityToken : Token
        data object AssignmentToken : Token
        data object NotEqualityToken : Token

        //mathematical
        sealed interface MathOperatorsToken : SyntaxKind {
            data object PlusToken : MathOperatorsToken
            data object MinusToken : MathOperatorsToken
            data object MultiplyToken : MathOperatorsToken
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

        sealed interface ParenthesisToken : SyntaxKind {
            //parenthesis
            data object OpenParenthesisToken : ParenthesisToken
            data object CloseParenthesisToken : ParenthesisToken
        }

        sealed interface BracesToken : SyntaxKind
        {
            data object  OpenBracesToken : BracesToken
            data object  CloseBracesToken : BracesToken
        }


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
        data object NumberExpressionSyntax : Expression
        data object NameExpressionSyntax : Expression
        data object AssignmentExpressionSyntax : Expression
    }

    sealed interface Statement : SyntaxKind
    {
        data object BlockStatementStatement : Statement
        data object ExpressionStatementStatement : Statement
    }

    data object  CompilationUnit : SyntaxKind

}