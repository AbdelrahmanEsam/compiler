package com.moj.najiz.frontend.nodes.annotation.scope

import com.moj.najiz.frontend.diagnostics.Diagnostic
import com.moj.najiz.frontend.nodes.annotation.AnnotationExpression
import kotlin.collections.set

class AnnotatedGlobalScope(
    private val previousScope: AnnotatedGlobalScope? = null,
    private val _diagnosticsBag: Diagnostic.DiagnosticsBag,
    private val _variables : MutableMap<String,Any>,
    private val _expression : AnnotationExpression,
) {

     val variables : Map<String,Any>
        get() = _variables

    val diagnostics :Diagnostic.DiagnosticsBag
        get() = _diagnosticsBag
    val expression :AnnotationExpression
        get() = _expression

    fun tryDeclare(name : String, value : Any) : Boolean {
       // if(_variables.contains(name)) return false
        _variables[name] = value
        return true
    }

    fun tryLookUp(name : String?) : Boolean{
        if( _variables.contains(name)) return true
        return previousScope?.variables?.contains(name) == true
    }

    fun get(name : String?) : Any? {
        if(_variables.contains(name)) return _variables[name]!!
        if(previousScope?.variables?.contains(name) == true)return previousScope.variables[name]!!
        return null
    }


}