package frontend.parser

import com.moj.najiz.frontend.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.nodes.syntax.*
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
        assertTrue(syntaxTree.root is BinaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull() is NumberExpressionSyntax)
        assertTrue(syntaxTree.root.children[1].kind is SyntaxKind.Token.MathOperatorsToken.PlusToken)
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
        assertTrue(syntaxTree.root is AssignmentExpressionSyntax)
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

        1
        assertTrue(syntaxTree.root is NameExpressionSyntax)
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
        assertTrue(syntaxTree.root is UnaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull()?.kind is SyntaxKind.Token.MathOperatorsToken.MinusToken)
        assertTrue(syntaxTree.root.children.lastOrNull() is NumberExpressionSyntax)
    }

    @Test
    fun inputUnaryExpressionWithParenthesizedExpressionWithMinusShouldReturnUnaryExpressionSyntaxWithParenthesizedExpressionInsideItAnd() {
        lexicalAnalyzer.setCodeInput("-(-1)")
        val parserGetTokens =
            parser::class.declaredMemberFunctions.first { it.name == "getTokens" }.apply { isAccessible = true }
        parserGetTokens.call(parser)
        val syntaxTree = parser.parseCompilationUnit()
        assertTrue(syntaxTree.root is UnaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull()?.kind is SyntaxKind.Token.MathOperatorsToken.MinusToken)
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
        assertTrue(syntaxTree.root is UnaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull()?.kind is SyntaxKind.Token.MathOperatorsToken.PlusToken)
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
        assertTrue(syntaxTree.root is UnaryExpressionSyntax)
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
        assertTrue(syntaxTree.root is ParenthesizedExpressionSyntax)
        assertTrue(syntaxTree.root.children.firstOrNull()?.kind is SyntaxKind.Token.BracketsToken.OpenBracketToken)
        assertTrue(syntaxTree.root.children[1] is BinaryExpressionSyntax)
        assertTrue(syntaxTree.root.children.lastOrNull()?.kind is SyntaxKind.Token.BracketsToken.CloseBracketToken)
    }




    @AfterEach
    fun tearDown() {
        lexicalAnalyzer.setCodeInput("")
    }

}