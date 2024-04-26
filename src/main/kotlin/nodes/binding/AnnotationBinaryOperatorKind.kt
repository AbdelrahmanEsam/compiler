package com.moj.najiz.nodes.binding

interface AnnotationBinaryOperatorKind {
    data object Addition : AnnotationBinaryOperatorKind
    data object Subtraction : AnnotationBinaryOperatorKind
    data object Division : AnnotationBinaryOperatorKind
    data object Multiplication : AnnotationBinaryOperatorKind
}