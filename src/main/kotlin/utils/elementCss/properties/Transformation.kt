package utils.elementCss.properties

import org.w3c.dom.css.CSSStyleDeclaration
import utils.extensions.nonNull

open class Transformation: CssProperty {

    private var translate: Array<Dimension> = arrayOf(Dimension(""), Dimension(""))

    fun translateX(pair: Pair<Double, Dimension.Unit>) {
        translate(Dimension(pair.first, pair.second, ""), translate[1])
    }

    fun translateY(pair: Pair<Double, Dimension.Unit>) {
        translate(translate[0], Dimension(pair.first, pair.second, ""))
    }

    fun translate(x: Dimension, y: Dimension) {
        translate[0] = x
        translate[1] = y
    }

    override fun applyToStyle(style: CSSStyleDeclaration) {
        toCss().nonNull { style.transform = it }
    }

    open fun toCss(): String? {
        if (!translate[0].isSet && !translate[1].isSet) {
            return null
        } else if (!translate[0].isSet) {
            return "translateY(${translate[1].cssString()})"
        } else if (!translate[1].isSet) {
            return "translateX(${translate[0].cssString()})"
        } else {
            return "translate(${translate[0].cssString()},${translate[1].cssString()})"
        }
    }
}