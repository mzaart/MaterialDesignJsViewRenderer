package core.renderers.viewRenderers.display

import core.renderers.viewRenderers.AbstractViewRenderer
import core.views.display.ImageView
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.Event
import utils.elementCss.ElementCss
import utils.elementCss.RelativePositioning
import utils.elementCss.properties.CssDimen
import utils.elementCss.properties.CssUnit
import utils.elementCss.properties.Overflow
import utils.elementCss.properties.Position
import utils.extensions.applyCss
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass

class ImageViewRenderer(
        view: ImageView,
        element: HTMLElement,
        reRendering: Boolean = true
): AbstractViewRenderer<ImageView>(view, element, reRendering) {

    constructor(view: ImageView): this(view, document.createElement("div") as HTMLElement, false)

    override fun buildElement() {
        element.applyCss {
            overflowX.value = Overflow.Value.HIDDEN
            overflowY.value = Overflow.Value.HIDDEN
        }
        element.addClass("test")

        val image = document.createElement("img") as HTMLImageElement
        image.src = view.imageResource

        when (view.scaleType) {
            ImageView.ScaleType.CENTER -> center(image)
            ImageView.ScaleType.CENTER_CROP -> centerCrop(image)
            ImageView.ScaleType.CENTER_INSIDE -> centerInside(image)
            ImageView.ScaleType.FIT -> fit(image)
        }

        element.appendChild(image)
    }

    private fun center(image: HTMLImageElement) {
        val imageCss = ElementCss().apply { position = Position.ABSOLUTE }
        RelativePositioning.centerHorizontal(imageCss)
        RelativePositioning.centerVertical(imageCss)
        imageCss.applyTo(image)
    }

    private fun centerCrop(image: HTMLImageElement) {
        image.applyCss {
            width.set(100.0 to CssUnit.RELATIVE)
            height.set(CssDimen.AUTO)
        }
    }

    private fun centerInside(image: HTMLImageElement) {
        image.style.setProperty("object-fit", "contain")
        image.style.setProperty("-o-object-fit", "contain")
        image.style.setProperty("-moz-object-fit", "contain")
        image.style.setProperty("-ms-object-fit", "contain")
        image.style.setProperty("-webkit-object-fit", "contain")
    }

    private fun fit(image: HTMLImageElement) {
        image.applyCss {
            width.set(100.0 to CssUnit.RELATIVE)
            height.set(100.0 to CssUnit.RELATIVE)
        }
    }
}