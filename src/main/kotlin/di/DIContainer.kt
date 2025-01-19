package com.moj.najiz.di

import com.moj.najiz.frontend.annotator.Annotator
import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.lexicalAnalyzer.LexicalAnalyzer
import com.moj.najiz.frontend.nodes.annotation.scope.AnnotatedGlobalScope
import com.moj.najiz.frontend.nodes.annotation.scope.AnnotatedScope
import com.moj.najiz.frontend.parser.Parser

object DIContainer {

    fun getLexer(): LexicalAnalyzer = LexicalAnalyzer()

    fun getParser(lexicalAnalyzer: LexicalAnalyzer): Parser = Parser(lexicalAnalyzer)


    fun getAnnotatedScope(parentScope: AnnotatedScope? = null ) : AnnotatedScope = AnnotatedScope(parentScope)


    fun getAnnotator(parentScope: AnnotatedScope? = null ) : Annotator = Annotator(parentScope)


    fun getEvaluator(parentScope: AnnotatedGlobalScope? = null ) : Evaluator = Evaluator(parentScope)


}