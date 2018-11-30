package view.core.renderers.viewRenderers.layouts

import view.core.views.View
import view.core.views.layouts.LinearLayout
import org.w3c.dom.HTMLElement
import view.utils.elementCss.properties.Display
import view.utils.extensions.applyCss
import view.utils.mapBased.keys.HasKeys
import view.utils.mapBased.keys.delegates.nullable.BoolRWKey
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
            childElement.applyCss { display = Display.BLOCK }
        }
        return true
    }
}
