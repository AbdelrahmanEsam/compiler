package com.moj.najiz.frontend.nodes.annotation.scope

import com.moj.najiz.frontend.VariableSymbol

class AnnotatedScope(private val _parentScope: AnnotatedScope?) {
    private val _variables: MutableMap<VariableSymbol, Any> = mutableMapOf()

    val variables: Map<VariableSymbol, Any> get() = _variables.toMap()

    val parentScope: AnnotatedScope?
        get() = _parentScope

    fun tryDeclare(variable: VariableSymbol, value: Any): Boolean {
        if (_variables.contains(variable)) return false
        var myParentScope: AnnotatedScope? = _parentScope
        while (myParentScope != null) {
            if (myParentScope.variables.contains(variable) == true) return true
            myParentScope = myParentScope._parentScope
        }
        _variables[variable] = value
        return true
    }

    fun tryLookUp(name: String?): Boolean {
        var variable = _variables.keys.firstOrNull { it.name == name }
        if (variable != null) return true
        var myParentScope : AnnotatedScope? = _parentScope
        while (myParentScope != null){
            variable = myParentScope.variables.keys.firstOrNull { it.name == name }
            if(variable != null) return true
            myParentScope = myParentScope._parentScope
        }
        return false
    }

    fun get(name: String): Pair<VariableSymbol,Any>? {
        var variable = _variables.keys.firstOrNull { it.name == name }
        if(variable != null) return Pair(variable,_variables[variable]!!)
        var myParentScope: AnnotatedScope? = _parentScope
        while (myParentScope != null) {
            variable = myParentScope.variables.keys.firstOrNull { it.name == name }
            if(variable != null) return Pair(variable,myParentScope.variables[variable]!!)
            myParentScope = myParentScope._parentScope
        }
        return null
    }

}