package core.renderers

import core.views.View
import org.w3c.dom.HTMLElement

interface ViewRenderer<V: View> {

    fun renderView(): HTMLElement
}