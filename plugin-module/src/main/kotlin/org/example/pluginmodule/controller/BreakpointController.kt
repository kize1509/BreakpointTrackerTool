package org.example.pluginmodule.controller

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import org.example.pluginmodule.model.BreakpointResponse
import org.example.pluginmodule.serialization.JacksonJsonConverter
import org.example.pluginmodule.serialization.JsonConverter
import org.example.pluginmodule.services.BreakpointService
import org.example.pluginmodule.services.BreakpointServiceImpl


class BreakpointController(
    private val project: Project,
    private val updateUI: (String) -> Unit
) : Disposable {

    private val jsonConverter: JsonConverter = JacksonJsonConverter()
    private val breakpointService: BreakpointService

    init {
        breakpointService = BreakpointServiceImpl(project) { breakpointData ->
            handleBreakpointUpdate(breakpointData)
        }
        breakpointService.initialize()
    }

    private fun handleBreakpointUpdate(breakpointData: BreakpointResponse) {
        try {
            val jsonData = jsonConverter.toJson(breakpointData)
            updateUI(jsonData)
        } catch (e: Exception) {
            val errorJson = jsonConverter.toErrorJson(e)
            updateUI(errorJson)
        }
    }

    override fun dispose() {
        breakpointService.dispose()
    }
}