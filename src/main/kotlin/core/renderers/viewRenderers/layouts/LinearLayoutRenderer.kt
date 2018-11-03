package core.renderers.viewRenderers.layouts

import core.views.View
import core.views.layouts.LinearLayout
import org.w3c.dom.HTMLElement
import utils.ElementCss
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
        css.display = ElementCss.Display.FLEX
        css.horizontalFlexDirection = view.direction == LinearLayout.Direction.HORIZONTAL
        val viewExtras = view.webExtras
        if (viewExtras != null) {
            layoutConfig.keys = viewExtras.keys
        }
        super.buildElement()
    }
}
