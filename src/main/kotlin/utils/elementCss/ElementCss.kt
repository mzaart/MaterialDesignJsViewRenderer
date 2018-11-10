package utils.elementCss

import org.w3c.dom.HTMLElement
import utils.elementCss.properties.*

open class ElementCss {

    private val css: MutableMap<String, Any?> = mutableMapOf()

    var visibility: Visibility? by css
    var display: Display? by css

    val width: Dimension by css
    val height: Dimension by css

    var position: Position? by css

    val top: Dimension by css
    val bottom: Dimension by css
    val start: Dimension by css
    val end: Dimension by css
    
    val marginTop: Dimension by css
    val marginBottom: Dimension by css
    val marginStart: Dimension by css
    val marginEnd: Dimension by css

    val paddingTop: Dimension by css
    val paddingBottom: Dimension by css
    val paddingStart: Dimension by css
    val paddingEnd: Dimension by css

    val overflowX: Overflow by css
    val overflowY: Overflow by css

    var whiteSpace: WhiteSpace? by css

    val flexSettings: FlexSettings by css
    val flexItemSettings: FlexItemSettings by css

    val transformation: Transformation by css

    val grid: Grid by css
    val gridItem: GridItem by css

    val text: Text by css
    val font: Font by css

    init {
        css["width"] = Dimension("width")
        css["height"] = Dimension("height")
        
        css["top"] = Dimension("top")
        css["bottom"] = Dimension("bottom")
        css["start"] = Dimension("left")
        css["end"] = Dimension("right")
        
        css["marginTop"] = Dimension("margin-top")
        css["marginBottom"] = Dimension("margin-bottom")
        css["marginStart"] = Dimension("margin-left")
        css["marginEnd"] = Dimension("margin-end")

        css["paddingTop"] = Dimension("padding-top")
        css["paddingBottom"] = Dimension("padding-bottom")
        css["paddingStart"] = Dimension("padding-left")
        css["paddingEnd"] = Dimension("padding-end")

        css["overflowX"] = Overflow(Overflow.OverflowAxis.X)
        css["overflowY"] = Overflow(Overflow.OverflowAxis.Y)
        
        css["flexSettings"] = FlexSettings()
        css["flexItemSettings"] = FlexItemSettings()
        
        css["transformation"] = Transformation()

        css["grid"] = Grid()
        css["gridItem"] = GridItem()

        css["font"] = Font()
        css["text"] = Text()
    }

    fun extend(style: ElementCss, override: Boolean = false) {
        css += if (override) {
            style.css
        } else {
            (style.css - css).toMutableMap() as MutableMap<String, Any?>
        }
    }

    open fun applyTo(element: HTMLElement) {
        css.values.forEach { cssProp ->
            if (cssProp !is CssProperty) {
                throw IllegalStateException("Field is not an instance of CssProperty")
            }
            cssProp.applyToStyle(element)
        }
    }
}
