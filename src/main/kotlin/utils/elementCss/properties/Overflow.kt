package utils.elementCss.properties

import org.w3c.dom.css.CSSStyleDeclaration

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

    override fun applyToStyle(style: CSSStyleDeclaration) {
        if (value != null) {
            if (axis == OverflowAxis.X) {
                style.overflowX = value!!.cssString()
            } else {
                style.overflowY = value!!.cssString()
            }
        }
    }
}