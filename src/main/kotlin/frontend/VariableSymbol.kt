package com.moj.najiz.frontend

import kotlin.reflect.KType

data class VariableSymbol(val name : String, val type : KType , val isReadOnly : Boolean)