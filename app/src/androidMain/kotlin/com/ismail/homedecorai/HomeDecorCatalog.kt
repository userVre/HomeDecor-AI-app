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

    // ── Image generation prompts for thumbnails ──────────────────────────────
    // Used by AI to generate preview thumbnails for materials and styles.

    val floorMaterialPrompts = mapOf(
        "Carrara Marble" to "Polished Carrara marble floor with subtle grey veining, natural stone texture, warm ambient lighting, photorealistic interior photography",
        "Oak Wood" to "Natural oak parquet floor, warm honey tones, visible wood grain, Scandinavian interior, soft daylight, photorealistic",
        "Walnut" to "Heritage walnut wood plank flooring, rich dark brown tones, elegant grain pattern, luxury interior, warm lighting, photorealistic",
        "Concrete" to "Industrial polished concrete floor, smooth grey surface, subtle aggregate texture, modern minimalist interior, natural light, photorealistic",
        "Limewash" to "Limewash wall finish, warm ivory tones, soft chalky texture, Mediterranean interior, natural daylight, photorealistic",
        "Terrazzo" to "Terrazzo tile flooring, terrazzo pattern with colorful stone chips, Italian design, bright interior, photorealistic",
        "White Tile" to "White ceramic tile flooring, clean glossy finish, modern bathroom or kitchen, bright natural light, photorealistic",
        "Black Tile" to "Black matte tile flooring, sophisticated dark finish, luxury bathroom, moody ambient lighting, photorealistic",
        "Warm Beige" to "Warm beige painted wall, soft neutral tone, cozy living room, natural daylight, photorealistic interior",
        "Dark Elegant" to "Dark elegant painted wall, deep charcoal grey, sophisticated mood, dramatic accent lighting, photorealistic interior",
    )

    val wallMaterialPrompts = mapOf(
        "Limewash" to "Limewash wall finish, warm ivory tones, soft chalky texture, Mediterranean interior, natural daylight, photorealistic",
        "White Tile" to "White subway tile wall, clean glossy finish, modern kitchen backsplash, bright natural light, photorealistic",
        "Black Tile" to "Black hexagonal tile wall, sophisticated dark finish, luxury bathroom, moody ambient lighting, photorealistic",
        "Warm Beige" to "Warm beige painted wall, soft neutral tone, cozy living room, natural daylight, photorealistic interior",
        "Dark Elegant" to "Dark elegant painted wall, deep charcoal grey, sophisticated mood, dramatic accent lighting, photorealistic interior",
    )

    val interiorStylePrompts = mapOf(
        "Moderne" to "Modern interior design, clean lines, neutral palette, minimal furniture, open space, natural light, photorealistic room photography",
        "Luxe" to "Luxury interior design, rich textures, gold accents, velvet upholstery, crystal lighting, opulent decor, photorealistic",
        "Japandi" to "Japandi interior design, minimalist Japanese-Scandinavian fusion, natural materials, warm wood, neutral tones, photorealistic",
        "Cyberpunk" to "Cyberpunk interior design, neon accents, dark surfaces, futuristic furniture, LED lighting, tech-inspired decor, photorealistic",
        "Tropicale" to "Tropical interior design, lush green plants, natural rattan, vibrant patterns, warm wood, resort-style decor, photorealistic",
        "Minimaliste" to "Minimalist interior design, essential furniture only, white and neutral palette, clean surfaces, zen atmosphere, photorealistic",
        "Scandinave" to "Scandinavian interior design, light wood, white walls, cozy textiles, hygge atmosphere, natural daylight, photorealistic",
        "Bohème" to "Bohemian interior design, eclectic patterns, layered textiles, warm earth tones, plants, vintage decor, photorealistic",
        "Midcentury" to "Mid-century modern interior design, retro furniture, warm wood, geometric patterns, iconic design pieces, photorealistic",
        "Art Deco" to "Art Deco interior design, geometric patterns, rich jewel tones, gold accents, luxurious materials, glamour, photorealistic",
        "Côtier" to "Coastal interior design, blue and white palette, natural textures, light airy atmosphere, beach-inspired decor, photorealistic",
        "Rustique" to "Rustic interior design, exposed wood beams, natural stone, warm earth tones, farmhouse charm, photorealistic",
        "Vintage" to "Vintage interior design, retro furniture, muted pastel tones, nostalgic decor, classic patterns, photorealistic",
        "Méditerranéen" to "Mediterranean interior design, terracotta tones, arched doorways, warm plaster walls, wrought iron details, photorealistic",
        "Glam" to "Glam interior design, metallic accents, plush velvet, dramatic lighting, mirrored surfaces, luxury decor, photorealistic",
        "Campagne française" to "French country interior design, soft floral patterns, antique furniture, warm wood, romantic atmosphere, photorealistic",
    )

    val exteriorStylePrompts = mapOf(
        "Appartement" to "Modern apartment building exterior, clean facade, large windows, contemporary architecture, urban setting, photorealistic",
        "Maison" to "Modern villa exterior, clean lines, large glass windows, pool area, minimalist landscaping, photorealistic",
        "Immeuble de bureaux" to "Glass office building exterior, modern corporate architecture, reflective facade, urban skyline, photorealistic",
        "Résidentiel" to "Residential house exterior, warm stone facade, landscaped garden, family home, welcoming entrance, photorealistic",
        "Vente au détail" to "Retail storefront exterior, modern commercial design, large display windows, inviting entrance, photorealistic",
        "Villa" to "Luxury villa exterior, Mediterranean style, terracotta roof, landscaped grounds, pool area, photorealistic",
    )

    val gardenStylePrompts = mapOf(
        "Moderne" to "Modern garden design, clean geometric lines, structured planting, minimalist hardscaping, outdoor lighting, photorealistic",
        "Tropicale" to "Tropical garden design, lush exotic plants, palm trees, vibrant flowers, natural stone path, resort-style, photorealistic",
        "Minimaliste" to "Minimalist garden design, simple plant palette, gravel paths, clean boundaries, zen atmosphere, photorealistic",
        "Méditerranéen" to "Mediterranean garden design, terracotta pots, olive trees, lavender, natural stone, warm tones, photorealistic",
        "Japandi" to "Japandi garden design, Japanese-Scandinavian fusion, minimalist water feature, natural materials, peaceful atmosphere, photorealistic",
        "Rustique" to "Rustic garden design, natural wildflower meadow, stone walls, wooden fence, cottage garden charm, photorealistic",
        "Zen" to "Zen garden design, raked gravel, smooth stones, bamboo, bonsai, meditation space, tranquil atmosphere, photorealistic",
        "Jardin anglais" to "English cottage garden design, mixed flower borders, climbing roses, brick path, arbor, romantic atmosphere, photorealistic",
        "Paysage" to "Professional landscape design, layered planting, seasonal color, mature trees, manicured lawn, photorealistic",
        "Bohème" to "Bohemian garden design, eclectic plant mix, colorful textiles, fairy lights, upcycled decor, free-spirited atmosphere, photorealistic",
        "Scandinave" to "Scandinavian garden design, clean lines, native plants, light wood elements, hygge outdoor space, photorealistic",
    )

    val replacementStylePrompts = mapOf(
        "Remplacer le sofa" to "modern sofa matching the room scale, perspective, and light",
        "Remplacer la table" to "refined table matching the room scale, perspective, and light",
        "Remplacer la lampe" to "elegant lamp matching the room scale, perspective, and light",
        "Remplacer le tapis" to "textured area rug matching the room scale, perspective, and light",
        "Remplacer l'art mural" to "framed wall art matching the room scale, perspective, and light",
        "Remplacer la plante" to "healthy indoor plant matching the room scale, perspective, and light",
        "Remplacer la chaise" to "comfortable accent chair matching the room scale, perspective, and light",
        "Remplacer le cabinet" to "streamlined cabinet matching the room scale, perspective, and light",
    )

    val gallery = tools.mapIndexed { index, tool ->
        GalleryItem(
            id = tool.id,
            title = tool.title,
            category = if (index < 3) "Spaces" else "Tools",
            imageRes = tool.imageRes,
        )
    }

    private fun sectionItems(
        idPrefix: String,
        styleTypes: List<String>,
        descriptions: List<String>,
        rooms: List<String> = emptyList(),
        colors: List<String> = emptyList(),
        moods: List<String> = emptyList(),
        vararg imageRes: Int,
    ): List<GalleryItem> =
        imageRes.mapIndexed { index, image ->
            GalleryItem(
                id = "$idPrefix-${index + 1}",
                title = idPrefix,
                category = idPrefix,
                styleType = styleTypes.getOrElse(index) { "Modern" },
                description = descriptions.getOrElse(index) { "$idPrefix design" },
                imageRes = image,
                room = rooms.getOrElse(index) { "" },
                color = colors.getOrElse(index) { "" },
                mood = moods.getOrElse(index) { "" },
            )
        }

    private fun discoverSection(
        id: String,
        cluster: String,
        serviceToolId: String,
        items: List<GalleryItem>,
    ) = DiscoverSection(id = id, title = id, cluster = cluster, serviceToolId = serviceToolId, items = items)

    val discoverSections = listOf(
        // ── Interior ──
        DiscoverSection(
            id = "kitchen",
            title = "Kitchen",
            cluster = "interior",
            serviceToolId = "interior",
            items = sectionItems(
                "kitchen",
                styleTypes = listOf("Modern", "Minimalist", "Contemporary", "Scandinavian", "Transitional", "Coastal", "Farmhouse"),
                descriptions = listOf(
                    "Modern kitchen with island and pendant lighting",
                    "Minimalist kitchen with clean lines and neutral palette",
                    "Contemporary kitchen with warm wood accents",
                    "Scandinavian kitchen with bright natural light",
                    "Transitional kitchen blending traditional and modern elements",
                    "Coastal kitchen with light blue cabinetry",
                    "Farmhouse kitchen with rustic charm and open shelving",
                ),
                rooms = listOf("Kitchen", "Kitchen", "Kitchen", "Kitchen", "Kitchen", "Kitchen", "Kitchen"),
                colors = listOf("White", "Neutral", "Wood", "White", "Beige", "Blue", "Cream"),
                moods = listOf("Sleek", "Calm", "Warm", "Bright", "Balanced", "Breezy", "Rustic"),
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
                styleTypes = listOf("Modern", "Scandinavian", "Contemporary", "Minimalist", "Transitional", "Coastal", "Bohemian"),
                descriptions = listOf(
                    "Modern living room with clean lines and neutral tones",
                    "Scandinavian living room with cozy textiles and light wood",
                    "Contemporary living room with statement lighting",
                    "Minimalist living room with curated furnishings",
                    "Transitional living room blending classic and modern pieces",
                    "Coastal living room with breezy blue accents",
                    "Bohemian living room with eclectic patterns and plants",
                ),
                rooms = listOf("Living Room", "Living Room", "Living Room", "Living Room", "Living Room", "Living Room", "Living Room"),
                colors = listOf("Neutral", "White", "Gray", "White", "Beige", "Blue", "Terracotta"),
                moods = listOf("Sleek", "Cozy", "Bold", "Calm", "Balanced", "Breezy", "Eclectic"),
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
                styleTypes = listOf("Modern", "Minimalist", "Scandinavian", "Contemporary", "Luxury", "Coastal", "Transitional"),
                descriptions = listOf(
                    "Modern bedroom with platform bed and soft lighting",
                    "Minimalist bedroom with clean serene aesthetic",
                    "Scandinavian bedroom with warm wood and layered textiles",
                    "Contemporary bedroom with accent wall",
                    "Luxury bedroom with elegant drapery",
                    "Coastal bedroom with light airy palette",
                    "Transitional bedroom balanced and timeless",
                ),
                rooms = listOf("Bedroom", "Bedroom", "Bedroom", "Bedroom", "Bedroom", "Bedroom", "Bedroom"),
                colors = listOf("Gray", "White", "Wood", "Navy", "Gold", "Blue", "Beige"),
                moods = listOf("Calm", "Serene", "Cozy", "Bold", "Luxurious", "Breezy", "Balanced"),
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
                styleTypes = listOf("Modern", "Spa", "Contemporary"),
                descriptions = listOf(
                    "Modern bathroom with floating vanity and clean lines",
                    "Spa-inspired bathroom with lavender tones and soaking tub",
                    "Contemporary bathroom with green tile accents and natural light",
                ),
                rooms = listOf("Bathroom", "Bathroom", "Bathroom"),
                colors = listOf("White", "Lavender", "Green"),
                moods = listOf("Sleek", "Serene", "Fresh"),
                R.drawable.assets_media_discover_home_homebathroom,
                R.drawable.assets_media_discover_wallscenes_lavendermistbath,
                R.drawable.assets_media_discover_wallscenes_sagegreensuite,
            ),
        ),
        DiscoverSection(
            id = "office",
            title = "Office",
            cluster = "interior",
            serviceToolId = "interior",
            items = sectionItems(
                "office",
                styleTypes = listOf("Modern", "Traditional", "Contemporary"),
                descriptions = listOf(
                    "Modern home office with desk and shelving",
                    "Traditional study with warm wood paneling",
                    "Contemporary study with olive green accent wall",
                ),
                rooms = listOf("Office", "Office", "Office"),
                colors = listOf("White", "Wood", "Green"),
                moods = listOf("Focused", "Classic", "Creative"),
                R.drawable.assets_media_discover_home_homehomeoffice,
                R.drawable.assets_media_discover_home_homestudy,
                R.drawable.assets_media_discover_wallscenes_deepolivestudy,
            ),
        ),
        DiscoverSection(
            id = "dining",
            title = "Dining Room",
            cluster = "interior",
            serviceToolId = "interior",
            items = sectionItems(
                "dining",
                styleTypes = listOf("Modern", "Contemporary", "Formal"),
                descriptions = listOf(
                    "Modern dining room with centerpiece table setting",
                    "Contemporary dining room with terracotta accent wall",
                    "Formal dining room in soft neutral tones",
                ),
                rooms = listOf("Dining Room", "Dining Room", "Dining Room"),
                colors = listOf("White", "Terracotta", "Neutral"),
                moods = listOf("Sleek", "Warm", "Elegant"),
                R.drawable.assets_media_discover_home_homediningroom,
                R.drawable.assets_media_discover_wallscenes_terracottadining,
                R.drawable.assets_media_discover_wallscenes_pearlgraysalon,
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
                styleTypes = listOf("Contemporary", "Minimalist", "Glass", "Mid-Century"),
                descriptions = listOf(
                    "Contemporary house with clean geometric facade",
                    "Minimalist house with warm exterior lighting",
                    "Glass-front house with modern landscaping",
                    "Mid-century inspired house with flat roof",
                ),
                rooms = listOf("Exterior", "Exterior", "Exterior", "Exterior"),
                colors = listOf("White", "Warm White", "Glass", "Wood"),
                moods = listOf("Sleek", "Calm", "Bold", "Retro"),
                R.drawable.assets_media_discover_generated_exterior_exterior1,
                R.drawable.assets_media_discover_generated_exterior_exterior2,
                R.drawable.assets_media_discover_generated_exterior_exterior3,
                R.drawable.assets_media_discover_generated_exterior_exterior4,
            ),
        ),
        DiscoverSection(
            id = "classic-house",
            title = "Classic House",
            cluster = "architecture",
            serviceToolId = "facade",
            items = sectionItems(
                "classic-house",
                styleTypes = listOf("Stone Manor", "Traditional", "Modern Traditional"),
                descriptions = listOf(
                    "Elegant stone manor house with classic architecture",
                    "Traditional house with gabled roof and warm tones",
                    "Modern traditional house blending classic proportions",
                ),
                rooms = listOf("Exterior", "Exterior", "Exterior"),
                colors = listOf("Stone", "Cream", "Beige"),
                moods = listOf("Elegant", "Timeless", "Refined"),
                R.drawable.assets_media_discover_exterior_exteriorstonemanor,
                R.drawable.assets_media_discover_generated_exterior_exterior7,
                R.drawable.assets_media_discover_generated_exterior_exterior5,
            ),
        ),
        DiscoverSection(
            id = "apartment",
            title = "Apartment",
            cluster = "architecture",
            serviceToolId = "facade",
            items = sectionItems(
                "apartment",
                styleTypes = listOf("Modern Block", "Contemporary", "Urban"),
                descriptions = listOf(
                    "Modern apartment block with clean facade",
                    "Contemporary apartment with balcony details",
                    "Urban apartment building with rooftop features",
                ),
                rooms = listOf("Exterior", "Exterior", "Exterior"),
                colors = listOf("Gray", "White", "Concrete"),
                moods = listOf("Urban", "Modern", "Dynamic"),
                R.drawable.assets_media_discover_exterior_exteriorapartmentblock,
                R.drawable.assets_media_discover_generated_exterior_exterior6,
                R.drawable.assets_media_discover_generated_exterior_exterior2,
            ),
        ),
        DiscoverSection(
            id = "villa",
            title = "Villa",
            cluster = "architecture",
            serviceToolId = "facade",
            items = sectionItems(
                "villa",
                styleTypes = listOf("Modern", "Luxury", "Mediterranean"),
                descriptions = listOf(
                    "Modern villa with clean lines and pool",
                    "Luxury villa with dramatic evening lighting",
                    "Mediterranean-inspired villa exterior",
                ),
                rooms = listOf("Exterior", "Exterior", "Exterior"),
                colors = listOf("White", "Gold", "Terracotta"),
                moods = listOf("Sleek", "Luxurious", "Warm"),
                R.drawable.assets_media_discover_exterior_exteriormodernvilla,
                R.drawable.assets_media_discover_generated_exterior_exterior7,
                R.drawable.assets_media_discover_generated_exterior_exterior1,
            ),
        ),
        DiscoverSection(
            id = "cabin",
            title = "Cabin",
            cluster = "architecture",
            serviceToolId = "facade",
            items = sectionItems(
                "cabin",
                styleTypes = listOf("Rustic", "Woodland"),
                descriptions = listOf(
                    "Rustic cabin with stone facade and warm lighting",
                    "Woodland cabin retreat surrounded by nature",
                ),
                rooms = listOf("Exterior", "Exterior"),
                colors = listOf("Wood", "Stone"),
                moods = listOf("Rustic", "Natural"),
                R.drawable.assets_media_discover_exterior_exteriorpoolhouse,
                R.drawable.assets_media_discover_generated_exterior_exterior3,
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
                styleTypes = listOf("Cozy", "Backyard", "Modern", "Tropical"),
                descriptions = listOf(
                    "Cozy garden with fire pit seating area",
                    "Backyard garden with lush lawn and trees",
                    "Modern landscape design with structured planting",
                    "Tropical garden with vibrant foliage",
                ),
                rooms = listOf("Garden", "Garden", "Garden", "Garden"),
                colors = listOf("Green", "Green", "Green", "Tropical"),
                moods = listOf("Cozy", "Natural", "Structured", "Vibrant"),
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
                styleTypes = listOf("Elegant", "Terraced", "Modern"),
                descriptions = listOf(
                    "Elegant patio with comfortable outdoor furniture",
                    "Terraced patio with layered seating areas",
                    "Modern patio with clean lines and planters",
                ),
                rooms = listOf("Patio", "Patio", "Patio"),
                colors = listOf("Stone", "Terracotta", "Gray"),
                moods = listOf("Elegant", "Layered", "Sleek"),
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
                styleTypes = listOf("Luxury", "Courtyard", "Modern"),
                descriptions = listOf(
                    "Luxury swimming pool with lounge area",
                    "Pool courtyard with elegant tilework",
                    "Modern pool design with clean geometric lines",
                ),
                rooms = listOf("Pool", "Pool", "Pool"),
                colors = listOf("Blue", "Turquoise", "White"),
                moods = listOf("Luxurious", "Elegant", "Sleek"),
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
                styleTypes = listOf("Modern", "Urban", "Contemporary"),
                descriptions = listOf(
                    "Modern rooftop deck with seating and planters",
                    "Urban rooftop terrace with panoramic views",
                    "Contemporary rooftop lounge with greenery",
                ),
                rooms = listOf("Rooftop", "Rooftop", "Rooftop"),
                colors = listOf("Gray", "Concrete", "Green"),
                moods = listOf("Sleek", "Urban", "Fresh"),
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
                styleTypes = listOf("Cozy", "Greenery", "Modern"),
                descriptions = listOf(
                    "Cozy balcony with potted plants and seating",
                    "Balcony entrance with lush greenery and stone path",
                    "Modern balcony garden with tropical plants",
                ),
                rooms = listOf("Balcony", "Balcony", "Balcony"),
                colors = listOf("Green", "Stone", "Tropical"),
                moods = listOf("Cozy", "Natural", "Fresh"),
                R.drawable.assets_media_discover_garden_gardenfrontyard,
                R.drawable.assets_media_discover_garden_gardenvillaentry,
                R.drawable.assets_media_discover_generated_garden_garden7,
            ),
        ),
    )
}
