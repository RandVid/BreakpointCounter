package org.randvid.breakpointcounter

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import javax.swing.JComponent

class BreakpointView(project: Project) : Disposable {
    private val jcefBrowser: JBCefBrowser
    val component: JComponent
    val server: BreakpointServerManager

    override fun dispose() {
        server.dispose()
    }

    init {
        server = BreakpointServerManager(project)
        jcefBrowser = JBCefBrowser("http://localhost:15050/breakpoints")
        component = jcefBrowser.component

        project.messageBus.connect().subscribe(
            XBreakpointListener.TOPIC,
            object : XBreakpointListener<XBreakpoint<*>> {
                override fun breakpointAdded(breakpoint: XBreakpoint<*>) =
                    jcefBrowser.loadURL("http://localhost:15050/breakpoints") // fixed port here
                override fun breakpointRemoved(breakpoint: XBreakpoint<*>) =
                    jcefBrowser.loadURL("http://localhost:15050/breakpoints")
                override fun breakpointChanged(breakpoint: XBreakpoint<*>) =
                    jcefBrowser.loadURL("http://localhost:15050/breakpoints")
            }
        )
    }
}
