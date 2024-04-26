package com.moj.najiz.nodes.binding

class AnnotationLiteralExpression(private val _value: Any) : AnnotationExpression() {
    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.LiteralExpression

    val value : Any
        get() = _value::class.java


}