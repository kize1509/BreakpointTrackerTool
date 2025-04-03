package org.example.frontend

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.staticfiles.Location
import io.javalin.websocket.WsContext
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class BreakpointServer(private val port: Int = 0) {
    private val app = Javalin.create { config ->
        config.staticFiles.add { staticFiles ->
            staticFiles.directory = "/static"
            staticFiles.location = Location.CLASSPATH
        }
    }

    private val wsConnections = ConcurrentHashMap<Int, WsContext>()
    private val nextId = AtomicInteger(0)
    private var actualPort: Int = 0

    private var onFrontendReadyCallback: (() -> Unit)? = null

    fun start(): Int {
        app.ws("/breakpoints") { ws ->
            ws.onConnect { ctx ->
                val id = nextId.incrementAndGet()
                wsConnections[id] = ctx
                ctx.session.idleTimeout = Duration.of(60, java.time.temporal.ChronoUnit.SECONDS)
                println("Client connected: $id")
            }

            ws.onMessage { ctx ->
                val message = ctx.message()
                println("Received WebSocket message: $message")

                if (message.contains("\"ping\":true")) {
                    println("Frontend is ready, initializing listeners")
                    onFrontendReadyCallback?.invoke()
                }
            }

            ws.onClose { ctx ->
                wsConnections.entries.removeIf { it.value === ctx }
            }
        }

        app.get("/health") { ctx ->
            ctx.result("Breakpoint server running")
        }

        actualPort = if (port > 0) port else findAvailablePort()
        app.start(actualPort)
        return actualPort
    }

    fun stop() {
        app.stop()
    }

    fun updateBreakpoints(breakpointData: String) {
        wsConnections.values.forEach { ctx ->
            ctx.send(breakpointData)
        }
    }

    fun setOnFrontendReadyCallback(callback: () -> Unit) {
        this.onFrontendReadyCallback = callback
    }

    private fun findAvailablePort(): Int {
        return 8080
    }

    fun getServerUrl(): String {
        return "http://localhost:$actualPort"
    }
}