package org.example.pluginmodule

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBusConnection
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode

class BreakpointListener(
    private val project: Project,
    private val updateUI: (String) -> Unit
) : XBreakpointListener<XBreakpoint<*>>, Disposable {

    private val connection: MessageBusConnection
    private val objectMapper = ObjectMapper()

    init {
        connection = project.messageBus.connect()

        connection.subscribe(XBreakpointListener.TOPIC, this)

        updateBreakpoints()
    }

    override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
        println("Breakpoint added: ${breakpoint.type.id}")
        updateBreakpoints()
    }

    override fun breakpointRemoved(breakpoint: XBreakpoint<*>) {
        println("Breakpoint removed: ${breakpoint.type.id}")
        updateBreakpoints()
    }

    override fun breakpointChanged(breakpoint: XBreakpoint<*>) {
        println("Breakpoint changed: ${breakpoint.type.id}")
        updateBreakpoints()
    }

    private fun updateBreakpoints() {
        try {
            val breakpointManager = XDebuggerManager.getInstance(project).breakpointManager
            val breakpoints = breakpointManager.allBreakpoints

            val rootNode = objectMapper.createObjectNode()
            val breakpointsArray = objectMapper.createArrayNode()

            breakpoints.forEach { breakpoint ->
                if (breakpoint.sourcePosition==null) {
                    println("Skipping breakpoint with null source position")
                }else{
                    val bpNode = objectMapper.createObjectNode()

                    val sourcePosition = breakpoint.sourcePosition
                    val fileName = sourcePosition?.file?.name ?: "Unknown"
                    val line = if (sourcePosition != null) sourcePosition.line + 1 else -1

                    bpNode.put("fileName", fileName)
                    bpNode.put("line", line)
                    bpNode.put("enabled", breakpoint.isEnabled)
                    bpNode.put("type", breakpoint.type.id.split(".").last())

                    breakpointsArray.add(bpNode)
                }
            }
            rootNode.set<ArrayNode>("breakpoints", breakpointsArray)
            rootNode.put("timestamp", System.currentTimeMillis())
            rootNode.put("count", breakpoints.size)

            // Convert to JSON string and send to UI
            val jsonData = objectMapper.writeValueAsString(rootNode)
            updateUI(jsonData)

        } catch (e: Exception) {
            val errorJson = """
                {"error": true, "message": "${e.message}", "stackTrace": "${e.stackTraceToString()}"}
            """.trimIndent()
            updateUI(errorJson)
        }
    }

    override fun dispose() {
        connection.disconnect()
    }

}