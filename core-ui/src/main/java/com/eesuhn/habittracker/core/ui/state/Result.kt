package com.eesuhn.habittracker.core.ui.state

sealed class Result<out T> {
    data class Success<R>(val value: R) : Result<R>()
    object Loading : Result<Nothing>()
    data class Failure(val error: Throwable) : Result<Nothing>()
}