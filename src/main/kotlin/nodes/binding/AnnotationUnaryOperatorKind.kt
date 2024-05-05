package com.moj.najiz.nodes.binding

interface AnnotationUnaryOperatorKind {
    data object Identity : AnnotationUnaryOperatorKind
    data object Negation : AnnotationUnaryOperatorKind
    data object LogicalNegation : AnnotationUnaryOperatorKind
}