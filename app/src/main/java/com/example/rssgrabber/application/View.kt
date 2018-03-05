package com.example.rssgrabber.application

import com.example.rssgrabber.retrofit.DataResponse

interface View {
    fun showError(dataResponse: DataResponse)
    fun hideError()
}
