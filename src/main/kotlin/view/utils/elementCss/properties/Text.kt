package view.utils.elementCss.properties

import org.w3c.dom.HTMLElement
import view.utils.extensions.nonNull

class Text: CssProperty {

    enum class Align: CssValue {
        LEFT,
        RIGHT,
        CENTER,
        JUSTIFY
    }

    var align: Align? = null
    var color: String? = null

    override fun applyToStyle(element: HTMLElement) {
        align.nonNull { element.style.textAlign = it.cssString() }
        color.nonNull { element.style.color = it }
    }
}