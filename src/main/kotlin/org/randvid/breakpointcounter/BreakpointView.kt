package org.randvid.breakpointcounter

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import java.net.ServerSocket
import javax.swing.JComponent

/*
 * BreakpointView
 * Creates the server with breakpoint counter page
 * Accesses and updates that page when something happens to the breakpoints
 */
class BreakpointView(project: Project) : Disposable {
    private val jcefBrowser: JBCefBrowser
    val component: JComponent
    val server: BreakpointServerManager
    val port: Int = ServerSocket(0).use { socket ->
        socket.localPort
    }
    val address: String = "http://localhost:$port/breakpoints"

    override fun dispose() {
        server.dispose()
    }

    init {
        server = BreakpointServerManager(project, port)
        jcefBrowser = JBCefBrowser(address)
        component = jcefBrowser.component
        println(port)

        project.messageBus.connect().subscribe(
            XBreakpointListener.TOPIC,
            object : XBreakpointListener<XBreakpoint<*>> {
                override fun breakpointAdded(breakpoint: XBreakpoint<*>) =
                    jcefBrowser.loadURL(address)
                override fun breakpointRemoved(breakpoint: XBreakpoint<*>) =
                    jcefBrowser.loadURL(address)
                override fun breakpointChanged(breakpoint: XBreakpoint<*>) =
                    jcefBrowser.loadURL(address)
            }
        )
    }
}
