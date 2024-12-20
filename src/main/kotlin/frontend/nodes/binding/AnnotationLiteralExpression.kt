package com.moj.najiz.frontend.nodes.binding

class AnnotationLiteralExpression(private val _value: Any) : AnnotationExpression() {
    override val annotationKind: AnnotationKind
        get() = AnnotationKind.AnnotationExpressionKind.LiteralExpression


   override val value: Any
        get() = _value




}