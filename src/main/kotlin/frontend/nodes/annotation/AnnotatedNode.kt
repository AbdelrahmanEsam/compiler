package com.moj.najiz.frontend.nodes.annotation

abstract class AnnotatedNode {
    abstract val annotationKind: AnnotationKind
    open val children: List<AnnotatedNode>  = listOf()
}