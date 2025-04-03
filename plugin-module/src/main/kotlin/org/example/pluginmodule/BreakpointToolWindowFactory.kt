package org.example.pluginmodule

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.content.ContentFactory
import org.example.frontend.BreakpointServer
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import org.example.pluginmodule.BreakpointListener

class BreakpointToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val server = BreakpointServer()
        val serverPort = server.start()
        val serverUrl = server.getServerUrl()

        val browser = JBCefBrowser(serverUrl)
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(browser.component, "Breakpoints", false)

        var listener: BreakpointListener? = null

        server.setOnFrontendReadyCallback {
            listener = BreakpointListener(project) { breakpointData ->
                server.updateBreakpoints(breakpointData)
            }

            content.putUserData(
                com.intellij.openapi.util.Key.create("breakpointListener"),
                listener
            )

            println("BreakpointListener initialized after frontend ready signal")
        }

        // Add content to the tool window
        toolWindow.contentManager.addContent(content)

        val disposable = Disposable {
            server.stop()
            listener?.dispose()
        }

        Disposer.register(content, disposable)
    }
}