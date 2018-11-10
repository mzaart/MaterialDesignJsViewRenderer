package utils.elementCss.properties

import org.w3c.dom.HTMLElement
import utils.extensions.nonNull

class Font: CssProperty {

    enum class Style: CssValue {
        NORMAL,
        ITALIC,
        OBLIQUE,
        INITIAL,
        INHERIT
    }

    enum class Weight: CssValue {
        NORMAL,
        BOLD,
        BOLDER,
        LIGHTER,
        INITIAL,
        INHERIT
    }

    var family: List<String>? = null
    var size: Dimension = Dimension("font-size")
    var style: Style? = null
    var weight: Weight? = null


    override fun applyToStyle(element: HTMLElement) {
        element.style.apply {
            family.nonNull {
                fontFamily = it.map { "'$it'" }.joinToString(" ")
            }
            size.applyToStyle(element)
            style.nonNull { fontStyle = it.cssString() }
            weight.nonNull { fontWeight = it.cssString() }
        }
    }
}