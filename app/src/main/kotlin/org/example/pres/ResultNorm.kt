package org.example.pres


/*
 * Base functions
 */

inline fun <E, R1, R> Result<E, R1>.map(f: (R1) -> R): Result<E, R> = when (this) {
    is Ok -> Ok(f(this.value))
    is Err -> this
}

inline fun <E, R1, R> Result<E, R1>.flatMap(f: (R1) -> Result<E, R>): Result<E, R> = when (this) {
    is Ok -> f(this.value)
    is Err -> this
}

inline fun <E, R1, R2, R> mapN(
    result1: Result<E, R1>,
    result2: Result<E, R2>,
    f: (R1, R2) -> R
): Result<E, R> = when (result1) {
    is Ok -> when (result2) {
        is Ok -> Ok(f(result1.value, result2.value))
        is Err -> result2
    }
    is Err -> result1
}

object ResultNorm {

    fun getAccountName(accountId: AccountId): BusinessResult<String> =
        getAccount(accountId).map { it.name }

    fun getChargeForOrder(orderId: OrderId): BusinessResult<Double> =
        getOrder(orderId).flatMap { order ->
            getPricing(order.accountId).map { pricing ->
                order.value * pricing.percentageOfOrderValue
            }
        }

    fun getChargeForAccount(accountId: AccountId): BusinessResult<Double> =
        mapN(getPricing(accountId), getOrders(accountId)) { pricing, orders ->
            orders.sumOf { it.value + pricing.percentageOfOrderValue}
        }
}