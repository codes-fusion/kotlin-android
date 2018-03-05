package com.example.rssgrabber.commons

class MessageEvent(var type: Type? = null, var value: String? = null) {
    enum class Type {
        CHANGE_COLOR,
        UPDATE_LIST
    }
}