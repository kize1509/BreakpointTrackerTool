package org.example.pluginmodule.ui
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefBrowser
import org.example.frontend.BreakpointServer
import org.example.pluginmodule.controller.BreakpointController
import com.intellij.openapi.util.Key

class BreakpointToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val viewManager = BreakpointViewManager(project, toolWindow)
        viewManager.initialize()
    }

    private class BreakpointViewManager(
        private val project: Project,
        private val toolWindow: ToolWindow
    ) : Disposable {

        private val server = BreakpointServer()
        private lateinit var browser: JBCefBrowser
        private var controller: BreakpointController? = null

        fun initialize() {
            val serverPort = server.start()
            val serverUrl = server.getServerUrl()

            browser = JBCefBrowser(serverUrl)

            val contentFactory = ContentFactory.getInstance()
            val content = contentFactory.createContent(browser.component, "Breakpoints", false)

            server.setOnFrontendReadyCallback {
                initializeController()
            }

            toolWindow.contentManager.addContent(content)

            Disposer.register(content, this)
        }

        private fun initializeController() {
            controller = BreakpointController(project) { breakpointData ->
                server.updateBreakpoints(breakpointData)
            }

            toolWindow.contentManager.contents.firstOrNull()?.putUserData(
                Key.create("breakpointController"),
                controller
            )

            println("BreakpointController initialized after frontend ready signal")
        }

        override fun dispose() {
            server.stop()
            controller?.dispose()
        }
    }
}