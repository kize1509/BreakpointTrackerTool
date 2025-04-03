package org.example.frontend.controller

import io.javalin.Javalin
import io.javalin.http.Context

class HealthCheckController {
    fun register(app: Javalin) {
        app.get("/health") { ctx -> handleHealthCheck(ctx) }
    }

    private fun handleHealthCheck(ctx: Context) {
        ctx.result("Breakpoint server running")
    }
}