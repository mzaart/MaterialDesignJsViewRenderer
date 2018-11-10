package core.renderers.viewRenderers.display

import core.renderers.viewRenderers.AbstractViewRenderer
import core.views.Dimension
import core.views.display.TextView
import org.w3c.dom.HTMLElement
import utils.elementCss.properties.CssUnit
import utils.elementCss.properties.Font
import utils.elementCss.properties.Text
import utils.extensions.applyCss
import utils.extensions.nonNull
import kotlin.browser.document

class TextViewRenderer(
        view: TextView,
        element: HTMLElement,
        reRendering: Boolean = true
): AbstractViewRenderer<TextView>(view, element, reRendering) {

    constructor(view: TextView): this(view, document.createElement("p") as HTMLElement, false)

    override fun buildElement() {
        view.text.nonNull {
            val textNode = document.createTextNode(it)
            element.appendChild(textNode)
        }

        element.applyCss {
            view.align.nonNull {
                text.align = when (it) {
                    TextView.Align.LEFT -> Text.Align.LEFT
                    TextView.Align.RIGHT -> Text.Align.RIGHT
                    TextView.Align.CENTER -> Text.Align.CENTER
                    TextView.Align.JUSTIFY -> Text.Align.JUSTIFY
                }
            }
            view.fontColor.nonNull { text.color = it.toString(16) }

            view.font.nonNull { font.family = listOf(it) }
            view.fontSize.nonNull { s ->
                val size = when (Dimension.type(s)) {
                    Dimension.Type.WRAP_CONTENT -> null
                    Dimension.Type.RELATIVE ->  s * 100 to CssUnit.RELATIVE
                    Dimension.Type.EXPLICIT -> s to CssUnit.PX
                }
                size.nonNull { font.size.set(it) }
            }
            view.fontStyle.nonNull { it.forEach { s ->
                when (s) {
                    TextView.FontStyle.NORMAL ->  font.style = Font.Style.NORMAL
                    TextView.FontStyle.ITALIC -> font.style = Font.Style.ITALIC
                    TextView.FontStyle.BOLD -> font.weight = Font.Weight.BOLD
                }
            } }
        }
    }
}