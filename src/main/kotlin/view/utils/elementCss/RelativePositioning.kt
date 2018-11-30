package view.utils.elementCss

import view.utils.elementCss.properties.CssDimen
import view.utils.elementCss.properties.CssUnit

/**
 * Contains utility methods for positioning DOM elements in relation to each other.
 */
object RelativePositioning {

    fun alignToParentTop(childCss: ElementCss) {
        childCss.top.set(CssDimen.ZERO)
    }

    fun alignToParentBottom(childCss: ElementCss) {
        childCss.bottom.set(CssDimen.ZERO)
    }

    fun alignToParentStart(childCss: ElementCss) {
        childCss.start.set(CssDimen.ZERO)
    }

    fun alignToParentEnd(childCss: ElementCss) {
        childCss.end.set(CssDimen.ZERO)
    }

    fun centerHorizontal(childCss: ElementCss) {
        childCss.apply {
            start.set(50.0 to CssUnit.RELATIVE)
            transformation.translateX(-50.0 to CssUnit.RELATIVE)

            marginStart.set(CssDimen.ZERO)
            marginEnd.set(CssDimen.ZERO)
        }
    }

    fun centerVertical(childCss: ElementCss) {
        childCss.apply {
            top.set(50.0 to CssUnit.RELATIVE)
            transformation.translateY(-50.0 to CssUnit.RELATIVE)

            marginTop.set(CssDimen.ZERO)
            marginBottom.set(CssDimen.ZERO)
        }
    }
}