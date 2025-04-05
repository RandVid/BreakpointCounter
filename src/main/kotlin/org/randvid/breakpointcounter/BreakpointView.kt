package org.randvid.breakpointcounter

import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpointListener

class BreakpointView(private val project: Project) {
    val jcefBrowser = JBCefBrowser()
    val component = jcefBrowser.component

    init {
        updateHtml()
        project.messageBus.connect().subscribe(
            XBreakpointListener.TOPIC,
            object : XBreakpointListener<XBreakpoint<*>> {
                override fun breakpointAdded(breakpoint: XBreakpoint<*>) = updateHtml()
                override fun breakpointRemoved(breakpoint: XBreakpoint<*>) = updateHtml()
                override fun breakpointChanged(breakpoint: XBreakpoint<*>) = updateHtml()
            }
        )
    }

    private fun updateHtml() {
        val breakpoints = XDebuggerManager.getInstance(project).breakpointManager.allBreakpoints.filter {
            it.sourcePosition?.file != null
        }
        println("[DEBUG] All breakpoints: ${breakpoints.joinToString("\n")}")
        val colorsScheme = EditorColorsManager.getInstance().globalScheme
        val foreground = "#ffffff"
        val fontFamily = colorsScheme.editorFontName
        val html = """
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
        jcefBrowser.loadHTML(html)
    }
}
