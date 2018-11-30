package view.core.renderers

import view.core.views.View
import org.w3c.dom.HTMLElement

/**
 * Defines an interface for classes that render views.
 */
interface ViewRenderer<V: View> {

    /**
     * Builds a DOM element corresponding to the view.
     *
     * @return The HTML element corresponding to the view.
     */
    fun renderView(): HTMLElement
}