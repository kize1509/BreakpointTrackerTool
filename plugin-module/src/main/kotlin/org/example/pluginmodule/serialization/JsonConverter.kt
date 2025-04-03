package org.example.pluginmodule.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.pluginmodule.model.BreakpointResponse

interface JsonConverter {
    fun toJson(data: Any): String
    fun toErrorJson(exception: Exception): String
}

class JacksonJsonConverter : JsonConverter {
    private val objectMapper = ObjectMapper()

    override fun toJson(data: Any): String {
        return objectMapper.writeValueAsString(data)
    }

    override fun toErrorJson(exception: Exception): String {
        val errorNode = objectMapper.createObjectNode()
        errorNode.put("error", true)
        errorNode.put("message", exception.message)
        errorNode.put("stackTrace", exception.stackTraceToString())
        return objectMapper.writeValueAsString(errorNode)
    }
}