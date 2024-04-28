package org.example.pres

//typealias BusinessResult<T> = Result<BusinessError, T>

/*
 * Base types
 */

// aka Either<L, R>
sealed interface Result<out E, out R>
// aka Left<L>
data class Err<out E>(val error: E) : Result<E, Nothing>
// aka Right<R>
data class Ok<out R>(val value: R) : Result<Nothing, R>

sealed interface BusinessError
class CannotBeBlank(field: String) : BusinessError

/*
 * Base functions
 */

fun getAccount(accountId: AccountId): Result<BusinessError, Account> = TODO()
fun getPricing(accountId: AccountId): Result<BusinessError, Pricing> = TODO()
fun getOrder(orderId: OrderId): Result<BusinessError, Order> = TODO()
fun getOrders(accountId: AccountId): Result<BusinessError, List<Order>> = TODO()

object ResultNaive {

    /*
     * Implementations
     */

    // (Result<BusinessError, Account>, (Account) -> String) -> Result<BusinessError, String>
    fun getAccountName(accountId: AccountId): Result<BusinessError, String> =
        when (val accountR = getAccount(accountId)) {
            is Ok -> Ok(accountR.value.name)
            is Err -> accountR
        }

    // (Result<BusinessError, Order>, (Account) -> Result<BusinessError, Double>) -> Result<BusinessError, Double>
    fun getChargeForOrder(orderId: OrderId): Result<BusinessError, Double> =
        when (val orderR = getOrder(orderId)) {
            is Ok -> when (val pricingR = getPricing(orderR.value.accountId)) {
                is Ok -> Ok(orderR.value.value * pricingR.value.percentageOfOrderValue)
                is Err -> pricingR
            }
            is Err -> orderR
        }

    // (Result<BusinessError, Pricing>, Result<BusinessError, List<Order>>, (Pricing, List<Order>) -> Double) -> Result<BusinessError, Double>
    fun getChargeForAccount(accountId: AccountId): Result<BusinessError, Double> =
        when (val pricingR = getPricing(accountId)) {
            is Ok -> when (val ordersR = getOrders(accountId)) {
                is Ok -> Ok(ordersR.value.sumOf { it.value * pricingR.value.percentageOfOrderValue })
                is Err -> ordersR
            }
            // Other possibilities here!
            is Err -> pricingR
        }
    
    // Typealias!
}