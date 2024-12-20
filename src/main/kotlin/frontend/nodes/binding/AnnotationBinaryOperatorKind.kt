package com.moj.najiz.frontend.nodes.binding

interface AnnotationBinaryOperatorKind {
    data object Addition : AnnotationBinaryOperatorKind
    data object Subtraction : AnnotationBinaryOperatorKind
    data object Division : AnnotationBinaryOperatorKind
    data object Multiplication : AnnotationBinaryOperatorKind
    data object LogicalAnd : AnnotationBinaryOperatorKind
    data object LogicalOr : AnnotationBinaryOperatorKind
    data object Equality : AnnotationBinaryOperatorKind
    data object NotEquality : AnnotationBinaryOperatorKind
}