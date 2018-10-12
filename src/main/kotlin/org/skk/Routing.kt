package org.skk

import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.skk.resource.Accounts
import org.skk.resource.Health
import org.skk.resource.Transfers

fun Routing.routeConfiguration(dependencyProvider :DependencyProvider) {
    val health = dependencyProvider.healthResource()
    val transfer = dependencyProvider.transferResource()
    val account = dependencyProvider.accountsResource()

    get("/health") {
        health.get(call)
    }
    post("/transfers") {
        transfer.post(call)
    }
    route("/accounts") {
        post {
            account.post(call)
        }
        get("{id}") {
            account.get(call)
        }

        get("{id}/balance") {
            account.getBalance(call)
        }
    }
}