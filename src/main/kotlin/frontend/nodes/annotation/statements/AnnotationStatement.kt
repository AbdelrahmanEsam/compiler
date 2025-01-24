package frontend.nodes.annotation.statements

import com.moj.najiz.frontend.expressionEvaluator.Evaluator
import com.moj.najiz.frontend.nodes.annotation.AnnotatedNode

abstract class AnnotationStatement : AnnotatedNode()
{


    abstract fun evaluate(evaluator: Evaluator) : Any?
}