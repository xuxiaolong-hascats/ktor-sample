package com.example.business.account

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handleLogin(accountService: AccountService) {

    val name = call.request.queryParameters["name"]
    val password = call.request.queryParameters["password"]
    if (name == null) {
        call.respond(LoginResult(StatusCode.ERROR, "Name is required"))
    }
    if (password == null) {
        call.respond(LoginResult(StatusCode.ERROR, "Password is required"))
    }

    val queryAccount = accountService.read(name!!)

    if (queryAccount == null) {
        call.respond(LoginResult(StatusCode.ERROR, "Account not found"))
    }

    if (queryAccount!!.password != password) {
        call.respond(LoginResult(StatusCode.ERROR, "Password do not match"))
    }

    call.respond(LoginResult(StatusCode.SUCCESS, null))
}

