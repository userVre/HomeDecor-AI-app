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
        "Anglais",
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
        DiamondPack("starter", "Découverte", 10, "19,80 MAD", description = "Pour tester plusieurs idées sans engagement."),
        DiamondPack("designer", "Designer", 30, "49,65 MAD", "POPULAIRE", "Le meilleur équilibre pour explorer une pièce complète."),
        DiamondPack("architect", "Architecte", 100, "129,25 MAD", description = "Pensé pour les séries de concepts et variantes."),
        DiamondPack("estate", "Studio", 300, "249,00 MAD", "MEILLEURE OFFRE", "Crédits profonds pour gros projets et portfolios."),
    )

    val gallery = tools.mapIndexed { index, tool ->
        GalleryItem(
            id = tool.id,
            title = tool.title,
            category = if (index < 3) "Spaces" else "Tools",
            imageRes = tool.imageRes,
        )
    }

    private fun numberedDiscoverItems(idPrefix: String, category: String, vararg imageRes: Int): List<GalleryItem> =
        imageRes.mapIndexed { index, image ->
            val number = index + 1
            GalleryItem("$idPrefix-$number", "$category $number", category, image)
        }

    val discoverSections = listOf(
        DiscoverSection(
            id = "kitchen",
            title = "Cuisine",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "kitchen",
                "Cuisine",
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
            title = "Salon",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "living",
                "Salon",
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
            title = "Chambre",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "bedroom",
                "Chambre",
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
            title = "Salle de bain",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "bathroom",
                "Salle de bain",
                R.drawable.assets_media_discover_home_homebathroom,
                R.drawable.assets_media_discover_wallscenes_lavendermistbath,
                R.drawable.assets_media_styles_styleluxury,
            ),
        ),
        DiscoverSection(
            id = "dining",
            title = "Salle à manger",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "dining",
                "Salle à manger",
                R.drawable.assets_media_discover_home_homediningroom,
                R.drawable.assets_media_styles_styleartdeco,
                R.drawable.assets_media_styles_stylemediterranean,
            ),
        ),
        DiscoverSection(
            id = "home-office",
            title = "Bureau à domicile",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "office",
                "Bureau",
                R.drawable.assets_media_discover_home_homehomeoffice,
                R.drawable.assets_media_discover_home_homestudy,
                R.drawable.assets_media_styles_stylemidcentury,
            ),
        ),
        DiscoverSection(
            id = "library",
            title = "Bibliothèque",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "library",
                "Bibliothèque",
                R.drawable.assets_media_discover_home_homelibrary,
                R.drawable.assets_media_styles_stylevintage,
                R.drawable.assets_media_styles_stylerustic,
            ),
        ),
        DiscoverSection(
            id = "hall",
            title = "Entrée / couloir",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "hall",
                "Entrée",
                R.drawable.assets_media_discover_home_homehall,
                R.drawable.assets_media_styles_stylefrenchcountry,
                R.drawable.assets_media_styles_stylecoastal,
            ),
        ),
        DiscoverSection(
            id = "gaming",
            title = "Salle de jeux",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "gaming",
                "Loisir",
                R.drawable.assets_media_discover_home_homegamingroom,
                R.drawable.assets_media_styles_stylecyberpunk,
                R.drawable.assets_media_styles_stylemodern,
            ),
        ),
        DiscoverSection(
            id = "laundry",
            title = "Blanchisserie",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "laundry",
                "Service",
                R.drawable.assets_media_discover_home_homelaundry,
                R.drawable.assets_media_styles_stylescandinavian,
                R.drawable.assets_media_styles_styleminimalist,
            ),
        ),
        DiscoverSection(
            id = "villa",
            title = "Villa",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "villa",
                "Villa",
                R.drawable.assets_media_discover_exterior_exteriormodernvilla,
                R.drawable.assets_media_discover_generated_exterior_exterior1,
                R.drawable.assets_media_discover_generated_exterior_exterior2,
            ),
        ),
        DiscoverSection(
            id = "house",
            title = "Maison",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "house",
                "Maison",
                R.drawable.tool_exterior,
                R.drawable.assets_media_discover_generated_exterior_exterior7,
            ),
        ),
        DiscoverSection(
            id = "apartment",
            title = "Appartement",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "apartment",
                "Appartement",
                R.drawable.assets_media_discover_exterior_exteriorapartmentblock,
                R.drawable.assets_media_discover_generated_exterior_exterior3,
            ),
        ),
        DiscoverSection(
            id = "office-building",
            title = "Immeuble de bureaux",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "office-building",
                "Immeuble de bureaux",
                R.drawable.assets_media_discover_exterior_exteriorglassoffice,
                R.drawable.assets_media_discover_generated_exterior_exterior4,
            ),
        ),
        DiscoverSection(
            id = "retail",
            title = "Vente au détail",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "retail",
                "Vente au détail",
                R.drawable.assets_media_discover_exterior_exteriorretailstorefront,
                R.drawable.assets_media_discover_generated_exterior_exterior5,
            ),
        ),
        DiscoverSection(
            id = "residential",
            title = "Résidentiel",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "residential",
                "Résidentiel",
                R.drawable.assets_media_discover_exterior_exteriorpoolhouse,
                R.drawable.assets_media_discover_exterior_exteriorstonemanor,
                R.drawable.assets_media_discover_generated_exterior_exterior6,
            ),
        ),
        DiscoverSection(
            id = "wall-scenes",
            title = "Murs",
            cluster = "Intérieurs",
            serviceToolId = "paint",
            items = listOf(
                GalleryItem("wall-1", "Ivoire doux", "Mur", R.drawable.assets_media_discover_wallscenes_softivorykitchen),
                GalleryItem("wall-2", "Vert sauge", "Mur", R.drawable.assets_media_discover_wallscenes_sagegreensuite),
                GalleryItem("wall-3", "Bleu nuit", "Mur", R.drawable.assets_media_discover_wallscenes_midnightnavybedroom),
                GalleryItem("wall-4", "Charbon galerie", "Mur", R.drawable.assets_media_discover_wallscenes_gallerycharcoallounge),
                GalleryItem("wall-5", "Terre cuite", "Mur", R.drawable.assets_media_discover_wallscenes_terracottadining),
                GalleryItem("wall-6", "Rose poudré", "Mur", R.drawable.assets_media_discover_wallscenes_dustyroseretreat),
                GalleryItem("wall-7", "Vert olive", "Mur", R.drawable.assets_media_discover_wallscenes_deepolivestudy),
                GalleryItem("wall-8", "Gris perle", "Mur", R.drawable.assets_media_discover_wallscenes_pearlgraysalon),
            ),
        ),
        DiscoverSection(
            id = "floors",
            title = "Sols",
            cluster = "Intérieurs",
            serviceToolId = "floor",
            items = listOf(
                GalleryItem("floor-1", "Chêne naturel", "Sol", R.drawable.assets_media_discover_floorscenes_naturaloakparquet),
                GalleryItem("floor-2", "Noyer", "Sol", R.drawable.assets_media_discover_floorscenes_heritagewalnutplank),
                GalleryItem("floor-3", "Marbre", "Sol", R.drawable.assets_media_discover_floorscenes_polishedcarraramarble),
                GalleryItem("floor-4", "Béton poli", "Sol", R.drawable.assets_media_discover_floorscenes_industrialgrayconcrete),
                GalleryItem("floor-5", "Chevron", "Sol", R.drawable.assets_media_discover_floorscenes_walnutchevron),
                GalleryItem("floor-6", "Terre cuite", "Sol", R.drawable.assets_media_discover_floorscenes_terracottaateliertile),
                GalleryItem("floor-7", "Carrelage ardoise", "Sol", R.drawable.assets_media_discover_floorscenes_modernslatetile),
                GalleryItem("floor-8", "Tapis ivoire", "Sol", R.drawable.assets_media_discover_floorscenes_plushivorycarpet),
                GalleryItem("floor-9", "Chêne patiné", "Sol", R.drawable.assets_media_discover_floorscenes_weatheredoakstudio),
            ),
        ),
        DiscoverSection(
            id = "garden",
            title = "Jardin",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "garden",
                "Jardin",
                R.drawable.assets_media_discover_garden_gardenfiresidepatio,
                R.drawable.assets_media_discover_generated_garden_garden1,
                R.drawable.assets_media_discover_generated_garden_garden2,
            ),
        ),
        DiscoverSection(
            id = "backyard",
            title = "Cour arrière",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "backyard",
                "Cour arrière",
                R.drawable.assets_media_discover_garden_gardenbackyard,
                R.drawable.assets_media_discover_generated_garden_garden3,
            ),
        ),
        DiscoverSection(
            id = "terrace",
            title = "Terrasse",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "terrace",
                "Terrasse",
                R.drawable.assets_media_discover_garden_gardenterrace,
                R.drawable.assets_media_discover_garden_gardendeck,
            ),
        ),
        DiscoverSection(
            id = "patio",
            title = "Patio",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "patio",
                "Patio",
                R.drawable.assets_media_discover_garden_gardenpatio,
                R.drawable.assets_media_discover_generated_garden_garden4,
            ),
        ),
        DiscoverSection(
            id = "yard",
            title = "Cour",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "yard",
                "Cour",
                R.drawable.assets_media_discover_generated_garden_garden5,
                R.drawable.assets_media_discover_generated_garden_garden6,
                R.drawable.assets_media_discover_generated_garden_garden7,
            ),
        ),
        DiscoverSection(
            id = "pool",
            title = "Piscine",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "pool",
                "Piscine",
                R.drawable.assets_media_discover_garden_gardenswimmingpool,
                R.drawable.assets_media_discover_garden_gardenpoolcourtyard,
            ),
        ),
        DiscoverSection(
            id = "front-garden",
            title = "Jardin avant",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "front-garden",
                "Jardin avant",
                R.drawable.assets_media_discover_garden_gardenfrontyard,
                R.drawable.assets_media_discover_garden_gardenvillaentry,
            ),
        ),
    )
}
