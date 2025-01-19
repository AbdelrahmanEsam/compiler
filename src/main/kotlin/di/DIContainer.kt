package com.moj.najiz.di

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.frontend.parser.Parser

object DIContainer {

    fun getLexer(): LexicalAnalyzer = LexicalAnalyzer()

    fun getParser(lexicalAnalyzer: LexicalAnalyzer): Parser = Parser(lexicalAnalyzer)


}