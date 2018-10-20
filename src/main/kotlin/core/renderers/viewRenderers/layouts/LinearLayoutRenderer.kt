package core.renderers.viewRenderers.layouts

import core.renderers.viewRenderers.ViewRenderer
import core.views.Dimension
import core.views.View
import core.views.layouts.LinearLayout
import di.inject
import org.w3c.dom.HTMLElement
import utils.ElementCss
import utils.getGridSize
import utils.mapBased.keys.HasKeys
import utils.mapBased.keys.delegates.nullable.BoolRWKey
import kotlin.browser.document


class LinearLayoutRenderer: ViewRenderer<LinearLayout>() {

    class LinearLayoutConfig: HasKeys() {

        override var keys: MutableMap<String, Any?> = mutableMapOf()

        var isCard by BoolRWKey
    }

    override val element = document.createElement("div") as HTMLElement

    // TODO
    var layoutConfig = LinearLayoutConfig()

    override fun buildElement(view: LinearLayout) {
        val viewExtras = view.webExtras
        if (viewExtras != null) {
            layoutConfig.keys = viewExtras.keys
        }

        renderChildren(view, element)
    }

    private fun renderChildren(layout: LinearLayout, element: HTMLElement) {
        layout.children().forEach { c: View ->
            val childRenderer by inject<ViewRenderer<View>>(c::class.simpleName)
            val child = childRenderer.renderView(c)

            val classes: MutableSet<String> = mutableSetOf()
            val css = ElementCss()

            val width: Pair<Double, ElementCss.DimensionUnit>? = when (Dimension.type(c.width)) {
                Dimension.Type.WRAP_CONTENT -> null
                Dimension.Type.RELATIVE ->  c.width * 100 to ElementCss.DimensionUnit.RELATIVE
                Dimension.Type.EXPLICIT -> c.width to ElementCss.DimensionUnit.PX
            }
            if (width != null) {
                css.width = width
            }

            val height: Pair<Double, ElementCss.DimensionUnit>? = when (Dimension.type(c.height)) {
                Dimension.Type.WRAP_CONTENT -> null
                Dimension.Type.RELATIVE -> c.height * 100 to ElementCss.DimensionUnit.RELATIVE
                Dimension.Type.EXPLICIT -> c.height to ElementCss.DimensionUnit.PX
            }
            if (height != null) {
                css.height = height
            }

            child.className += " " + classes.joinToString(" ")
            css.applyTo(child)
            element.appendChild(child)

            if (layout.direction == LinearLayout.Direction.VERTICAL) {
                element.append(document.createElement("br"))
            }
        }
    }
}