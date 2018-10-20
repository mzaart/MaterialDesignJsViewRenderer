package core.renderers

import core.renderers.viewRenderers.inputs.ButtonRenderer
import core.views.Theme
import core.views.View
import core.views.input.Button
import utils.mapBased.keys.HasKeys

open class DefaultTheme: Theme() {

    // view settings
    open val horizontalMargin: Double = 16.0
    open val verticalMargin: Double = 16.0

    // button settings
    open val buttonRipple = true
    open val buttonRaised = true
    open val buttonAccentColor = ButtonRenderer.ButtonConfig.Color.ACCENT

    init {
        // register preprocessors

        val viewPreProcessor = { view: Any ->
            if (view !is View) {
                throwExcp()
            } else {
                view.marginStart = horizontalMargin
                view.marginEnd = horizontalMargin
                view.marginTop = verticalMargin
                view.marginBottom = verticalMargin
            }
        }
        register(View::class, viewPreProcessor)


        val buttonPreProcessor = { button: Any ->
            if (button !is Button) {
                throwExcp()
            } else {
                viewPreProcessor(button)
                if (button.webExtras == null) {
                    button.webExtras = HasKeys(mutableMapOf<String, Any?>())
                }
                val keys = button.webExtras!!.keys
                keys.setIfEmpty("ripple", buttonRipple)
                keys.setIfEmpty("raise", buttonRaised)
                keys.setIfEmpty("accentColor", buttonAccentColor)
            }
        }
        register(Button::class, buttonPreProcessor)
    }

    protected fun <K, V> MutableMap<K, V>.setIfEmpty(k: K, v: V) {
        if (this[k] == null) {
            this[k] = v
        }
    }

    protected fun throwExcp() {
        throw IllegalStateException("PreProcessor does not correspond to the object passed")
    }
}