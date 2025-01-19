package com.moj.najiz.frontend.nodes.annotation.scope

import kotlin.collections.get

class AnnotatedScope(private val parentScope: AnnotatedScope?)  {
    private val _variables : MutableMap<String,Any> = mutableMapOf()

    val variables : Map<String,Any> get() = _variables.toMap()

    fun tryDeclare(name : String, value : Any) : Boolean {
        if(_variables.contains(name)) return false
        _variables[name] = value
        return true
    }

    fun tryLookUp(name : String?) : Boolean{
       if( _variables.contains(name)) return true
      return parentScope?.variables?.contains(name) == true
    }

    fun get(name : String?) : Any? {
        if(_variables.contains(name)) return _variables[name]!!
        if(parentScope?.variables?.contains(name) == true)return parentScope.variables[name]!!
        return null
    }

}