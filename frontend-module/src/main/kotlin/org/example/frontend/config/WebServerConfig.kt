package org.example.frontend.config

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location

class WebServerConfig(private val port: Int) {
    fun configureJavalin(): Javalin {
        return Javalin.create { config ->
            config.staticFiles.add { staticFiles ->
                staticFiles.directory = "/static"
                staticFiles.location = Location.CLASSPATH
            }
        }
    }

    fun determinePort(): Int {
        return if (port > 0) port else findAvailablePort()
    }

    private fun findAvailablePort(): Int {
        return 8080 // In real implementation, this would scan for an available port
    }
}