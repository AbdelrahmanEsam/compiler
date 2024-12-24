package com.moj.najiz.frontend.nodes.binding

import com.moj.najiz.frontend.expressionEvaluator.Evaluator

abstract class AnnotationExpression : AnnotatedNode()
{
    abstract val value : Any

    abstract fun evaluate(evaluator: Evaluator) : Any


}