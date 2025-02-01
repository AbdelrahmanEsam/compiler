package com.moj.najiz.frontend.nodes.annotation.scope

import com.moj.najiz.frontend.VariableSymbol

class AnnotatedScope(private val parentScope: AnnotatedScope?) {
    private val _variables: MutableMap<VariableSymbol, Any> = mutableMapOf()

    val variables: Map<VariableSymbol, Any> get() = _variables.toMap()

    fun tryDeclare(variable: VariableSymbol, value: Any): Boolean {
        if (_variables.contains(variable)) return false
        var myParentScope: AnnotatedScope? = parentScope
        while (myParentScope != null) {
            if (myParentScope.variables.contains(variable) == true) return true
            myParentScope = myParentScope.parentScope
        }
        _variables[variable] = value
        return true
    }

    fun tryLookUp(name: String?): Boolean {
        var variable = _variables.keys.firstOrNull { it.name == name }
        if (variable != null) return true
        var myParentScope : AnnotatedScope? = parentScope
        while (myParentScope != null){
            variable = myParentScope.variables.keys.firstOrNull { it.name == name }
            if(variable != null) return true
            myParentScope = myParentScope.parentScope
        }
        return false
    }

    fun get(name: String): VariableSymbol? {
        var variable = _variables.keys.firstOrNull { it.name == name }
        var myParentScope: AnnotatedScope? = parentScope
        while (myParentScope != null) {
            variable = myParentScope.variables.keys.firstOrNull { it.name == name }
            if(variable != null) return variable
            myParentScope = myParentScope.parentScope
        }
        return null
    }

}