package com.example.rssgrabber.retrofit

data class DataRequest(
    var url: String? = null,
    var id: Long = 0,
    var data: Any? = null,
    var type: Int = 0
)