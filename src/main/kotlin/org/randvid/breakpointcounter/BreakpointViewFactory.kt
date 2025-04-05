package org.randvid.breakpointcounter

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class BreakpointViewFactory : ToolWindowFactory {

    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow
    ) {

        val contentManager = toolWindow.contentManager
        val view = BreakpointView(project)
        val content = contentManager.factory.createContent(view.component, "", false)
        contentManager.addContent(content)
    }
}