package com.example.rssgrabber.commons

interface HasComponent<out T> {
    fun getComponent(): T?
}
