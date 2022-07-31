package com.raffaellmir.exchangerate.data.network.models.response

import com.raffaellmir.exchangerate.data.network.models.Error

data class ErrorResponse(
    val error: Error
)