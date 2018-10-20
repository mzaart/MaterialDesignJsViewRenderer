package di

import core.renderers.DefaultTheme
import core.renderers.RendererConfig
import core.renderers.viewRenderers.inputs.ButtonRenderer
import core.renderers.viewRenderers.ViewRenderer
import core.renderers.viewRenderers.layouts.LinearLayoutRenderer
import core.views.Theme
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton

object Container {

    val kodein = Kodein {

        bind<RendererConfig>() with singleton { RendererConfig() }
        bind<Theme>() with singleton { DefaultTheme() }

        bind<ViewRenderer<*>>("Button") with provider { ButtonRenderer() }
        bind<ViewRenderer<*>>("LinearLayout") with provider { LinearLayoutRenderer() }
    }
}