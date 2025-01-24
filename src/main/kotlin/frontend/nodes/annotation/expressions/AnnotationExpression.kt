package com.moj.najiz.frontend.nodes.annotation.expressions

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotatedNode

abstract class AnnotationExpression : AnnotatedNode()
{
    abstract val value : Any

    abstract fun evaluate(evaluator: Evaluator) : Any?
}