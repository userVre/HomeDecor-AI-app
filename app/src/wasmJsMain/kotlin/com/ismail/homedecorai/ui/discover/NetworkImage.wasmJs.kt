package com.ismail.homedecorai.ui.discover

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import homedecorai.app.generated.resources.Res
import homedecorai.app.generated.resources.assets_media_discover_exterior_exteriorapartmentblock
import homedecorai.app.generated.resources.assets_media_discover_exterior_exteriorglassoffice
import homedecorai.app.generated.resources.assets_media_discover_exterior_exteriormodernvilla
import homedecorai.app.generated.resources.assets_media_discover_exterior_exteriorpoolhouse
import homedecorai.app.generated.resources.assets_media_discover_exterior_exteriorretailstorefront
import homedecorai.app.generated.resources.assets_media_discover_exterior_exteriorstonemanor
import homedecorai.app.generated.resources.assets_media_discover_floorscenes_heritagewalnutplank
import homedecorai.app.generated.resources.assets_media_discover_floorscenes_industrialgrayconcrete
import homedecorai.app.generated.resources.assets_media_discover_floorscenes_modernslatetile
import homedecorai.app.generated.resources.assets_media_discover_floorscenes_naturaloakparquet
import homedecorai.app.generated.resources.assets_media_discover_floorscenes_plushivorycarpet
import homedecorai.app.generated.resources.assets_media_discover_floorscenes_polishedcarraramarble
import homedecorai.app.generated.resources.assets_media_discover_floorscenes_terracottaateliertile
import homedecorai.app.generated.resources.assets_media_discover_floorscenes_walnutchevron
import homedecorai.app.generated.resources.assets_media_discover_floorscenes_weatheredoakstudio
import homedecorai.app.generated.resources.assets_media_discover_garden_gardenbackyard
import homedecorai.app.generated.resources.assets_media_discover_garden_gardendeck
import homedecorai.app.generated.resources.assets_media_discover_garden_gardenfiresidepatio
import homedecorai.app.generated.resources.assets_media_discover_garden_gardenfrontyard
import homedecorai.app.generated.resources.assets_media_discover_garden_gardenpatio
import homedecorai.app.generated.resources.assets_media_discover_garden_gardenpoolcourtyard
import homedecorai.app.generated.resources.assets_media_discover_garden_gardenswimmingpool
import homedecorai.app.generated.resources.assets_media_discover_garden_gardenterrace
import homedecorai.app.generated.resources.assets_media_discover_garden_gardenvillaentry
import homedecorai.app.generated.resources.assets_media_discover_generated_bedroom_bedroom1
import homedecorai.app.generated.resources.assets_media_discover_generated_bedroom_bedroom2
import homedecorai.app.generated.resources.assets_media_discover_generated_bedroom_bedroom3
import homedecorai.app.generated.resources.assets_media_discover_generated_bedroom_bedroom4
import homedecorai.app.generated.resources.assets_media_discover_generated_bedroom_bedroom5
import homedecorai.app.generated.resources.assets_media_discover_generated_bedroom_bedroom6
import homedecorai.app.generated.resources.assets_media_discover_generated_bedroom_bedroom7
import homedecorai.app.generated.resources.assets_media_discover_generated_exterior_exterior1
import homedecorai.app.generated.resources.assets_media_discover_generated_exterior_exterior2
import homedecorai.app.generated.resources.assets_media_discover_generated_exterior_exterior3
import homedecorai.app.generated.resources.assets_media_discover_generated_exterior_exterior4
import homedecorai.app.generated.resources.assets_media_discover_generated_exterior_exterior6
import homedecorai.app.generated.resources.assets_media_discover_generated_exterior_exterior7
import homedecorai.app.generated.resources.assets_media_discover_generated_garden_garden1
import homedecorai.app.generated.resources.assets_media_discover_generated_garden_garden2
import homedecorai.app.generated.resources.assets_media_discover_generated_garden_garden4
import homedecorai.app.generated.resources.assets_media_discover_generated_kitchen_kitchen1
import homedecorai.app.generated.resources.assets_media_discover_generated_kitchen_kitchen2
import homedecorai.app.generated.resources.assets_media_discover_generated_kitchen_kitchen3
import homedecorai.app.generated.resources.assets_media_discover_generated_kitchen_kitchen4
import homedecorai.app.generated.resources.assets_media_discover_generated_kitchen_kitchen5
import homedecorai.app.generated.resources.assets_media_discover_generated_kitchen_kitchen6
import homedecorai.app.generated.resources.assets_media_discover_generated_kitchen_kitchen7
import homedecorai.app.generated.resources.assets_media_discover_generated_livingroom_livingroom1
import homedecorai.app.generated.resources.assets_media_discover_generated_livingroom_livingroom2
import homedecorai.app.generated.resources.assets_media_discover_generated_livingroom_livingroom3
import homedecorai.app.generated.resources.assets_media_discover_generated_livingroom_livingroom4
import homedecorai.app.generated.resources.assets_media_discover_generated_livingroom_livingroom5
import homedecorai.app.generated.resources.assets_media_discover_generated_livingroom_livingroom6
import homedecorai.app.generated.resources.assets_media_discover_generated_livingroom_livingroom7
import homedecorai.app.generated.resources.assets_media_discover_home_homediningroom
import homedecorai.app.generated.resources.assets_media_discover_home_homehomeoffice
import homedecorai.app.generated.resources.assets_media_discover_home_homebathroom
import homedecorai.app.generated.resources.assets_media_discover_home_homestudy
import homedecorai.app.generated.resources.assets_media_discover_wallscenes_lavendermistbath
import homedecorai.app.generated.resources.assets_media_styles_styleartdeco
import homedecorai.app.generated.resources.assets_media_styles_styleluxury
import homedecorai.app.generated.resources.assets_media_styles_stylemediterranean
import homedecorai.app.generated.resources.assets_media_styles_stylemidcentury
import homedecorai.app.generated.resources.tool_exterior
import homedecorai.app.generated.resources.tool_floor
import homedecorai.app.generated.resources.tool_garden
import homedecorai.app.generated.resources.tool_interior
import homedecorai.app.generated.resources.tool_layout
import homedecorai.app.generated.resources.tool_paint
import homedecorai.app.generated.resources.tool_reference
import homedecorai.app.generated.resources.tool_replace
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
) {
    val painter = urlToPainter(url)

    Box(
        modifier = modifier
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun urlToPainter(url: String): Painter? {
    if (url.isEmpty()) return null
    val name = url.removePrefix("images/").removeSuffix(".webp")
    return when (name) {
        "tool_interior" -> painterResource(Res.drawable.tool_interior)
        "tool_exterior" -> painterResource(Res.drawable.tool_exterior)
        "tool_garden" -> painterResource(Res.drawable.tool_garden)
        "tool_paint" -> painterResource(Res.drawable.tool_paint)
        "tool_floor" -> painterResource(Res.drawable.tool_floor)
        "tool_layout" -> painterResource(Res.drawable.tool_layout)
        "tool_replace" -> painterResource(Res.drawable.tool_replace)
        "tool_reference" -> painterResource(Res.drawable.tool_reference)
        "assets_media_discover_generated_kitchen_kitchen1" -> painterResource(Res.drawable.assets_media_discover_generated_kitchen_kitchen1)
        "assets_media_discover_generated_kitchen_kitchen2" -> painterResource(Res.drawable.assets_media_discover_generated_kitchen_kitchen2)
        "assets_media_discover_generated_kitchen_kitchen3" -> painterResource(Res.drawable.assets_media_discover_generated_kitchen_kitchen3)
        "assets_media_discover_generated_kitchen_kitchen4" -> painterResource(Res.drawable.assets_media_discover_generated_kitchen_kitchen4)
        "assets_media_discover_generated_kitchen_kitchen5" -> painterResource(Res.drawable.assets_media_discover_generated_kitchen_kitchen5)
        "assets_media_discover_generated_kitchen_kitchen6" -> painterResource(Res.drawable.assets_media_discover_generated_kitchen_kitchen6)
        "assets_media_discover_generated_kitchen_kitchen7" -> painterResource(Res.drawable.assets_media_discover_generated_kitchen_kitchen7)
        "assets_media_discover_generated_livingroom_livingroom1" -> painterResource(Res.drawable.assets_media_discover_generated_livingroom_livingroom1)
        "assets_media_discover_generated_livingroom_livingroom2" -> painterResource(Res.drawable.assets_media_discover_generated_livingroom_livingroom2)
        "assets_media_discover_generated_livingroom_livingroom3" -> painterResource(Res.drawable.assets_media_discover_generated_livingroom_livingroom3)
        "assets_media_discover_generated_livingroom_livingroom4" -> painterResource(Res.drawable.assets_media_discover_generated_livingroom_livingroom4)
        "assets_media_discover_generated_livingroom_livingroom5" -> painterResource(Res.drawable.assets_media_discover_generated_livingroom_livingroom5)
        "assets_media_discover_generated_livingroom_livingroom6" -> painterResource(Res.drawable.assets_media_discover_generated_livingroom_livingroom6)
        "assets_media_discover_generated_livingroom_livingroom7" -> painterResource(Res.drawable.assets_media_discover_generated_livingroom_livingroom7)
        "assets_media_discover_generated_bedroom_bedroom1" -> painterResource(Res.drawable.assets_media_discover_generated_bedroom_bedroom1)
        "assets_media_discover_generated_bedroom_bedroom2" -> painterResource(Res.drawable.assets_media_discover_generated_bedroom_bedroom2)
        "assets_media_discover_generated_bedroom_bedroom3" -> painterResource(Res.drawable.assets_media_discover_generated_bedroom_bedroom3)
        "assets_media_discover_generated_bedroom_bedroom4" -> painterResource(Res.drawable.assets_media_discover_generated_bedroom_bedroom4)
        "assets_media_discover_generated_bedroom_bedroom5" -> painterResource(Res.drawable.assets_media_discover_generated_bedroom_bedroom5)
        "assets_media_discover_generated_bedroom_bedroom6" -> painterResource(Res.drawable.assets_media_discover_generated_bedroom_bedroom6)
        "assets_media_discover_generated_bedroom_bedroom7" -> painterResource(Res.drawable.assets_media_discover_generated_bedroom_bedroom7)
        "assets_media_discover_home_homebathroom" -> painterResource(Res.drawable.assets_media_discover_home_homebathroom)
        "assets_media_discover_wallscenes_lavendermistbath" -> painterResource(Res.drawable.assets_media_discover_wallscenes_lavendermistbath)
        "assets_media_styles_styleluxury" -> painterResource(Res.drawable.assets_media_styles_styleluxury)
        "assets_media_discover_home_homehomeoffice" -> painterResource(Res.drawable.assets_media_discover_home_homehomeoffice)
        "assets_media_discover_home_homestudy" -> painterResource(Res.drawable.assets_media_discover_home_homestudy)
        "assets_media_styles_stylemidcentury" -> painterResource(Res.drawable.assets_media_styles_stylemidcentury)
        "assets_media_discover_home_homediningroom" -> painterResource(Res.drawable.assets_media_discover_home_homediningroom)
        "assets_media_styles_styleartdeco" -> painterResource(Res.drawable.assets_media_styles_styleartdeco)
        "assets_media_styles_stylemediterranean" -> painterResource(Res.drawable.assets_media_styles_stylemediterranean)
        "assets_media_discover_exterior_exteriormodernvilla" -> painterResource(Res.drawable.assets_media_discover_exterior_exteriormodernvilla)
        "assets_media_discover_generated_exterior_exterior1" -> painterResource(Res.drawable.assets_media_discover_generated_exterior_exterior1)
        "assets_media_discover_generated_exterior_exterior2" -> painterResource(Res.drawable.assets_media_discover_generated_exterior_exterior2)
        "assets_media_discover_generated_exterior_exterior3" -> painterResource(Res.drawable.assets_media_discover_generated_exterior_exterior3)
        "assets_media_discover_generated_exterior_exterior4" -> painterResource(Res.drawable.assets_media_discover_generated_exterior_exterior4)
        "assets_media_discover_generated_exterior_exterior6" -> painterResource(Res.drawable.assets_media_discover_generated_exterior_exterior6)
        "assets_media_discover_generated_exterior_exterior7" -> painterResource(Res.drawable.assets_media_discover_generated_exterior_exterior7)
        "assets_media_discover_exterior_exteriorapartmentblock" -> painterResource(Res.drawable.assets_media_discover_exterior_exteriorapartmentblock)
        "assets_media_discover_exterior_exteriorglassoffice" -> painterResource(Res.drawable.assets_media_discover_exterior_exteriorglassoffice)
        "assets_media_discover_exterior_exteriorpoolhouse" -> painterResource(Res.drawable.assets_media_discover_exterior_exteriorpoolhouse)
        "assets_media_discover_exterior_exteriorstonemanor" -> painterResource(Res.drawable.assets_media_discover_exterior_exteriorstonemanor)
        "assets_media_discover_garden_gardenfiresidepatio" -> painterResource(Res.drawable.assets_media_discover_garden_gardenfiresidepatio)
        "assets_media_discover_generated_garden_garden1" -> painterResource(Res.drawable.assets_media_discover_generated_garden_garden1)
        "assets_media_discover_generated_garden_garden2" -> painterResource(Res.drawable.assets_media_discover_generated_garden_garden2)
        "assets_media_discover_garden_gardenpatio" -> painterResource(Res.drawable.assets_media_discover_garden_gardenpatio)
        "assets_media_discover_generated_garden_garden4" -> painterResource(Res.drawable.assets_media_discover_generated_garden_garden4)
        "assets_media_discover_garden_gardenswimmingpool" -> painterResource(Res.drawable.assets_media_discover_garden_gardenswimmingpool)
        "assets_media_discover_garden_gardenpoolcourtyard" -> painterResource(Res.drawable.assets_media_discover_garden_gardenpoolcourtyard)
        "assets_media_discover_garden_gardenterrace" -> painterResource(Res.drawable.assets_media_discover_garden_gardenterrace)
        "assets_media_discover_garden_gardendeck" -> painterResource(Res.drawable.assets_media_discover_garden_gardendeck)
        "assets_media_discover_garden_gardenfrontyard" -> painterResource(Res.drawable.assets_media_discover_garden_gardenfrontyard)
        "assets_media_discover_garden_gardenvillaentry" -> painterResource(Res.drawable.assets_media_discover_garden_gardenvillaentry)
        "assets_media_discover_floorscenes_heritagewalnutplank" -> painterResource(Res.drawable.assets_media_discover_floorscenes_heritagewalnutplank)
        "assets_media_discover_floorscenes_industrialgrayconcrete" -> painterResource(Res.drawable.assets_media_discover_floorscenes_industrialgrayconcrete)
        "assets_media_discover_floorscenes_modernslatetile" -> painterResource(Res.drawable.assets_media_discover_floorscenes_modernslatetile)
        "assets_media_discover_floorscenes_naturaloakparquet" -> painterResource(Res.drawable.assets_media_discover_floorscenes_naturaloakparquet)
        "assets_media_discover_floorscenes_plushivorycarpet" -> painterResource(Res.drawable.assets_media_discover_floorscenes_plushivorycarpet)
        "assets_media_discover_floorscenes_polishedcarraramarble" -> painterResource(Res.drawable.assets_media_discover_floorscenes_polishedcarraramarble)
        "assets_media_discover_floorscenes_terracottaateliertile" -> painterResource(Res.drawable.assets_media_discover_floorscenes_terracottaateliertile)
        "assets_media_discover_floorscenes_walnutchevron" -> painterResource(Res.drawable.assets_media_discover_floorscenes_walnutchevron)
        "assets_media_discover_floorscenes_weatheredoakstudio" -> painterResource(Res.drawable.assets_media_discover_floorscenes_weatheredoakstudio)
        "assets_media_discover_exterior_exteriorretailstorefront" -> painterResource(Res.drawable.assets_media_discover_exterior_exteriorretailstorefront)
        "assets_media_discover_garden_gardenbackyard" -> painterResource(Res.drawable.assets_media_discover_garden_gardenbackyard)
        else -> null
    }
}
