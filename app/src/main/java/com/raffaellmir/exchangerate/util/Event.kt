package com.raffaellmir.exchangerate.util

sealed class Event<T>(val data: T? = null, val error: String? = null) {
    class Loading<T>(data: T? = null): Event<T>(data)
    class Success<T>(data: T?): Event<T>(data)
    class Error<T>(error: String, data: T? = null): Event<T>(data, error)
}