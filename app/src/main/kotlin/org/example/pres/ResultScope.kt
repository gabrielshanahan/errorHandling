package org.example.pres

import arrow.core.None
import arrow.core.Option
import arrow.core.Some

    class ResultScope<E> {
        fun <R> Result<E, R>.bind(): R = when (this) {
            is Ok -> value
            is Err -> fail(error)
        }

        fun <R> Option<R>.bind(error: () -> E): R = when (this) {
            is Some -> value
            is None -> fail(error())
        }

        fun <R> R?.bind(error: () -> E): R = when (this) {
            null -> fail(error())
            else -> this
        }

        fun fail(error: E): Nothing = throw ShortCircuitException(error)
    }

    class ShortCircuitException(val value: Any?) : RuntimeException()

    inline fun <E, R> result(
        block: ResultScope<E>.() -> R
    ): Result<E, R> =
        try {
            Ok(ResultScope<E>().block())
        } catch (e: ShortCircuitException) {
            Err(e.value as E)
        }

infix fun <E, R1, R2> Result<E, R1>.zip(other: Result<E, R2>): Result<E, Pair<R1, R2>> =
    when(this) {
        is Ok -> when(other) {
            is Ok -> Ok(value to other.value)
            is Err -> other
        }
        is Err -> this
    }