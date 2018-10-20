package core.renderers.viewRenderers

import core.views.Theme
import core.views.View
import di.inject
import org.w3c.dom.HTMLElement
import utils.ElementCss
import kotlin.browser.document


abstract class ViewRenderer<V: View> {

    protected abstract val element: HTMLElement

    fun renderView(view: V): HTMLElement {
        applyCommonViewAttrs(view)
        buildElement(view)
        return element
    }

    protected abstract fun buildElement(view: V)

    private fun applyCommonViewAttrs(view: V) {
        // apply theme
        val theme by inject<Theme>()
        theme.applyTo(view)

        // apply css
        val style = ElementCss().apply {
            if (view.visibility == View.Visibility.GONE) {
                display = ElementCss.Display.None
            } else {
                visibility = when (view.visibility) {
                    View.Visibility.VISIBLE -> ElementCss.Visibility.VISIBLE
                    View.Visibility.INVISIBLE -> ElementCss.Visibility.HIDDEN
                    else -> throw IllegalStateException()
                }
            }

            marginStart = view.marginStart to ElementCss.DimensionUnit.PX
            marginEnd = view.marginEnd to ElementCss.DimensionUnit.PX
            marginTop = view.marginTop to ElementCss.DimensionUnit.PX
            marginBottom = view.marginBottom to ElementCss.DimensionUnit.PX
        }
        style.applyTo(element)

        if (view.disabled) {
            val attr = document.createAttribute("disabled")
            element.setAttributeNode(attr)
        }

        // apply listeners
        element.onclick = { view.onClickListener(view) }
        // todo implement on long click
        element.onresize = { view.onClickListener(view) }
    }
}