package com.moj.najiz.frontend.nodes.annotation

interface AnnotationUnaryOperatorKind {
    data object Identity : AnnotationUnaryOperatorKind
    data object Negation : AnnotationUnaryOperatorKind
    data object LogicalNegation : AnnotationUnaryOperatorKind
}