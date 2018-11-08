package core.renderers.viewRenderers.layouts

import core.views.View
import core.views.layouts.LinearLayout
import org.w3c.dom.HTMLElement
import utils.elementCss.ElementCss
import utils.elementCss.properties.Display
import utils.elementCss.properties.FlexSettings
import utils.elementCss.properties.Number
import utils.extensions.applyCss
import utils.mapBased.keys.HasKeys
import utils.mapBased.keys.delegates.nullable.BoolRWKey
import kotlin.browser.document

class LinearLayoutRenderer(
        view: LinearLayout,
        element: HTMLElement,
        reRendering: Boolean = true
): LayoutRenderer<LinearLayout>(view, element, reRendering) {

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
        super.buildElement()
    }

    override fun childCreated(child: View, childElement: HTMLElement): Boolean {
        if (view.direction == LinearLayout.Direction.VERTICAL) {
            element.appendChild(document.createElement("br"))
        }
        return true
    }
}
