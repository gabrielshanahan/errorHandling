package org.example.pres

/*
 * Base types
 */

@JvmInline
value class AccountId(val value: String)

@JvmInline
value class OrderId(val value: String)

data class Account(val accountId: AccountId, val name: String, val email: String, val phone: String)
data class Order(val orderId: OrderId, val accountId: AccountId, val value: Double)
data class Pricing(val accountId: AccountId, val percentageOfOrderValue: Double)

object Base {
    /*
     * Base functions
     */

    fun getAccount(accountId: AccountId): Account = TODO()
    fun getPricing(accountId: AccountId): Pricing = TODO()
    fun getOrder(orderId: OrderId): Order = TODO()
    fun getOrders(accountId: AccountId): List<Order> = TODO()

    /*
     * Implementations
     */

    fun getAccountName(accountId: AccountId): String = getAccount(accountId).name

    fun getChargeForOrder(orderId: OrderId): Double {
        val order = getOrder(orderId)
        val pricing = getPricing(order.accountId)

        return order.value * pricing.percentageOfOrderValue
    }

    fun getTotalBillForAccount(accountId: AccountId): Double {
        val pricing = getPricing(accountId)
        val orders = getOrders(accountId)

        return orders.sumOf { it.value * pricing.percentageOfOrderValue }
    }
}