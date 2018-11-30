package view.core.renderers.viewRenderers.display

import view.core.renderers.viewRenderers.AbstractViewRenderer
import view.core.views.display.ImageView
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import view.utils.elementCss.ElementCss
import view.utils.elementCss.RelativePositioning
import view.utils.elementCss.properties.*
import view.utils.extensions.applyCss
import kotlin.browser.document

class ImageViewRenderer(
        view: ImageView,
        element: HTMLElement,
        reRendering: Boolean = true
): AbstractViewRenderer<ImageView>(view, element, reRendering) {

    constructor(view: ImageView): this(view, document.createElement("div") as HTMLElement, false)

    override fun buildElement() {
        element.applyCss {
            display = Display.INLINE_BLOCK
            overflowX.value = Overflow.Value.HIDDEN
            overflowY.value = Overflow.Value.HIDDEN
        }

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