package com.ismail.homedecorai

import com.ismail.homedecorai.model.AdvancedControlSpec
import com.ismail.homedecorai.model.DiamondPack
import com.ismail.homedecorai.model.DiscoverSection
import com.ismail.homedecorai.model.GalleryItem
import com.ismail.homedecorai.model.DecorTool

object HomeDecorCatalog {
    val tools = listOf(
        DecorTool(
            id = "interior",
            title = "Design d'intérieur",
            description = "Redéfinissez votre espace avec l'IA.",
            imageRes = R.drawable.tool_interior,
            serviceType = "redesign",
        ),
        DecorTool(
            id = "facade",
            title = "Conception extérieure",
            description = "Réinventez l'extérieur de votre maison.",
            imageRes = R.drawable.tool_exterior,
            serviceType = "redesign",
        ),
        DecorTool(
            id = "garden",
            title = "Conception de jardin",
            description = "Concevez de beaux espaces extérieurs.",
            imageRes = R.drawable.tool_garden,
            serviceType = "redesign",
        ),
        DecorTool(
            id = "paint",
            title = "Peinture intelligente",
            description = "Palettes et textures sur mesure.",
            imageRes = R.drawable.tool_paint,
            serviceType = "paint",
        ),
        DecorTool(
            id = "floor",
            title = "Relooking du sol",
            description = "Matériaux et finitions haut de gamme.",
            imageRes = R.drawable.tool_floor,
            serviceType = "floor",
        ),
        DecorTool(
            id = "layout",
            title = "Agencement Intelligent",
            description = "Optimisez l'agencement pour le confort.",
            imageRes = R.drawable.tool_layout,
            serviceType = "layout",
        ),
        DecorTool(
            id = "replace",
            title = "Remplacer des objets",
            description = "Masquez et remplacez avec l'IA.",
            imageRes = R.drawable.tool_replace,
            serviceType = "replace",
        ),
        DecorTool(
            id = "reference",
            title = "Transfert de style",
            description = "Appliquez un style visuel à votre pièce.",
            imageRes = R.drawable.tool_reference,
            serviceType = "reference",
        ),
    )

    val rooms = listOf(
        "Salon",
        "Chambre à coucher",
        "Cuisine",
        "Salle de bain",
        "Bureau à domicile",
        "Salle à manger",
        "Chambre d'enfant",
        "Cinéma maison",
        "Salle de jeux",
        "Entrée / couloir",
        "Bibliothèque",
        "Blanchisserie",
    )

    val buildingTypes = listOf(
        "Appartement",
        "Maison",
        "Immeuble de bureaux",
        "Résidentiel",
        "Vente au détail",
        "Villa",
    )

    val outdoorSpaces = listOf(
        "Cour arrière",
        "Terrasse",
        "Patio",
        "Cour",
        "Piscine",
        "Jardin avant",
    )

    val gardenStyles = listOf(
        "Noël",
        "Moderne",
        "Tropicale",
        "Minimaliste",
        "Méditerranéen",
        "Japandi",
        "Rustique",
        "Zen",
        "Jardin anglais",
        "Paysage",
        "Bohème",
        "Scandinave",
    )

    val maskTargets = listOf(
        "Mur",
        "Sol",
        "Sofa",
        "Table",
        "Cabinet",
        "Éclairage",
    )

    val materialLibrary = listOf(
        "Carrara Marble",
        "Oak Wood",
        "Walnut",
        "Concrete",
        "Limewash",
        "Terrazzo",
        "White Tile",
        "Black Tile",
        "Warm Beige",
        "Dark Elegant",
    )

    val layoutGoals = listOf(
        "Circulation ouverte",
        "Plus de rangement",
        "Coin bureau",
        "Espace familial",
        "Salon plus spacieux",
        "Meilleure lumière",
        "Réorganisation complète",
        "Coin lecture cozy",
        "Espace pet-friendly",
        "Zone méditation",
        "Zone multi-usage",
        "Espace télétravail",
        "Espace jeux enfants",
    )

    val referenceStrengths = listOf(
        "Subtil",
        "Équilibré",
        "Fidèle",
        "Très fidèle",
    )

    val referenceOptions = listOf(
        "Palette seulement",
        "Matériaux",
        "Mobilier",
        "Lumière",
        "Ambiance complète",
    )

    val budgetModes = listOf(
        "Low budget",
        "Medium budget",
        "Luxury",
    )

    val avoidOptions = listOf(
        "no dark colors",
        "no structural changes",
        "no plants",
        "keep windows",
        "no furniture changes",
    )

    val advancedControlSpecs = mapOf(
        "interior" to AdvancedControlSpec(
            keepOptions = listOf("agencement", "fenêtres", "sol", "mobilier principal"),
            changeOptions = listOf("style", "couleurs", "décor", "éclairage"),
        ),
        "facade" to AdvancedControlSpec(
            keepOptions = listOf("structure", "fenêtres", "toit", "entrée"),
            changeOptions = listOf("façade", "couleurs", "éclairage", "paysage"),
        ),
        "garden" to AdvancedControlSpec(
            keepOptions = listOf("agencement", "arbres", "piscine", "terrasse", "clôture"),
            changeOptions = listOf("plantes", "éclairage", "mobilier", "chemins"),
        ),
        "layout" to AdvancedControlSpec(
            keepOptions = listOf("murs", "fenêtres", "portes", "mobilier important"),
            changeOptions = listOf("organisation", "circulation", "rangement", "zones"),
        ),
        "reference" to AdvancedControlSpec(
            keepOptions = listOf("agencement", "mobilier", "couleurs principales"),
            changeOptions = listOf("style", "ambiance", "matériaux", "décor"),
        ),
    )

    val protectRestToolIds = setOf("replace", "paint", "floor")

    val paintColors = materialLibrary

    val replaceSuggestions = listOf(
        "Remplacer le sofa",
        "Remplacer la table",
        "Remplacer la lampe",
        "Remplacer le tapis",
        "Remplacer l'art mural",
        "Remplacer la plante",
        "Remplacer la chaise",
        "Remplacer le cabinet",
    )

    val replacementTemplatePrompts = mapOf(
        "Remplacer le sofa" to "modern sofa matching the room scale, perspective, and light",
        "Remplacer la table" to "refined table matching the room scale, perspective, and light",
        "Remplacer la lampe" to "elegant lamp matching the room scale, perspective, and light",
        "Remplacer le tapis" to "textured area rug matching the room scale, perspective, and light",
        "Remplacer l'art mural" to "framed wall art matching the room scale, perspective, and light",
        "Remplacer la plante" to "healthy indoor plant matching the room scale, perspective, and light",
        "Remplacer la chaise" to "comfortable accent chair matching the room scale, perspective, and light",
        "Remplacer le cabinet" to "streamlined cabinet matching the room scale, perspective, and light",
    )

    val styles = listOf(
        "Moderne",
        "Luxe",
        "Japandi",
        "Cyberpunk",
        "Tropicale",
        "Minimaliste",
        "Scandinave",
        "Bohème",
        "Midcentury",
        "Art Deco",
        "Côtier",
        "Rustique",
        "Vintage",
        "Méditerranéen",
        "Glam",
        "Campagne française",
    )

    val palettes = listOf(
        "Mélange organisé",
        "Gris millénaire",
        "Mirage en terre cuite",
        "Teintes forestières",
        "Verger de pêchers",
        "Fleur fuchsia",
        "Gemme d'émeraude",
        "Brise pastel",
        "Brume océanique",
        "Crépuscule de velours",
        "Rêve d'améthyste",
        "Fuchsia Noir",
        "Sable doré",
        "Bleu profond",
        "Rose poudré",
        "Vert sauge",
        "Terracotta chaleureux",
        "Noir et blanc",
        "Bleu canard",
        "Mauve doux",
        "Jaune moutarde",
        "Vert forêt",
        "Rouge brique",
        "Bleu ciel",
    )

    val designModes = listOf(
        "Conserver la structure" to "Gardez les murs, ouvertures et volumes en place tout en améliorant le style.",
        "Rénover librement" to "Autorisez l'IA à proposer une transformation plus ambitieuse et créative.",
    )

    val diamondPacks = listOf(
        DiamondPack("starter", "Starter", 50, "$1.99", description = "Enough for multiple room explorations."),
        DiamondPack("designer", "Creator", 150, "$4.99", "POPULAR", "The best balance for a full creative session."),
        DiamondPack("architect", "Pro", 400, "$9.99", "BEST VALUE", "Built for concept series, variants, and portfolios."),
        DiamondPack("estate", "Ultimate", 1000, "$19.99", description = "Deep credits for large projects and heavy usage."),
    )

    val gallery = tools.mapIndexed { index, tool ->
        GalleryItem(
            id = tool.id,
            title = tool.title,
            category = if (index < 3) "Spaces" else "Tools",
            imageRes = tool.imageRes,
        )
    }

    private fun sectionItems(idPrefix: String, vararg imageRes: Int): List<GalleryItem> =
        imageRes.mapIndexed { index, image ->
            GalleryItem("$idPrefix-${index + 1}", idPrefix, idPrefix, image)
        }

    val discoverSections = listOf(
        // ── Interior ──
        DiscoverSection(
            id = "kitchen",
            title = "Kitchen",
            cluster = "interior",
            serviceToolId = "interior",
            items = sectionItems(
                "kitchen",
                R.drawable.assets_media_discover_generated_kitchen_kitchen1,
                R.drawable.assets_media_discover_generated_kitchen_kitchen2,
                R.drawable.assets_media_discover_generated_kitchen_kitchen3,
                R.drawable.assets_media_discover_generated_kitchen_kitchen4,
                R.drawable.assets_media_discover_generated_kitchen_kitchen5,
                R.drawable.assets_media_discover_generated_kitchen_kitchen6,
                R.drawable.assets_media_discover_generated_kitchen_kitchen7,
            ),
        ),
        DiscoverSection(
            id = "living-room",
            title = "Living Room",
            cluster = "interior",
            serviceToolId = "interior",
            items = sectionItems(
                "living-room",
                R.drawable.assets_media_discover_generated_livingroom_livingroom1,
                R.drawable.assets_media_discover_generated_livingroom_livingroom2,
                R.drawable.assets_media_discover_generated_livingroom_livingroom3,
                R.drawable.assets_media_discover_generated_livingroom_livingroom4,
                R.drawable.assets_media_discover_generated_livingroom_livingroom5,
                R.drawable.assets_media_discover_generated_livingroom_livingroom6,
                R.drawable.assets_media_discover_generated_livingroom_livingroom7,
            ),
        ),
        DiscoverSection(
            id = "bedroom",
            title = "Bedroom",
            cluster = "interior",
            serviceToolId = "interior",
            items = sectionItems(
                "bedroom",
                R.drawable.assets_media_discover_generated_bedroom_bedroom1,
                R.drawable.assets_media_discover_generated_bedroom_bedroom2,
                R.drawable.assets_media_discover_generated_bedroom_bedroom3,
                R.drawable.assets_media_discover_generated_bedroom_bedroom4,
                R.drawable.assets_media_discover_generated_bedroom_bedroom5,
                R.drawable.assets_media_discover_generated_bedroom_bedroom6,
                R.drawable.assets_media_discover_generated_bedroom_bedroom7,
            ),
        ),
        DiscoverSection(
            id = "bathroom",
            title = "Bathroom",
            cluster = "interior",
            serviceToolId = "interior",
            items = sectionItems(
                "bathroom",
                R.drawable.assets_media_discover_home_homebathroom,
                R.drawable.assets_media_discover_wallscenes_lavendermistbath,
                R.drawable.assets_media_styles_styleluxury,
            ),
        ),
        DiscoverSection(
            id = "office",
            title = "Office",
            cluster = "interior",
            serviceToolId = "interior",
            items = sectionItems(
                "office",
                R.drawable.assets_media_discover_home_homehomeoffice,
                R.drawable.assets_media_discover_home_homestudy,
                R.drawable.assets_media_styles_stylemidcentury,
            ),
        ),
        DiscoverSection(
            id = "dining",
            title = "Dining Room",
            cluster = "interior",
            serviceToolId = "interior",
            items = sectionItems(
                "dining",
                R.drawable.assets_media_discover_home_homediningroom,
                R.drawable.assets_media_styles_styleartdeco,
                R.drawable.assets_media_styles_stylemediterranean,
            ),
        ),

        // ── Architecture ──
        DiscoverSection(
            id = "modern-house",
            title = "Modern House",
            cluster = "architecture",
            serviceToolId = "facade",
            items = sectionItems(
                "modern-house",
                R.drawable.assets_media_discover_generated_exterior_exterior1,
                R.drawable.assets_media_discover_generated_exterior_exterior2,
                R.drawable.assets_media_discover_generated_exterior_exterior3,
            ),
        ),
        DiscoverSection(
            id = "classic-house",
            title = "Classic House",
            cluster = "architecture",
            serviceToolId = "facade",
            items = sectionItems(
                "classic-house",
                R.drawable.assets_media_discover_exterior_exteriorstonemanor,
                R.drawable.assets_media_discover_generated_exterior_exterior7,
                R.drawable.tool_exterior,
            ),
        ),
        DiscoverSection(
            id = "apartment",
            title = "Apartment",
            cluster = "architecture",
            serviceToolId = "facade",
            items = sectionItems(
                "apartment",
                R.drawable.assets_media_discover_exterior_exteriorapartmentblock,
                R.drawable.assets_media_discover_generated_exterior_exterior4,
                R.drawable.assets_media_discover_generated_exterior_exterior5,
            ),
        ),
        DiscoverSection(
            id = "villa",
            title = "Villa",
            cluster = "architecture",
            serviceToolId = "facade",
            items = sectionItems(
                "villa",
                R.drawable.assets_media_discover_exterior_exteriormodernvilla,
                R.drawable.assets_media_discover_generated_exterior_exterior6,
            ),
        ),
        DiscoverSection(
            id = "cabin",
            title = "Cabin",
            cluster = "architecture",
            serviceToolId = "facade",
            items = sectionItems(
                "cabin",
                R.drawable.assets_media_discover_exterior_exteriorpoolhouse,
                R.drawable.assets_media_discover_exterior_exteriorglassoffice,
                R.drawable.assets_media_discover_exterior_exteriorretailstorefront,
            ),
        ),

        // ── Landscape ──
        DiscoverSection(
            id = "garden",
            title = "Garden",
            cluster = "landscape",
            serviceToolId = "garden",
            items = sectionItems(
                "garden",
                R.drawable.assets_media_discover_garden_gardenfiresidepatio,
                R.drawable.assets_media_discover_garden_gardenbackyard,
                R.drawable.assets_media_discover_generated_garden_garden1,
                R.drawable.assets_media_discover_generated_garden_garden2,
            ),
        ),
        DiscoverSection(
            id = "patio",
            title = "Patio",
            cluster = "landscape",
            serviceToolId = "garden",
            items = sectionItems(
                "patio",
                R.drawable.assets_media_discover_garden_gardenpatio,
                R.drawable.assets_media_discover_garden_gardenterrace,
                R.drawable.assets_media_discover_generated_garden_garden3,
            ),
        ),
        DiscoverSection(
            id = "pool",
            title = "Pool Area",
            cluster = "landscape",
            serviceToolId = "garden",
            items = sectionItems(
                "pool",
                R.drawable.assets_media_discover_garden_gardenswimmingpool,
                R.drawable.assets_media_discover_garden_gardenpoolcourtyard,
                R.drawable.assets_media_discover_generated_garden_garden4,
            ),
        ),
        DiscoverSection(
            id = "rooftop",
            title = "Rooftop",
            cluster = "landscape",
            serviceToolId = "garden",
            items = sectionItems(
                "rooftop",
                R.drawable.assets_media_discover_garden_gardendeck,
                R.drawable.assets_media_discover_generated_garden_garden5,
                R.drawable.assets_media_discover_generated_garden_garden6,
            ),
        ),
        DiscoverSection(
            id = "balcony",
            title = "Balcony",
            cluster = "landscape",
            serviceToolId = "garden",
            items = sectionItems(
                "balcony",
                R.drawable.assets_media_discover_garden_gardenfrontyard,
                R.drawable.assets_media_discover_garden_gardenvillaentry,
                R.drawable.assets_media_discover_generated_garden_garden7,
            ),
        ),
    )
}
