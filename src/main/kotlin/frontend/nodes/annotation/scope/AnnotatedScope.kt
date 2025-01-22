package com.moj.najiz.frontend.nodes.annotation.scope

import kotlin.collections.get

class AnnotatedScope(private val parentScope: AnnotatedScope?)  {
    private val _variables : MutableMap<String,Any> = mutableMapOf()

    val variables : Map<String,Any> get() = _variables.toMap()

    fun tryDeclare(name : String, value : Any) : Boolean {
      //  if(_variables.contains(name)) return false
        _variables[name] = value
        return true
    }

    fun tryLookUp(name : String?) : Boolean{
       if( _variables.contains(name)) return true
        var myParentScope : AnnotatedScope? = parentScope
        while (myParentScope != null){
            if(myParentScope.variables.contains(name) == true) return true
            myParentScope = myParentScope.parentScope
        }
      return false
    }

    fun get(name : String?) : Any? {
        if(_variables.contains(name)) return _variables[name]!!
        var myParentScope : AnnotatedScope? = parentScope
        while (myParentScope != null){
            if(myParentScope.variables.contains(name) == true) return myParentScope.variables[name]!!
            myParentScope = myParentScope.parentScope
        }
        return null
    }

}