package org.example.frontend.websocket

import io.javalin.websocket.WsConfig
import io.javalin.websocket.WsContext
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class WebSocketHandler {
    private val wsConnections = ConcurrentHashMap<Int, WsContext>()
    private val nextId = AtomicInteger(0)
    private var onFrontendReadyCallback: (() -> Unit)? = null

    fun configure(wsConfig: WsConfig) {
        wsConfig.onConnect { ctx ->
            val id = nextId.incrementAndGet()
            wsConnections[id] = ctx
            ctx.session.idleTimeout = Duration.of(60, java.time.temporal.ChronoUnit.SECONDS)
            println("Client connected: $id")
        }

        wsConfig.onMessage { ctx ->
            val message = ctx.message()
            println("Received WebSocket message: $message")

            if (message.contains("\"ping\":true")) {
                println("Frontend is ready, initializing listeners")
                onFrontendReadyCallback?.invoke()
            }
        }

        wsConfig.onClose { ctx ->
            wsConnections.entries.removeIf { it.value === ctx }
        }
    }

    fun broadcastToAll(message: String) {
        wsConnections.values.forEach { ctx ->
            ctx.send(message)
        }
    }

    fun setOnFrontendReadyCallback(callback: () -> Unit) {
        this.onFrontendReadyCallback = callback
    }
}