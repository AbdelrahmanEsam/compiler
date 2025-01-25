package com.moj.najiz.utils

import kotlin.reflect.KType
import kotlin.reflect.typeOf

inline fun <reified T> getKType(value: T): KType {
    return typeOf<T>()
}