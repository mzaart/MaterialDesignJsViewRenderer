package core.renderers.viewRenderers.layouts

import core.renderers.ViewRenderer
import core.renderers.viewRenderers.AbstractViewRenderer
import core.views.View
import core.views.layouts.Layout
import di.inject
import org.w3c.dom.HTMLElement
import utils.elementCss.properties.Overflow
import utils.elementCss.properties.WhiteSpace
import utils.extensions.viewChildren
import kotlin.browser.document

abstract class LayoutRenderer<L: Layout>(
        view: L,
        element: HTMLElement,
        reRendering: Boolean = true
): AbstractViewRenderer<L>(view, element, reRendering) {

    constructor(view: L): this(view, document.createElement("div") as HTMLElement, false)

    override fun buildElement() {
        applyScroll()
        renderChildren()
        removeChildren()
    }

    protected open fun childCreated(child: View, childElement: HTMLElement): Boolean = true

    protected open fun beforeChildIsRemoved(childElement: HTMLElement): Boolean = true

    protected open fun renderChildren() {
        getViewsToBeRendered().map { id -> view.find(id) }.forEach { c: View ->
            val childRenderer by inject<ViewRenderer<View>, View>(c.name, c)
            val child = childRenderer.renderView()
            val shouldAdd = childCreated(c, child)
            if (shouldAdd) {
                element.appendChild(child)
            }
        }
    }

    protected open fun removeChildren() {
        getViewsToBeRemoved().forEach { id ->
            val element = getChildElements().first { it.id.toInt() == id }
            val shouldRemove = beforeChildIsRemoved(element)
            if (shouldRemove) {
                element.removeChild(element)
            }
        }
    }

    protected open fun getViewsToBeRendered(): List<Int> {
        val renderedIds = getChildElements().map { c -> c.id.toInt() }
        val childIds = view.children().map { c -> c.id }
        println("To be rendered: ${childIds - renderedIds}")
        return childIds - renderedIds
    }

    protected open fun getViewsToBeRemoved(): List<Int> {
        val renderedIds = getChildElements().map { c -> c.id.toInt() }
        val childIds = view.children().map { c -> c.id }
        println("To be removed: ${renderedIds - childIds}")
        return renderedIds - childIds
    }

    protected open fun getChildElements(): List<HTMLElement> = element.viewChildren()

    private fun applyScroll() {
        css.whiteSpace = WhiteSpace.NOWRAP
        css.overflowX.value = if (view.scrollX) Overflow.Value.AUTO else Overflow.Value.HIDDEN
        css.overflowY.value = if (view.scrollY) Overflow.Value.AUTO else Overflow.Value.HIDDEN
    }
}