package frontend.parser

import com.moj.najiz.frontend.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.syntax.*
import com.moj.najiz.frontend.nodes.syntax.expressions.AssignmentExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.BinaryExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.LiteralExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.NameExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.NumberExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.ParenthesizedExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.expressions.UnaryExpressionSyntax
import com.moj.najiz.frontend.nodes.syntax.statements.ExpressionStatementSyntax
import com.moj.najiz.frontend.parser.Parser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

class ParserTest {
    private val lexicalAnalyzer: LexicalAnalyzer = LexicalAnalyzer()
    private val parser: Parser = Parser(lexicalAnalyzer)


    @Test
    fun inputBinaryExpressionShouldReturnBinaryExpressionSyntax() {
        lexicalAnalyzer.setCodeInput("1 + 2")
        val parserGetTokens =
            parser::class.declaredMemberFunctions.first { it.name == "getTokens" }.apply { isAccessible = true }
        parserGetTokens.call(parser)
        val syntaxTree = parser.parseCompilationUnit()

        // assert that your tree is
        // +
        //1       \\2
        assertTrue(syntaxTree.root is ExpressionStatementSyntax)
        assertTrue((syntaxTree.root as ExpressionStatementSyntax).expression is BinaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull() is NumberExpressionSyntax)
        assertTrue(syntaxTree.root.children[1].kind is SyntaxKind.Token.Math.OperatorsTokens.PlusToken)
        assertTrue(syntaxTree.root.children.lastOrNull() is NumberExpressionSyntax)
    }

    @Test
    fun inputAssignmentExpressionShouldReturnAssignmentExpressionSyntax() {
        lexicalAnalyzer.setCodeInput("variable = 1")
        val parserGetTokens =
            parser::class.declaredMemberFunctions.first { it.name == "getTokens" }.apply { isAccessible = true }
        parserGetTokens.call(parser)
        val syntaxTree = parser.parseCompilationUnit()

        // assert that your tree is
        //    =
        //variable    1
        assertTrue(syntaxTree.root is ExpressionStatementSyntax)
        assertTrue((syntaxTree.root as ExpressionStatementSyntax).expression is AssignmentExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull() is SyntaxToken)
        assertTrue(syntaxTree.root.children.lastOrNull() is NumberExpressionSyntax)
    }

    @Test
    fun inputNameExpressionShouldReturnNameExpressionSyntax() {
        lexicalAnalyzer.setCodeInput("variable")


        val parserGetTokens =
            parser::class.declaredMemberFunctions.first { it.name == "getTokens" }.apply { isAccessible = true }
        parserGetTokens.call(parser)
        val syntaxTree = parser.parseCompilationUnit()


        assertTrue(syntaxTree.root is ExpressionStatementSyntax)
        assertTrue((syntaxTree.root as ExpressionStatementSyntax).expression is NameExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull() is SyntaxToken)

    }

    @Test
    fun inputUnaryExpressionWithMinusShouldReturnUnaryExpressionSyntax() {
        lexicalAnalyzer.setCodeInput("-1")
        val parserGetTokens =
            parser::class.declaredMemberFunctions.first { it.name == "getTokens" }.apply { isAccessible = true }
        parserGetTokens.call(parser)
        val syntaxTree = parser.parseCompilationUnit()

        // assert that your tree is
        // -
        //1
        assertTrue(syntaxTree.root is ExpressionStatementSyntax)
        assertTrue((syntaxTree.root as ExpressionStatementSyntax).expression is UnaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull()?.kind is SyntaxKind.Token.Math.OperatorsTokens.MinusToken)
        assertTrue(syntaxTree.root.children.lastOrNull() is NumberExpressionSyntax)
    }

    @Test
    fun inputUnaryExpressionWithParenthesizedExpressionWithMinusShouldReturnUnaryExpressionSyntaxWithParenthesizedExpressionInsideItAnd() {
        lexicalAnalyzer.setCodeInput("-(-1)")
        val parserGetTokens =
            parser::class.declaredMemberFunctions.first { it.name == "getTokens" }.apply { isAccessible = true }
        parserGetTokens.call(parser)
        val syntaxTree = parser.parseCompilationUnit()
        assertTrue(syntaxTree.root is ExpressionStatementSyntax)
        assertTrue((syntaxTree.root as ExpressionStatementSyntax).expression is UnaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull()?.kind is SyntaxKind.Token.Math.OperatorsTokens.MinusToken)
        assertTrue(syntaxTree.root.children[1] is ParenthesizedExpressionSyntax)
    }


    @Test
    fun inputUnaryExpressionWithPlusShouldReturnUnaryExpressionSyntax() {
        lexicalAnalyzer.setCodeInput("+1")
        val parserGetTokens =
            parser::class.declaredMemberFunctions.first { it.name == "getTokens" }.apply { isAccessible = true }
        parserGetTokens.call(parser)
        val syntaxTree = parser.parseCompilationUnit()

        // assert that your tree is
        // +
        //1
        assertTrue(syntaxTree.root is ExpressionStatementSyntax)
        assertTrue((syntaxTree.root as ExpressionStatementSyntax).expression is UnaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull()?.kind is SyntaxKind.Token.Math.OperatorsTokens.PlusToken)
        assertTrue(syntaxTree.root.children.lastOrNull() is NumberExpressionSyntax)
    }

    @Test
    fun inputUnaryExpressionWithNotShouldReturnUnaryExpressionSyntax() {
        lexicalAnalyzer.setCodeInput("!true")
        val parserGetTokens =
            parser::class.declaredMemberFunctions.first { it.name == "getTokens" }.apply { isAccessible = true }
        parserGetTokens.call(parser)
        val syntaxTree = parser.parseCompilationUnit()

        // assert that your tree is
        // +
        //1
        assertTrue(syntaxTree.root is ExpressionStatementSyntax)
        assertTrue((syntaxTree.root as ExpressionStatementSyntax).expression is UnaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull()?.kind is SyntaxKind.Token.BooleanOperatorToken.NotToken)
        assertTrue((syntaxTree.root.children.lastOrNull() is LiteralExpressionSyntax)   && (syntaxTree.root.children.lastOrNull() as LiteralExpressionSyntax).literalToken.kind is SyntaxKind.Token.BooleanToken.TrueToken)
    }

    @Test
    fun inputParenthesizedExpressionInsideItBinaryExpressionSyntaxShouldReturnParenthesizedExpressionSyntaxInsideItBinaryExpressionSyntaxBetweenBrackets() {
        lexicalAnalyzer.setCodeInput("(1 + 2)")
        val parserGetTokens =
            parser::class.declaredMemberFunctions.first { it.name == "getTokens" }.apply { isAccessible = true }
        parserGetTokens.call(parser)
        val syntaxTree = parser.parseCompilationUnit()

        // assert that your tree is
        // -
        //1
        assertTrue(syntaxTree.root is ExpressionStatementSyntax)
        assertTrue((syntaxTree.root as ExpressionStatementSyntax).expression is ParenthesizedExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull()?.kind is SyntaxKind.Token.ParenthesisToken.OpenParenthesisToken)
        assertTrue(syntaxTree.root.children[1] is BinaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.lastOrNull()?.kind is SyntaxKind.Token.ParenthesisToken.CloseParenthesisToken)
    }




    @AfterEach
    fun tearDown() {
        lexicalAnalyzer.setCodeInput("")
    }

}