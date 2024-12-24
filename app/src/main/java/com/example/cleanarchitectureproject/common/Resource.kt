package com.example.cleanarchitectureproject.common

sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    //T is placeholder and its type will be specified when the instance is created

    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}