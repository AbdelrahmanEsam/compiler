package com.moj.najiz.nodes.binding


sealed interface AnnotationKind {

    sealed interface AnnotationExpressionKind : AnnotationKind {


        data object UnaryExpression : AnnotationExpressionKind
        data object BinaryExpression : AnnotationExpressionKind
        data object LiteralExpression : AnnotationExpressionKind

    }
}
