package core.renderers.viewRenderers

import core.renderers.ViewRenderer
import core.views.Dimension
import core.views.Theme
import core.views.View
import di.inject
import org.w3c.dom.HTMLElement
import utils.ElementCss
import utils.extensions.nonNull
import kotlin.browser.document


abstract class AbstractViewRenderer<V: View>(
        protected val view: V,
        protected val element: HTMLElement,
        protected val reRendering: Boolean = false
): ViewRenderer<V> {

    protected val css = ElementCss()

    override fun renderView(): HTMLElement {
        applyCommonViewAttrs()
        buildElement()
        css.applyTo(element)
        return element
    }

    protected abstract fun buildElement()

    private fun applyCommonViewAttrs() {
        // set id
        element.id = view.id.toString()

        // apply theme
        if (!reRendering) {
            val theme by inject<Theme>()
            theme.applyTo(view)
        }

        handleVisibility()
        handleWidth()
        handleHeight()
        handleMargins()
        handlePadding()
        handleDisabled()
        handleOnClick()
    }

    private fun handleVisibility() {
        if (view.visibility == View.Visibility.GONE) {
            css.display = ElementCss.Display.None
        } else {
            element.style.removeProperty("display")
            css.visibility = when (view.visibility) {
                View.Visibility.VISIBLE -> ElementCss.Visibility.VISIBLE
                View.Visibility.INVISIBLE -> ElementCss.Visibility.HIDDEN
                else -> throw IllegalStateException()
            }
        }
    }

    private fun handleWidth() {
        val width = when (Dimension.type(view.width)) {
            Dimension.Type.WRAP_CONTENT -> {
                element.style.removeProperty("width")
                null
            }
            Dimension.Type.RELATIVE ->  view.width * 100 to ElementCss.DimensionUnit.RELATIVE
            Dimension.Type.EXPLICIT -> view.width to ElementCss.DimensionUnit.PX
        }
        if (width != null) {
            css.width = width
        }
    }

    private fun handleHeight() {
        val height = when (Dimension.type(view.height)) {
            Dimension.Type.WRAP_CONTENT -> {
                element.style.removeProperty("height")
                null
            }
            Dimension.Type.RELATIVE ->  view.height * 100 to ElementCss.DimensionUnit.RELATIVE
            Dimension.Type.EXPLICIT -> view.height to ElementCss.DimensionUnit.PX
        }
        if (height != null) {
            css.height = height
        }
    }
    
    private fun handleMargins() {
        css.apply {
            view.marginStart.nonNull { marginStart = it to ElementCss . DimensionUnit.PX }
            view.marginEnd.nonNull { marginEnd = it to ElementCss.DimensionUnit.PX }
            view.marginTop.nonNull { marginTop = it to ElementCss.DimensionUnit.PX }
            view.marginBottom.nonNull { marginBottom = it to ElementCss.DimensionUnit.PX }
        }
    }

    private fun handlePadding() {
        css.apply {
            view.paddingStart.nonNull { paddingStart = it to ElementCss . DimensionUnit.PX }
            view.paddingEnd.nonNull { paddingEnd = it to ElementCss.DimensionUnit.PX }
            view.paddingTop.nonNull { paddingTop = it to ElementCss.DimensionUnit.PX }
            view.paddingBottom.nonNull { paddingBottom = it to ElementCss.DimensionUnit.PX }
        }
    }
    
    private fun handleDisabled() {
        if (view.disabled) {
            val attr = document.createAttribute("disabled")
            element.setAttributeNode(attr)
        } else {
            element.removeAttribute("disabled")
        }
    }

    private fun handleOnClick() {
        if (view.onClickListener != null) {
            element.onclick = { view.onClickListener!!(view) }
        } else {
            element.onclick = null
        }
    }

    private fun handleOnLongClick() {
        throw TODO()
    }

    private fun handleOnResize() {
        if (view.onResize != null) {
            element.onresize = { view.onResize!!(view) }
        } else {
            element.onresize = null
        }
    }
}
