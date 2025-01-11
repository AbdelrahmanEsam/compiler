package frontend.parser

import com.moj.najiz.frontend.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.frontend.lexicalAnalyzer.SyntaxKind
import com.moj.najiz.frontend.parser.Parser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

class ParserTest {
    private val lexicalAnalyzer : LexicalAnalyzer = LexicalAnalyzer()
    private val parser : Parser = Parser(lexicalAnalyzer)


    @Test
    fun inputBinaryExpressionShouldReturnBinaryExpressionSyntax() {
        lexicalAnalyzer.setCodeInput("1 + 2")
        val parserGetTokens = parser::class.declaredMemberFunctions.first { it.name == "getTokens" }.apply { isAccessible = true }
        parserGetTokens.call(parser)
        val syntaxTree = parser.parse()

        // assert that your tree is
            // +
        //1       \\2
        assertTrue( syntaxTree.root is com.moj.najiz.frontend.nodes.syntax.BinaryExpressionSyntax)
        assertTrue( syntaxTree.root.children.firstOrNull() is com.moj.najiz.frontend.nodes.syntax.NumberExpressionSyntax)
        assertTrue( syntaxTree.root.children[1].kind is SyntaxKind.Token.MathOperatorsToken.PlusToken)
        assertTrue( syntaxTree.root.children.lastOrNull() is com.moj.najiz.frontend.nodes.syntax.NumberExpressionSyntax)
    }

    @AfterEach
    fun tearDown() {
        lexicalAnalyzer.setCodeInput("")
    }

}