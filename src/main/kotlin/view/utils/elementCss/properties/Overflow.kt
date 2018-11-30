package view.utils.elementCss.properties

import org.w3c.dom.HTMLElement

class Overflow(var axis: OverflowAxis, var value: Value?): CssProperty {

    enum class OverflowAxis {
        X,
        Y
    }

    enum class Value: CssValue {
        SCROLL,
        AUTO,
        HIDDEN,
        VISIBLE,
        INITIAL,
        INHERIT
    }

    constructor(axis: OverflowAxis): this(axis, null)

    override fun applyToStyle(element: HTMLElement) {
        if (value != null) {
            if (axis == OverflowAxis.X) {
                element.style.overflowX = value!!.cssString()
            } else {
                element.style.overflowY = value!!.cssString()
            }
        }
    }
}