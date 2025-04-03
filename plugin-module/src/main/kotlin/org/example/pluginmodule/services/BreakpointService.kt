package org.example.pluginmodule.services

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBusConnection
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import org.example.pluginmodule.model.BreakpointInfo
import org.example.pluginmodule.model.BreakpointResponse

interface BreakpointService : Disposable {
    fun initialize()
    fun getAllBreakpoints(): BreakpointResponse
}

class BreakpointServiceImpl(
    private val project: Project,
    private val onBreakpointsChanged: (BreakpointResponse) -> Unit
) : BreakpointService, XBreakpointListener<XBreakpoint<*>> {

    private val connection: MessageBusConnection = project.messageBus.connect()

    override fun initialize() {
        connection.subscribe(XBreakpointListener.TOPIC, this)
        notifyBreakpointsChanged()
    }

    override fun getAllBreakpoints(): BreakpointResponse {
        val breakpointManager = XDebuggerManager.getInstance(project).breakpointManager
        val breakpoints = breakpointManager.allBreakpoints
        val breakpointInfos = breakpoints
            .filter { it.sourcePosition != null }
            .map { breakpoint ->
                val sourcePosition = breakpoint.sourcePosition!!
                val fileName = sourcePosition.file.name
                val line = sourcePosition.line + 1

                BreakpointInfo(
                    fileName = fileName,
                    line = line,
                    enabled = breakpoint.isEnabled,
                    type = breakpoint.type.id.split(".").last()
                )
            }

        return BreakpointResponse(
            breakpoints = breakpointInfos,
            timestamp = System.currentTimeMillis(),
            count = breakpointInfos.size
        )
    }

    private fun notifyBreakpointsChanged() {
        try {
            val breakpointData = getAllBreakpoints()
            onBreakpointsChanged(breakpointData)
        } catch (e: Exception) {
            println("Error updating breakpoints: ${e.message}")
        }
    }

    override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
        println("Breakpoint added: ${breakpoint.type.id}")
        notifyBreakpointsChanged()
    }

    override fun breakpointRemoved(breakpoint: XBreakpoint<*>) {
        println("Breakpoint removed: ${breakpoint.type.id}")
        notifyBreakpointsChanged()
    }

    override fun breakpointChanged(breakpoint: XBreakpoint<*>) {
        println("Breakpoint changed: ${breakpoint.type.id}")
        notifyBreakpointsChanged()
    }

    override fun dispose() {
        connection.disconnect()
    }
}