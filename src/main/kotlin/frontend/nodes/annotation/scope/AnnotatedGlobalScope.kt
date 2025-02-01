package com.moj.najiz.frontend.nodes.annotation.scope

import com.moj.najiz.frontend.VariableSymbol
import com.moj.najiz.frontend.diagnostics.Diagnostic
import frontend.nodes.annotation.statements.AnnotationStatement
import kotlin.collections.set

class AnnotatedGlobalScope(
    private val _previousScope: AnnotatedGlobalScope? = null,
    private val _diagnosticsBag: Diagnostic.DiagnosticsBag,
    private val _variables: MutableMap<VariableSymbol, Any>,
    private val _statement: AnnotationStatement,
) {

    val previousScope: AnnotatedGlobalScope?
        get() = _previousScope

    val variables: Map<VariableSymbol, Any>
        get() = _variables

    val diagnostics: Diagnostic.DiagnosticsBag
        get() = _diagnosticsBag
    val statement: AnnotationStatement
        get() = _statement

    fun tryDeclare(variable : VariableSymbol, value : Any) : Boolean {
        // if(_variables.contains(variable)) return false
        _variables[variable] = value
        return true
    }

    fun tryLookUp(name: String?): Any? {
        var variable = _variables.keys.firstOrNull { it.name == name }
        if (variable != null) return true
        var myParentScope: AnnotatedGlobalScope? = _previousScope
        return myParentScope?.tryLookUp(variable)
    }

    fun get(variable: VariableSymbol?): Pair<VariableSymbol, Any>? {
        if (_variables.contains(variable)) return Pair(variable!!,_variables[variable]!!)
        var myParentScope: AnnotatedGlobalScope? = _previousScope
        while (myParentScope != null) {
            if (myParentScope.variables.contains(variable) == true) return Pair(variable!!,myParentScope.variables[variable]!!)
            myParentScope = myParentScope.previousScope
        }
        return null
    }


}