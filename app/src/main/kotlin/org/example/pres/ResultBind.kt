package org.example.pres

typealias BusinessResult<T> = Result<BusinessError, T>

object ResultBind {

    fun getAccount(accountId: AccountId): BusinessResult<Account> = TODO()
    fun getPricing(accountId: AccountId): BusinessResult<Pricing> = TODO()
    fun getOrder(orderId: OrderId): BusinessResult<Order> = TODO()
    fun getOrders(accountId: AccountId): BusinessResult<List<Order>> = TODO()

    fun getAccountName(accountId: AccountId): BusinessResult<String> = result {
        val account = getAccount(accountId).bind()
        account.name
    }

    fun getChargeForOrder(orderId: OrderId): BusinessResult<Double> = result {
        val order = getOrder(orderId).bind()
        val pricing = getPricing(order.accountId).bind()

        order.value * pricing.percentageOfOrderValue
    }

    fun getChargeForAccount(accountId: AccountId): BusinessResult<Double> = result {
        val (pricing, orders) = (getPricing(accountId) zip getOrders(accountId)).bind()
        orders.sumOf { it.value + pricing.percentageOfOrderValue}
    }

}