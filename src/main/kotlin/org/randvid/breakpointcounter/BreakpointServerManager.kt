package org.randvid.breakpointcounter

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.ui.ColorUtil
import com.intellij.xdebugger.XDebuggerManager
import com.jetbrains.rd.util.printlnError
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

class BreakpointServerManager(private val project: Project) : Disposable {
    private val server: Server = Server(15050)
    private val srcPath = "/"

    init {
        val context = ServletContextHandler(ServletContextHandler.NO_SESSIONS)
        context.contextPath = srcPath
        server.handler = context

        context.addServlet(ServletHolder(object : HttpServlet() {
            override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
                resp.contentType = "text/html"
                resp.status = HttpServletResponse.SC_OK
                val html = updateHtml()
                resp.writer.write(html)
            }
        }), "/breakpoints")

        ApplicationManager.getApplication().executeOnPooledThread {
            server.start()
        }
    }

    private fun updateHtml(): String {
        return ApplicationManager.getApplication().runReadAction<String> {
            val breakpoints = XDebuggerManager.getInstance(project).breakpointManager.allBreakpoints.filter {
                it.sourcePosition?.file != null
            }
            val colorScheme = EditorColorsManager.getInstance().globalScheme
            val foreground = colorScheme.defaultForeground.let { color ->
                "#${ColorUtil.toHex(color)}"
            }
            val fontFamily = colorScheme.editorFontName
            """
                <html>
                    <body style=\\\";color:$foreground;font-family:$fontFamily;>
                        <h2>Total Breakpoints: ${breakpoints.size}</h2>
                        <ul> ${
                    breakpoints.joinToString("\n") {
                        "<li>${it.sourcePosition?.file?.path ?: "unknown"}: ${it.sourcePosition?.line ?: "unknown"}</li>"
                    }
                } </ul>
                    </body>
                </html>
            """
        }
    }

    override fun dispose() {
        try {
            if (server.isStarted) {
                server.stop()
            }
        } catch (e: Exception) {
            printlnError("Error stopping server: ${e.message}")
        }
    }
}