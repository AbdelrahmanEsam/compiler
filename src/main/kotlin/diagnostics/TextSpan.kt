package com.moj.najiz.diagnostics

class TextSpan(private val _start: Int, private val _length: Int) {

    val start: Int
        get() = _start

    val length: Int
        get() = _length

    val end: Int
        get() = _start + _length


}