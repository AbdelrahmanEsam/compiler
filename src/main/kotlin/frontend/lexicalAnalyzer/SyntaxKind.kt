package com.moj.najiz.frontend.lexicalAnalyzer


sealed interface SyntaxKind {

    sealed interface Token : SyntaxKind {

        data object NumberToken : Token
        data object AssignmentToken : Token

        //mathematical
        sealed interface Math : SyntaxKind{


            sealed interface OperatorsTokens : Math {
                data object PlusToken : OperatorsTokens
                data object MinusToken : OperatorsTokens
                data object MultiplyToken : OperatorsTokens
                data object SlashToken : OperatorsTokens
            }

            sealed interface EqualityTokens : Math {
                data object EqualityToken : EqualityTokens
                data object NotEqualityToken : EqualityTokens
                data object GreaterThanToken : EqualityTokens
                data object GreaterThanOrEqualToken : EqualityTokens
                data object LessThanToken : EqualityTokens
                data object LessThanOrEqualToken : EqualityTokens
            }
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

        sealed interface VariableDeclaration : SyntaxKind
        {
            data object  ValToken : VariableDeclaration
            data object  VarToken : VariableDeclaration
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
        data object VariableDeclarationStatementStatement : Statement
    }

    data object  CompilationUnit : SyntaxKind

}