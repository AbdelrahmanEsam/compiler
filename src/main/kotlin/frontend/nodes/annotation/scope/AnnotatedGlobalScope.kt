package com.moj.najiz.frontend.nodes.annotation.scope

import com.moj.najiz.frontend.diagnostics.Diagnostic
import frontend.nodes.annotation.statements.AnnotationStatement
import kotlin.collections.set

class AnnotatedGlobalScope(
    private val _previousScope: AnnotatedGlobalScope? = null,
    private val _diagnosticsBag: Diagnostic.DiagnosticsBag,
    private val _variables : MutableMap<String,Any>,
    private val _statement : AnnotationStatement,
) {

     val previousScope : AnnotatedGlobalScope?
        get() = _previousScope

     val variables : Map<String,Any>
        get() = _variables

    val diagnostics :Diagnostic.DiagnosticsBag
        get() = _diagnosticsBag
    val statement :AnnotationStatement
        get() = _statement

    fun tryDeclare(name : String, value : Any) : Boolean {
       // if(_variables.contains(name)) return false
        _variables[name] = value
        return true
    }

    fun tryLookUp(name : String?) : Boolean{
        if( _variables.contains(name)) return true
        var myParentScope : AnnotatedGlobalScope? = _previousScope
        while (myParentScope != null){
            if(myParentScope.variables.contains(name) == true) return true
            myParentScope = myParentScope.previousScope
        }
        return false
    }

    fun get(name : String?) : Any? {
        if(_variables.contains(name)) return _variables[name]!!
        var myParentScope : AnnotatedGlobalScope? = _previousScope
        while (myParentScope != null){
            if(myParentScope.variables.contains(name) == true) return myParentScope.variables[name]!!
            myParentScope = myParentScope.previousScope
        }
        return null
    }


}