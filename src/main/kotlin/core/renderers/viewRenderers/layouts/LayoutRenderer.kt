package core.renderers.viewRenderers.layouts

import core.renderers.ViewRenderer
import core.renderers.viewRenderers.AbstractViewRenderer
import core.views.View
import core.views.layouts.Layout
import di.inject
import org.w3c.dom.HTMLElement
import utils.extensions.children
import utils.extensions.findChild
import kotlin.browser.document

abstract class LayoutRenderer<L: Layout>(
        view: L,
        element: HTMLElement,
        reRendering: Boolean = true
): AbstractViewRenderer<L>(view, element, reRendering) {

    constructor(view: L): this(view, document.createElement("div") as HTMLElement, false)

    override fun buildElement() {
        renderChildren()
        removeChildren()
    }

    protected open fun beforeChildRenders(child: View) {}
    protected open fun afterChildRenders(child: View, childElement: HTMLElement) {}

    protected open fun beforeChildIsRemoved(child: View, childElement: HTMLElement) {}
    protected open fun afterChildIsRemoved(child: View, childElement: HTMLElement) {}

    protected open fun renderChildren() {
        getViewsToBeRendered().map { id -> view.find(id) }.forEach { c: View ->
            val childRenderer by inject<ViewRenderer<View>, View>(c.name, c)
            beforeChildRenders(c)
            val child = childRenderer.renderView()
            element.appendChild(child)
            afterChildRenders(c, child)
        }
    }

    protected open fun removeChildren() {
        getViewsToBeRemoved().forEach { id ->
            println(id)
            val child = view.find(id)
            val element = element.findChild(id)
            beforeChildIsRemoved(child, element)
            element.removeChild(element)
            afterChildIsRemoved(child, element)
        }
    }

    protected open fun getViewsToBeRendered(): List<Int> {
        val renderedIds = element.children().map { c -> c.id.toInt() }
        val childIds = view.children().map { c -> c.id }
        return childIds - renderedIds
    }

    protected open fun getViewsToBeRemoved(): List<Int> {
        val renderedIds = element.children().map { c -> c.id.toInt() }
        val childIds = view.children().map { c -> c.id }
        return renderedIds - childIds
    }
}