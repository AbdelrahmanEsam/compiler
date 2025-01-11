package com.moj.najiz.frontend.nodes.binding


sealed interface AnnotationKind {

    sealed interface AnnotationExpressionKind : AnnotationKind {


        data object UnaryExpression : AnnotationExpressionKind
        data object BinaryExpression : AnnotationExpressionKind
        data object LiteralExpression : AnnotationExpressionKind
        data object NumberExpression : AnnotationExpressionKind
        data object AnnotationParenthesizedExpression : AnnotationExpressionKind
        data object AnnotationNameExpression : AnnotationExpressionKind
        data object AnnotationAssignmentExpression : AnnotationExpressionKind

    }
}
