package com.ismail.homedecorai

import com.ismail.homedecorai.whop.InMemoryUserAccessStore
import com.ismail.homedecorai.whop.WhopConfig
import com.ismail.homedecorai.whop.WhopService
import com.ismail.homedecorai.whop.requireUserId
import com.ismail.homedecorai.whop.requireWhopAccess
import com.ismail.homedecorai.whop.whopWebhooks
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Application")

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val whopService = WhopService()
    val userAccessStore = InMemoryUserAccessStore()

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("X-User-Id")
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            logger.error("Unhandled exception: ${cause.message}", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to "Internal server error")
            )
        }
    }

    routing {
        get("/") {
            call.respondText("HomeDecorAI Server is running")
        }

        get("/health") {
            call.respond(mapOf("status" to "healthy"))
        }

        whopWebhooks(userAccessStore)

        route("/api/premium/features") {
            get {
                val userId = call.requireWhopAccess(whopService) ?: return@get
                call.respond(
                    mapOf(
                        "userId" to userId,
                        "features" to listOf(
                            "ai-room-design",
                            "unlimited-boards",
                            "export-hd",
                            "priority-support"
                        )
                    )
                )
            }
        }

        route("/api/premium/membership") {
            get {
                val userId = call.requireWhopAccess(whopService) ?: return@get
                val membership = whopService.getUserMembership(userId)
                call.respond(
                    mapOf(
                        "userId" to userId,
                        "membership" to membership
                    )
                )
            }
        }

        route("/api/checkout") {
            get("/urls") {
                call.respond(
                    mapOf(
                        "monthly" to WhopConfig.CHECKOUT_URL_MONTHLY,
                        "yearly" to WhopConfig.CHECKOUT_URL_YEARLY,
                        "pro" to WhopConfig.CHECKOUT_URL_PRO
                    )
                )
            }
        }
    }

    logger.info("HomeDecorAI server started")
}
