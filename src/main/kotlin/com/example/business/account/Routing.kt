package com.example.business.account

import com.example.plugins.CityService
import com.example.plugins.connectToPostgres
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection


fun Application.routingAccount() {
    val dbConnection: Connection = connectToPostgres(embedded = true)
    val accountService = AccountService(dbConnection)
    routing {

        get("/account/login") {
            handleLogin(accountService)
        }

    }
}


internal fun log(msg: String) = println("[Account] $msg")

data class LoginResult(
    val status: StatusCode = StatusCode.SUCCESS,
    val message: String? = null
)

enum class StatusCode(val code: Int) {
    ERROR(0),
    SUCCESS(1),
}