package core.renderers.viewRenderers.layouts

import core.renderers.ViewRenderer
import core.renderers.viewRenderers.AbstractViewRenderer
import core.views.View
import core.views.layouts.Layout
import di.inject
import org.w3c.dom.HTMLElement
import utils.elementCss.properties.Display
import utils.elementCss.properties.Overflow
import utils.elementCss.properties.WhiteSpace
import utils.extensions.viewChildren
import kotlin.browser.document

/**
 * This is the base class for renderers that render layout views.
 *
 * The procedure for rendering layout views goes as follows:
 *  1. The [AbstractViewRenderer] applies common view attributes.
 *  2. This class applies scrolling to the element
 *  3. Renders any newly added children or all children if the view is being rendered for the first time.
 *  4. Removes any child UI components that were removed from the layout view
 *
 *  Listener functions for child view rendering/removal so that subclassing layout renderers can apply layout specific
 *  positioning logic.
 *
 */
abstract class LayoutRenderer<L: Layout>(
        view: L,
        element: HTMLElement,
        reRendering: Boolean = true
): AbstractViewRenderer<L>(view, element, reRendering) {

    /**
     * This constructor is used when rendering the view for the first time.
     */
    constructor(view: L): this(view, document.createElement("div") as HTMLElement, false)

    override fun buildElement() {
        css.display = Display.INLINE_BLOCK
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
        return childIds - renderedIds
    }

    protected open fun getViewsToBeRemoved(): List<Int> {
        val renderedIds = getChildElements().map { c -> c.id.toInt() }
        val childIds = view.children().map { c -> c.id }
        return renderedIds - childIds
    }

    protected open fun getChildElements(): List<HTMLElement> = element.viewChildren()

    private fun applyScroll() {
        css.whiteSpace = WhiteSpace.NOWRAP
        css.overflowX.value = if (view.scrollX) Overflow.Value.AUTO else Overflow.Value.HIDDEN
        css.overflowY.value = if (view.scrollY) Overflow.Value.AUTO else Overflow.Value.HIDDEN
    }
}