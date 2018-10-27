package core.renderers.viewRenderers.inputs

import core.renderers.viewRenderers.AbstractViewRenderer
import core.views.input.Button
import org.w3c.dom.HTMLElement
import utils.extensions.addClasses
import utils.extensions.nonNull
import utils.mapBased.keys.HasKeys
import utils.mapBased.keys.delegates.nullable.BoolRWKey
import utils.mapBased.keys.delegates.nullable.EnumRWKey
import kotlin.browser.document

class ButtonRenderer(
        view: Button,
        element: HTMLElement,
        reRendering: Boolean = true
): AbstractViewRenderer<Button>(view, element, reRendering) {

    class ButtonConfig: HasKeys() {

        override var keys: MutableMap<String, Any?> = mutableMapOf()

        enum class Color {
            PRIMARY,
            ACCENT,
            NONE
        }

        enum class Type {
            FAB,
            FLAT
        }

        val ripple by BoolRWKey
        val raise: Boolean? by BoolRWKey
        val accentColor by EnumRWKey(Color.values())
        val type by EnumRWKey(Type.values())
    }

    private val buttonConfig = ButtonConfig()

    constructor(view: Button): this(view, document.createElement("button") as HTMLElement, false)

    override fun buildElement() {
        val viewExtras = view.webExtras
        if (viewExtras != null) {
            buttonConfig.keys = viewExtras.keys
        }

        val classes: MutableSet<String> = mutableSetOf(
                "mdl-button",
                "mdl-js-button"
        )
        applyCustomConfig(classes)

        element.addClasses(classes)
        element.textContent = view.text
    }

    private fun applyCustomConfig(classes: MutableSet<String>) {
        when (buttonConfig.ripple) {
            true -> classes += "mdl-js-ripple-effect"
            false -> classes -= "mdl-js-ripple-effect"
        }

        buttonConfig.raise.nonNull {
            if (it) classes += "mdl-button--raised" else classes -= "mdl-button--raised"
        }

        when (buttonConfig.accentColor) {
            null, ButtonConfig.Color.NONE -> classes -= listOf("mdl-button--accent", "mdl-button--primary")
            ButtonConfig.Color.PRIMARY -> classes += "mdl-button--primary"
            ButtonConfig.Color.ACCENT -> classes += "mdl-button--accent"
        }

        buttonConfig.type.nonNull {
            if (it.toString() == ButtonConfig.Type.FAB.toString()) {
                classes.add("mdl-button--fab")
            } else {
                classes.remove("mdl-button--raised")
            }
        }
    }
}