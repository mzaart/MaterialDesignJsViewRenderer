package core.renderers.viewRenderers.inputs

import core.renderers.viewRenderers.ViewRenderer
import core.views.input.Button
import org.w3c.dom.HTMLElement
import utils.addClasses
import utils.extensions.nonNull
import utils.mapBased.keys.HasKeys
import utils.mapBased.keys.delegates.nullable.BoolRWKey
import utils.mapBased.keys.delegates.nullable.EnumRWKey
import kotlin.browser.document

class ButtonRenderer: ViewRenderer<Button>() {

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

    override val element = document.createElement("button") as HTMLElement

    private val buttonConfig = ButtonConfig()

    override fun buildElement(view: Button) {
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

        if (buttonConfig.accentColor != null) classes -= "mdl-button--accent"
        when (buttonConfig.accentColor) {
            ButtonConfig.Color.PRIMARY -> classes += "mdl-button--primary"
            ButtonConfig.Color.ACCENT -> classes += "mdl-button--accent"
            else -> { }
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