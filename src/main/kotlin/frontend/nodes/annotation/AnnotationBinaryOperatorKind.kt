package com.moj.najiz.frontend.nodes.annotation

sealed interface AnnotationBinaryOperatorKind {
    data object Addition : AnnotationBinaryOperatorKind
    data object Subtraction : AnnotationBinaryOperatorKind
    data object Division : AnnotationBinaryOperatorKind
    data object Multiplication : AnnotationBinaryOperatorKind
    data object LogicalAnd : AnnotationBinaryOperatorKind
    data object LogicalOr : AnnotationBinaryOperatorKind
    data object Equality : AnnotationBinaryOperatorKind
    data object NotEquality : AnnotationBinaryOperatorKind
    data object GreaterThan : AnnotationBinaryOperatorKind
    data object GreaterThanOrEqual : AnnotationBinaryOperatorKind
    data object LessThan : AnnotationBinaryOperatorKind
    data object LessThanOrEqual : AnnotationBinaryOperatorKind
}