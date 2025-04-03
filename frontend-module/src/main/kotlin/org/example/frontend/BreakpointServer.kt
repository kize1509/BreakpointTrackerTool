package org.example.frontend

import io.javalin.Javalin
import org.example.frontend.config.WebServerConfig
import org.example.frontend.controller.HealthCheckController
import org.example.frontend.websocket.WebSocketHandler

class BreakpointServer(private val port: Int = 0) {
    private val webServerConfig = WebServerConfig(port)
    private val wsHandler = WebSocketHandler()
    private val healthCheckController = HealthCheckController()

    private lateinit var app: Javalin
    private var actualPort: Int = 0

    fun start(): Int {
        app = webServerConfig.configureJavalin()

        app.ws("/breakpoints") { ws ->
            wsHandler.configure(ws)
        }

        healthCheckController.register(app)

        actualPort = webServerConfig.determinePort()
        app.start(actualPort)
        return actualPort
    }

    fun stop() {
        app.stop()
    }

    fun updateBreakpoints(breakpointData: String) {
        wsHandler.broadcastToAll(breakpointData)
    }

    fun setOnFrontendReadyCallback(callback: () -> Unit) {
        wsHandler.setOnFrontendReadyCallback(callback)
    }

    fun getServerUrl(): String {
        return "http://localhost:$actualPort"
    }
}