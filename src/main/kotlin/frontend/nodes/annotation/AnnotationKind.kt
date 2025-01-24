package com.moj.najiz.frontend.nodes.annotation


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

    sealed interface AnnotationStatementKind : AnnotationKind
    {
        data object  BlockStatement : AnnotationStatementKind
        data object  ExpressionStatement : AnnotationStatementKind
    }
}
