package com.moj.najiz.frontend.nodes.annotation

sealed class AnnotatedNode {
    abstract val annotationKind: AnnotationKind
}