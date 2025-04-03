package org.example.pluginmodule.model


data class BreakpointInfo(
    val fileName: String,
    val line: Int,
    val enabled: Boolean,
    val type: String
)

data class BreakpointResponse(
    val breakpoints: List<BreakpointInfo>,
    val timestamp: Long,
    val count: Int
)