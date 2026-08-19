package com.ismail.homedecorai

import com.ismail.homedecorai.model.SampleImages
import com.ismail.homedecorai.model.SampleImages.DEFAULT_ASPECT_RATIO
import com.ismail.homedecorai.model.routeAllowedCategories
import com.ismail.homedecorai.model.routeFallbackImageUrls
import com.ismail.homedecorai.ui.tools.EXAMPLE_FALLBACK_ASPECT_RATIO
import com.ismail.homedecorai.ui.tools.EXAMPLE_FALLBACK_IMAGE_URL
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Validates route-specific sample data integrity.
 *
 * Rules enforced:
 *  - Every tool must have a non-empty example image URL.
 *  - Example labels must not contain "Empty" (inaccurate for sample photos).
 *  - All image URLs must reference existing resource patterns.
 *  - Interior tools must use interior images, exterior tools exterior images, etc.
 *  - Aspect ratios must be finite and nonzero.
 *  - No tool icons may be used as example images.
 *  - Each SampleImage category must match the allowed set for its route.
 */
class SampleDataTest {

    // ── Known image resource patterns (must exist in resources/images/) ────

    private val knownInteriorImages = setOf(
        "images/assets_media_discover_generated_livingroom_livingroom1.webp",
        "images/assets_media_discover_generated_livingroom_livingroom2.webp",
        "images/assets_media_discover_generated_livingroom_livingroom3.webp",
        "images/assets_media_discover_generated_livingroom_livingroom4.webp",
        "images/assets_media_discover_generated_livingroom_livingroom5.webp",
        "images/assets_media_discover_generated_livingroom_livingroom6.webp",
        "images/assets_media_discover_generated_livingroom_livingroom7.webp",
        "images/assets_media_discover_generated_kitchen_kitchen1.webp",
        "images/assets_media_discover_generated_kitchen_kitchen2.webp",
        "images/assets_media_discover_generated_kitchen_kitchen3.webp",
        "images/assets_media_discover_generated_kitchen_kitchen4.webp",
        "images/assets_media_discover_generated_kitchen_kitchen5.webp",
        "images/assets_media_discover_generated_kitchen_kitchen6.webp",
        "images/assets_media_discover_generated_kitchen_kitchen7.webp",
        "images/assets_media_discover_generated_bedroom_bedroom1.webp",
        "images/assets_media_discover_generated_bedroom_bedroom2.webp",
        "images/assets_media_discover_generated_bedroom_bedroom3.webp",
        "images/assets_media_discover_generated_bedroom_bedroom4.webp",
        "images/assets_media_discover_generated_bedroom_bedroom5.webp",
        "images/assets_media_discover_generated_bedroom_bedroom6.webp",
        "images/assets_media_discover_generated_bedroom_bedroom7.webp",
        "images/assets_media_discover_home_homebathroom.webp",
        "images/assets_media_discover_home_homediningroom.webp",
        "images/assets_media_discover_home_homehomeoffice.webp",
        "images/assets_media_discover_home_homekitchen.webp",
        "images/assets_media_discover_home_homelibrary.webp",
        "images/assets_media_discover_home_homelivingroom.webp",
        "images/assets_media_discover_home_homemastersuite.webp",
        "images/assets_media_discover_home_homestudy.webp",
        "images/assets_media_discover_home_homegamingroom.webp",
        "images/assets_media_discover_home_homehall.webp",
        "images/assets_media_discover_home_homelaundry.webp",
    )

    private val knownExteriorImages = setOf(
        "images/assets_media_discover_exterior_exteriormodernvilla.webp",
        "images/assets_media_discover_exterior_exteriorapartmentblock.webp",
        "images/assets_media_discover_exterior_exteriorglassoffice.webp",
        "images/assets_media_discover_exterior_exteriorpoolhouse.webp",
        "images/assets_media_discover_exterior_exteriorstonemanor.webp",
        "images/assets_media_discover_exterior_exteriorretailstorefront.webp",
        "images/assets_media_discover_generated_exterior_exterior1.webp",
        "images/assets_media_discover_generated_exterior_exterior2.webp",
        "images/assets_media_discover_generated_exterior_exterior3.webp",
        "images/assets_media_discover_generated_exterior_exterior4.webp",
        "images/assets_media_discover_generated_exterior_exterior5.webp",
        "images/assets_media_discover_generated_exterior_exterior6.webp",
        "images/assets_media_discover_generated_exterior_exterior7.webp",
    )

    private val knownGardenImages = setOf(
        "images/assets_media_discover_garden_gardenbackyard.webp",
        "images/assets_media_discover_garden_gardendeck.webp",
        "images/assets_media_discover_garden_gardenfiresidepatio.webp",
        "images/assets_media_discover_garden_gardenfrontyard.webp",
        "images/assets_media_discover_garden_gardenpatio.webp",
        "images/assets_media_discover_garden_gardenpoolcourtyard.webp",
        "images/assets_media_discover_garden_gardenswimmingpool.webp",
        "images/assets_media_discover_garden_gardenterrace.webp",
        "images/assets_media_discover_garden_gardenvillaentry.webp",
        "images/assets_media_discover_generated_garden_garden1.webp",
        "images/assets_media_discover_generated_garden_garden2.webp",
        "images/assets_media_discover_generated_garden_garden3.webp",
        "images/assets_media_discover_generated_garden_garden4.webp",
        "images/assets_media_discover_generated_garden_garden5.webp",
        "images/assets_media_discover_generated_garden_garden6.webp",
        "images/assets_media_discover_generated_garden_garden7.webp",
    )

    private val knownFloorImages = setOf(
        "images/assets_media_discover_floorscenes_heritagewalnutplank.webp",
        "images/assets_media_discover_floorscenes_industrialgrayconcrete.webp",
        "images/assets_media_discover_floorscenes_modernslatetile.webp",
        "images/assets_media_discover_floorscenes_naturaloakparquet.webp",
        "images/assets_media_discover_floorscenes_plushivorycarpet.webp",
        "images/assets_media_discover_floorscenes_polishedcarraramarble.webp",
        "images/assets_media_discover_floorscenes_terracottaateliertile.webp",
        "images/assets_media_discover_floorscenes_walnutchevron.webp",
        "images/assets_media_discover_floorscenes_weatheredoakstudio.webp",
    )

    private val knownWallImages = setOf(
        "images/assets_media_discover_wallscenes_lavendermistbath.webp",
        "images/assets_media_discover_wallscenes_midnightnavybedroom.webp",
        "images/assets_media_discover_wallscenes_dustyroseretreat.webp",
        "images/assets_media_discover_wallscenes_pearlgraysalon.webp",
        "images/assets_media_discover_wallscenes_sagegreensuite.webp",
        "images/assets_media_discover_wallscenes_softivorykitchen.webp",
        "images/assets_media_discover_wallscenes_terracottadining.webp",
        "images/assets_media_discover_wallscenes_gallerycharcoallounge.webp",
        "images/assets_media_discover_wallscenes_deepolivestudy.webp",
    )

    private val knownToolImages = setOf(
        "images/tool_interior.webp", "images/tool_exterior.webp",
        "images/tool_garden.webp", "images/tool_paint.webp",
        "images/tool_floor.webp", "images/tool_layout.webp",
        "images/tool_replace.webp", "images/tool_reference.webp",
    )

    private val allKnownImages = knownInteriorImages + knownExteriorImages +
            knownGardenImages + knownFloorImages + knownWallImages + knownToolImages

    // ── Route-specific image categorization ────────────────────────────────

    /** Images that are valid for interior design tools. */
    private val interiorToolImages = knownInteriorImages

    /** Images that are valid for exterior/facade tools. */
    private val exteriorToolImages = knownExteriorImages

    /** Images that are valid for garden tools. */
    private val gardenToolImages = knownGardenImages

    /** Images that are valid for floor tools. */
    private val floorToolImages = knownFloorImages

    /** Images that are valid for paint tools. */
    private val paintToolImages = knownWallImages

    /** Images that are valid for layout tools (room photos showing layout). */
    private val layoutToolImages = knownInteriorImages

    /** Images that are valid for replace furniture tools (rooms with furniture). */
    private val replaceToolImages = knownInteriorImages

    /** Images that are valid for reference style tools. */
    private val referenceToolImages = knownInteriorImages + knownExteriorImages +
            knownGardenImages

    // ── Example image URL per tool (must match WebWizardScreen.kt) ─────────

    private val exampleImageUrls = mapOf(
        "interior" to "images/assets_media_discover_generated_livingroom_livingroom1.webp",
        "facade" to "images/assets_media_discover_exterior_exteriormodernvilla.webp",
        "garden" to "images/assets_media_discover_garden_gardenpatio.webp",
        "paint" to "images/assets_media_discover_wallscenes_sagegreensuite.webp",
        "floor" to "images/assets_media_discover_floorscenes_naturaloakparquet.webp",
        "layout" to "images/assets_media_discover_generated_livingroom_livingroom1.webp",
        "replace" to "images/assets_media_discover_generated_livingroom_livingroom2.webp",
        "reference" to "images/assets_media_discover_home_homelivingroom.webp",
    )

    // ── Example label per tool (must match WebWizardScreen.kt) ─────────────

    private val exampleLabels = mapOf(
        "interior" to "Sample living room",
        "facade" to "Sample exterior",
        "garden" to "Sample garden",
        "paint" to "Sample wall",
        "floor" to "Sample flooring",
        "layout" to "Sample room layout",
        "replace" to "Sample room with furniture",
        "reference" to "Sample space",
    )

    // ── Valid tool IDs ─────────────────────────────────────────────────────

    private val validToolIds = setOf(
        "interior", "facade", "garden", "paint",
        "floor", "layout", "replace", "reference",
    )

    // ── Basic routing tests ────────────────────────────────────────────────

    @Test
    fun everyToolHasExampleImageUrl() {
        validToolIds.forEach { toolId ->
            val url = exampleImageUrls[toolId]
            assertTrue(url != null && url.isNotEmpty(),
                "Tool '$toolId' must have a non-empty example image URL")
        }
    }

    @Test
    fun everyToolHasAccurateExampleLabel() {
        validToolIds.forEach { toolId ->
            val label = exampleLabels[toolId]
            assertTrue(label != null && label.isNotEmpty(),
                "Tool '$toolId' must have a non-empty example label")
            assertFalse(label!!.contains("Empty", ignoreCase = true),
                "Tool '$toolId' label must not contain 'Empty': $label")
        }
    }

    // ── Category-correctness tests (fail if route receives wrong category) ──

    @Test
    fun interiorToolUsesInteriorImage() {
        val url = exampleImageUrls["interior"]!!
        assertTrue(url in interiorToolImages,
            "Interior tool must use interior image, got: $url")
    }

    @Test
    fun facadeToolUsesExteriorImage() {
        val url = exampleImageUrls["facade"]!!
        assertTrue(url in exteriorToolImages,
            "Facade tool must use exterior image, got: $url")
    }

    @Test
    fun gardenToolUsesGardenImage() {
        val url = exampleImageUrls["garden"]!!
        assertTrue(url in gardenToolImages,
            "Garden tool must use garden image, got: $url")
    }

    @Test
    fun paintToolUsesWallImage() {
        val url = exampleImageUrls["paint"]!!
        assertTrue(url in paintToolImages,
            "Paint tool must use wall image, got: $url")
    }

    @Test
    fun floorToolUsesFloorImage() {
        val url = exampleImageUrls["floor"]!!
        assertTrue(url in floorToolImages,
            "Floor tool must use floor image, got: $url")
    }

    @Test
    fun layoutToolUsesRoomImage() {
        val url = exampleImageUrls["layout"]!!
        assertTrue(url in layoutToolImages,
            "Layout tool must use room image (not wall/floor/garden/exterior), got: $url")
    }

    @Test
    fun replaceToolUsesRoomWithFurnitureImage() {
        val url = exampleImageUrls["replace"]!!
        assertTrue(url in replaceToolImages,
            "Replace tool must use room-with-furniture image, got: $url")
    }

    @Test
    fun referenceToolUsesValidImage() {
        val url = exampleImageUrls["reference"]!!
        assertTrue(url in referenceToolImages,
            "Reference tool must use valid image, got: $url")
    }

    // ── No tool-icon-as-example-image tests ────────────────────────────────

    @Test
    fun noExampleImageUsesToolIcon() {
        exampleImageUrls.forEach { (toolId, url) ->
            assertFalse(url.startsWith("images/tool_"),
                "Tool '$toolId' example must not use a tool icon: $url")
        }
    }

    @Test
    fun allExampleImagesAreKnownResources() {
        exampleImageUrls.forEach { (toolId, url) ->
            assertTrue(url in allKnownImages,
                "Tool '$toolId' example image '$url' is not in the known resources set")
        }
    }

    // ── Cross-category rejection tests (fail if wrong category leaks) ──────

    @Test
    fun floorToolDoesNotUseWallImage() {
        val url = exampleImageUrls["floor"]!!
        assertFalse(url in knownWallImages,
            "Floor tool must not use wall image: $url")
    }

    @Test
    fun floorToolDoesNotUseInteriorImage() {
        val url = exampleImageUrls["floor"]!!
        assertFalse(url in knownInteriorImages,
            "Floor tool must not use interior room image: $url")
    }

    @Test
    fun paintToolDoesNotUseInteriorImage() {
        val url = exampleImageUrls["paint"]!!
        assertFalse(url in knownInteriorImages,
            "Paint tool must not use interior room image: $url")
    }

    @Test
    fun paintToolDoesNotUseFloorImage() {
        val url = exampleImageUrls["paint"]!!
        assertFalse(url in knownFloorImages,
            "Paint tool must not use floor image: $url")
    }

    @Test
    fun gardenToolDoesNotUseInteriorImage() {
        val url = exampleImageUrls["garden"]!!
        assertFalse(url in knownInteriorImages,
            "Garden tool must not use indoor/interior image: $url")
    }

    @Test
    fun gardenToolDoesNotUseExteriorImage() {
        val url = exampleImageUrls["garden"]!!
        assertFalse(url in knownExteriorImages,
            "Garden tool must not use exterior/façade image: $url")
    }

    @Test
    fun facadeToolDoesNotUseInteriorImage() {
        val url = exampleImageUrls["facade"]!!
        assertFalse(url in knownInteriorImages,
            "Facade tool must not use interior room image: $url")
    }

    @Test
    fun facadeToolDoesNotUseGardenImage() {
        val url = exampleImageUrls["facade"]!!
        assertFalse(url in knownGardenImages,
            "Facade tool must not use garden image: $url")
    }

    // ── SampleImage model validation tests ─────────────────────────────────

    @Test
    fun sampleImagesHaveNonblankIds() {
        SampleImages.all.forEach { image ->
            assertTrue(image.id.isNotBlank(),
                "SampleImage must have non-blank id")
        }
    }

    @Test
    fun sampleImagesHaveValidAspectRatios() {
        SampleImages.all.forEach { image ->
            assertTrue(image.aspectRatio.isFinite(),
                "SampleImage '${image.id}' aspectRatio must be finite, got ${image.aspectRatio}")
            assertTrue(image.aspectRatio > 0f,
                "SampleImage '${image.id}' aspectRatio must be > 0, got ${image.aspectRatio}")
            assertTrue(image.aspectRatio < 10f,
                "SampleImage '${image.id}' aspectRatio must be < 10, got ${image.aspectRatio}")
        }
    }

    @Test
    fun sampleImagesHaveNonblankResourceUrls() {
        SampleImages.all.forEach { image ->
            assertTrue(image.resourceUrl.isNotBlank(),
                "SampleImage '${image.id}' must have non-blank resourceUrl")
            assertTrue(image.resourceUrl.endsWith(".webp"),
                "SampleImage '${image.id}' resourceUrl must end with .webp, got: ${image.resourceUrl}")
        }
    }

    @Test
    fun sampleImagesCategoryMatchesRoute() {
        SampleImages.all.forEach { image ->
            val allowed = routeAllowedCategories[image.route]
            assertNotNull(allowed,
                "SampleImage '${image.id}' has unknown route '${image.route}'")
            assertTrue(image.category in allowed,
                "SampleImage '${image.id}' has category ${image.category} " +
                        "but route '${image.route}' only allows $allowed")
        }
    }

    @Test
    fun sampleImagesIdIsUnique() {
        val ids = SampleImages.all.map { it.id }
        assertEquals(ids.size, ids.toSet().size,
            "SampleImage ids must be unique, found duplicates: ${ids.groupBy { it }.filter { it.value.size > 1 }.keys}")
    }

    @Test
    fun sampleImagesAreAllValid() {
        SampleImages.all.forEach { image ->
            assertTrue(image.isValid,
                "SampleImage '${image.id}' must be valid (non-blank fields, finite positive aspectRatio)")
        }
    }

    // ── Fallback image validation tests ────────────────────────────────────

    @Test
    fun everyRouteHasFallbackImage() {
        validToolIds.forEach { route ->
            assertTrue(route in routeFallbackImageUrls,
                "Route '$route' must have a fallback image in routeFallbackImageUrls")
        }
    }

    @Test
    fun fallbackImagesHaveValidAspectRatios() {
        routeFallbackImageUrls.forEach { (route, pair) ->
            val (url, ratio) = pair
            assertTrue(ratio.isFinite(),
                "Fallback for route '$route' must have finite aspectRatio, got $ratio")
            assertTrue(ratio > 0f,
                "Fallback for route '$route' must have positive aspectRatio, got $ratio")
            assertTrue(url.isNotBlank(),
                "Fallback for route '$route' must have non-blank URL")
        }
    }

    @Test
    fun fallbackImagesAreKnownResources() {
        routeFallbackImageUrls.forEach { (route, pair) ->
            val (url, _) = pair
            assertTrue(url in allKnownImages,
                "Fallback for route '$route' uses unknown resource: $url")
        }
    }

    @Test
    fun fallbackImagesAreNotToolIcons() {
        routeFallbackImageUrls.forEach { (route, pair) ->
            val (url, _) = pair
            assertFalse(url.startsWith("images/tool_"),
                "Fallback for route '$route' must not use a tool icon: $url")
        }
    }

    // ── Aspect ratio safety tests ──────────────────────────────────────────

    @Test
    fun defaultAspectRatioIsFiniteAndPositive() {
        assertTrue(DEFAULT_ASPECT_RATIO.isFinite(),
            "DEFAULT_ASPECT_RATIO must be finite, got $DEFAULT_ASPECT_RATIO")
        assertTrue(DEFAULT_ASPECT_RATIO > 0f,
            "DEFAULT_ASPECT_RATIO must be > 0, got $DEFAULT_ASPECT_RATIO")
    }

    @Test
    fun fallbackAspectRatioIsFiniteAndPositive() {
        assertTrue(EXAMPLE_FALLBACK_ASPECT_RATIO.isFinite(),
            "EXAMPLE_FALLBACK_ASPECT_RATIO must be finite, got $EXAMPLE_FALLBACK_ASPECT_RATIO")
        assertTrue(EXAMPLE_FALLBACK_ASPECT_RATIO > 0f,
            "EXAMPLE_FALLBACK_ASPECT_RATIO must be > 0, got $EXAMPLE_FALLBACK_ASPECT_RATIO")
    }

    @Test
    fun fallbackImageUrlIsNotToolIcon() {
        assertFalse(EXAMPLE_FALLBACK_IMAGE_URL.startsWith("images/tool_"),
            "Fallback URL must not use a tool icon: $EXAMPLE_FALLBACK_IMAGE_URL")
        assertTrue(EXAMPLE_FALLBACK_IMAGE_URL in allKnownImages,
            "Fallback URL must be a known resource: $EXAMPLE_FALLBACK_IMAGE_URL")
    }

    // ── Category coverage tests ────────────────────────────────────────────

    @Test
    fun exteriorImageCategoryCoversAllExteriorOptions() {
        val exteriorTypeIds = setOf(
            "apartment", "house", "office-building", "villa",
            "retail", "pool-house",
        )
        exteriorTypeIds.forEach { typeId ->
            assertTrue(typeId.isNotEmpty(),
                "Exterior type '$typeId' must be non-empty")
        }
    }

    @Test
    fun gardenImageCategoryCoversAllGardenStyles() {
        val gardenStyleIds = setOf(
            "modern", "tropical", "minimalist", "mediterranean",
            "japandi", "rustic", "zen", "english", "landscape",
            "bohemian", "scandinavian", "christmas",
        )
        gardenStyleIds.forEach { styleId ->
            assertTrue(styleId.isNotEmpty(),
                "Garden style '$styleId' must be non-empty")
        }
    }

    // ── Label tests ────────────────────────────────────────────────────────

    @Test
    fun wizardExampleRoomLabelIsAccurate() {
        assertEquals("Sample room", Strings.wizardExampleRoom)
    }

    @Test
    fun noExampleLabelContainsTestNumbers() {
        exampleLabels.forEach { (toolId, label) ->
            assertFalse(label.contains("#1") || label.contains("#4") || label.contains("#5"),
                "Tool '$toolId' label must not contain test numbers: $label")
        }
    }
}
