package core.renderers.viewRenderers.layouts

import core.renderers.ViewRenderer
import core.renderers.viewRenderers.AbstractViewRenderer
import core.views.View
import core.views.layouts.LinearLayout
import di.inject
import org.w3c.dom.HTMLElement
import utils.extensions.children
import utils.extensions.findChild
import utils.mapBased.keys.HasKeys
import utils.mapBased.keys.delegates.nullable.BoolRWKey
import kotlin.browser.document

class LinearLayoutRenderer(
        view: LinearLayout,
        element: HTMLElement,
        reRendering: Boolean = true
): AbstractViewRenderer<LinearLayout>(view, element, reRendering) {

    class LinearLayoutConfig: HasKeys() {

        override var keys: MutableMap<String, Any?> = mutableMapOf()

        var isCard by BoolRWKey
    }

    // TODO
    var layoutConfig = LinearLayoutConfig()

    constructor(view: LinearLayout): this(view, document.createElement("div") as HTMLElement, false)

    override fun buildElement() {
        val viewExtras = view.webExtras
        if (viewExtras != null) {
            layoutConfig.keys = viewExtras.keys
        }

        renderChildren()
    }

    private fun renderChildren() {
        val renderedIds = element.children().map { c -> c.id.toInt() }.toSet()
        val childIds = view.children().map { c -> c.id }.toSet()
        println("Rendered Ids: " + renderedIds)
        println("Child Ids: " + childIds)

        val viewsToBeRendered = childIds - renderedIds
        val viewsToBeRemoved = renderedIds - childIds
        println("To be rendered: " + viewsToBeRendered)
        println("To be removed: " + viewsToBeRemoved)

        viewsToBeRendered.map { id -> view.find(id) }.forEach { c: View ->
            val childRenderer by inject<ViewRenderer<View>, View>(c.name, c)
            val child = childRenderer.renderView()
            element.appendChild(child)
            if (view.direction == LinearLayout.Direction.VERTICAL) {
                element.append(document.createElement("br"))
            }
        }

        viewsToBeRemoved.forEach { id ->
            element.removeChild(element.findChild(id))
        }
    }
}