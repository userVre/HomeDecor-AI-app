package com.ismail.homedecorai

import com.ismail.homedecorai.model.BoardTab
import com.ismail.homedecorai.model.DiscoverScreenState
import com.ismail.homedecorai.model.DiscoverSectionItem
import com.ismail.homedecorai.model.GalleryCardItem
import com.ismail.homedecorai.model.ProfileScreenState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DiscoverBoardProfileTest {

    // ── Discover ──
    @Test
    fun testDiscoverClusterTabTagFormat() {
        assertEquals("discover_cluster_tab_rooms", Strings.formatTestTag(Strings.TestTags.discoverClusterTab, "rooms"))
    }

    @Test
    fun testDiscoverSeeAllTagFormat() {
        assertEquals("discover_see_all_all", Strings.formatTestTag(Strings.TestTags.discoverSeeAll, "all"))
    }

    @Test
    fun testDiscoverSectionRowTagFormat() {
        assertEquals("discover_section_row_living-room", Strings.formatTestTag(Strings.TestTags.discoverSectionRow, "living-room"))
    }

    @Test
    fun testDiscoverSectionCardTagFormat() {
        assertEquals("discover_section_card_item1", Strings.formatTestTag(Strings.TestTags.discoverSectionCard, "item1"))
    }

    @Test
    fun testDiscoverTestTagsExist() {
        assertNotNull(Strings.TestTags.discoverScreen)
        assertNotNull(Strings.TestTags.discoverClusterTabRow)
        assertNotNull(Strings.TestTags.discoverSectionScroll)
    }

    @Test
    fun testDiscoverA11yLabel() {
        val label = Strings.a11yDiscoverCard("Living Room", "Interior", "Modern")
        assertEquals("Living Room \u2014 Modern, Interior", label)
    }

    @Test
    fun testDiscoverA11yLabelWithoutStyle() {
        val label = Strings.a11yDiscoverCard("Living Room", "Interior")
        assertEquals("Living Room \u2014 Interior", label)
    }

    @Test
    fun testDiscoverClusterA11yLabel() {
        val label = Strings.a11yDiscoverCluster("Interior")
        assertEquals("Cluster: Interior", label)
    }

    @Test
    fun testDiscoverSeeAllA11yLabel() {
        val label = Strings.a11ySeeAll("Living Room")
        assertEquals("See all Living Room", label)
    }

    // ── Fixture Validation ──

    @Test
    fun testInteriorSectionsHaveCorrectCluster() {
        val interiorIds = setOf("kitchen", "living-room", "bedroom", "bathroom", "office", "dining")
        val state = DiscoverScreenState(sections = discoverTestSections())
        for (section in state.sections) {
            if (section.id in interiorIds) {
                assertEquals("interior", section.cluster, "Section ${section.id} must be in interior cluster")
            }
        }
    }

    @Test
    fun testArchitectureSectionsHaveCorrectCluster() {
        val archIds = setOf("modern-house", "classic-house", "apartment", "villa", "cabin")
        val state = DiscoverScreenState(sections = discoverTestSections())
        for (section in state.sections) {
            if (section.id in archIds) {
                assertEquals("architecture", section.cluster, "Section ${section.id} must be in architecture cluster")
            }
        }
    }

    @Test
    fun testLandscapeSectionsHaveCorrectCluster() {
        val landscapeIds = setOf("garden", "patio", "pool", "rooftop", "balcony")
        val state = DiscoverScreenState(sections = discoverTestSections())
        for (section in state.sections) {
            if (section.id in landscapeIds) {
                assertEquals("landscape", section.cluster, "Section ${section.id} must be in landscape cluster")
            }
        }
    }

    @Test
    fun testNoOfficeBuildingsInVillaOrHouse() {
        val state = DiscoverScreenState(sections = discoverTestSections())
        val villaOrHouseIds = setOf("villa", "modern-house", "classic-house")
        for (section in state.sections) {
            if (section.id in villaOrHouseIds) {
                for (item in section.items) {
                    assertFalse(
                        item.title.contains("Glass Office", ignoreCase = true) ||
                        item.id.contains("glassoffice", ignoreCase = true),
                        "Office building image must not appear in ${section.id}",
                    )
                }
            }
        }
    }

    @Test
    fun testNoApartmentBuildingsInHouseSections() {
        val state = DiscoverScreenState(sections = discoverTestSections())
        val houseIds = setOf("modern-house", "classic-house")
        for (section in state.sections) {
            if (section.id in houseIds) {
                for (item in section.items) {
                    assertFalse(
                        item.title.contains("Apartment", ignoreCase = true) ||
                        item.id.contains("apartmentblock", ignoreCase = true),
                        "Apartment building image must not appear in ${section.id}",
                    )
                }
            }
        }
    }

    @Test
    fun testPoolsOnlyInPoolSectionNotGarden() {
        val state = DiscoverScreenState(sections = discoverTestSections())
        for (section in state.sections) {
            if (section.id == "garden") {
                for (item in section.items) {
                    assertFalse(
                        item.id.contains("pool", ignoreCase = true) ||
                        item.id.contains("swimming", ignoreCase = true),
                        "Pool image must not appear in Garden section — pool images belong in Pool Area",
                    )
                }
            }
        }
    }

    @Test
    fun testEveryCardHasStyleType() {
        val state = DiscoverScreenState(sections = discoverTestSections())
        for (section in state.sections) {
            for (item in section.items) {
                assertTrue(
                    item.styleType.isNotBlank(),
                    "Card ${item.id} in ${section.id} must have non-blank styleType",
                )
            }
        }
    }

    @Test
    fun testEveryCardHasDescription() {
        val state = DiscoverScreenState(sections = discoverTestSections())
        for (section in state.sections) {
            for (item in section.items) {
                assertTrue(
                    item.description.isNotBlank(),
                    "Card ${item.id} in ${section.id} must have non-blank description",
                )
            }
        }
    }

    @Test
    fun testNoDuplicateImageIdsAcrossSections() {
        val state = DiscoverScreenState(sections = discoverTestSections())
        val allIds = mutableSetOf<String>()
        for (section in state.sections) {
            for (item in section.items) {
                assertTrue(
                    allIds.add(item.id),
                    "Duplicate item id '${item.id}' found across sections",
                )
            }
        }
    }

    @Test
    fun testGalleryCardItemDefaults() {
        val item = GalleryCardItem(id = "test", title = "Test", category = "interior")
        assertEquals("", item.styleType)
        assertEquals("", item.description)
        assertEquals("", item.imageUrl)
    }

    @Test
    fun testDiscoverScreenStateDefaults() {
        val state = DiscoverScreenState()
        assertEquals("interior", state.selectedCluster)
        assertTrue(state.sections.isEmpty())
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
        assertFalse(state.isSignedIn)
        assertEquals(null, state.noResultsMessage)
    }

    @Test
    fun testDiscoverNoResultsStrings() {
        assertEquals("No inspiration found", Strings.discoverNoResults)
        assertEquals("Try a different category", Strings.discoverNoResultsHint)
    }

    // ── Fixture Data ──

    private fun discoverTestSections() = listOf(
        DiscoverSectionItem("kitchen", "Kitchen", "interior", listOf(
            GalleryCardItem("kitchen-1", "Kitchen", "interior", "Modern", "desc"),
            GalleryCardItem("kitchen-2", "Kitchen", "interior", "Minimalist", "desc"),
            GalleryCardItem("kitchen-3", "Kitchen", "interior", "Contemporary", "desc"),
        )),
        DiscoverSectionItem("living-room", "Living Room", "interior", listOf(
            GalleryCardItem("living-1", "Living Room", "interior", "Modern", "desc"),
            GalleryCardItem("living-2", "Living Room", "interior", "Scandinavian", "desc"),
        )),
        DiscoverSectionItem("bedroom", "Bedroom", "interior", listOf(
            GalleryCardItem("bedroom-1", "Bedroom", "interior", "Modern", "desc"),
            GalleryCardItem("bedroom-2", "Bedroom", "interior", "Minimalist", "desc"),
        )),
        DiscoverSectionItem("bathroom", "Bathroom", "interior", listOf(
            GalleryCardItem("bathroom-1", "Bathroom", "interior", "Modern", "desc"),
        )),
        DiscoverSectionItem("office", "Office", "interior", listOf(
            GalleryCardItem("office-1", "Office", "interior", "Modern", "desc"),
        )),
        DiscoverSectionItem("dining", "Dining Room", "interior", listOf(
            GalleryCardItem("dining-1", "Dining", "interior", "Modern", "desc"),
        )),
        DiscoverSectionItem("modern-house", "Modern House", "architecture", listOf(
            GalleryCardItem("modern-house-1", "Modern House", "architecture", "Contemporary", "desc"),
        )),
        DiscoverSectionItem("classic-house", "Classic House", "architecture", listOf(
            GalleryCardItem("classic-house-1", "Stone Manor", "architecture", "Stone Manor", "desc"),
        )),
        DiscoverSectionItem("apartment", "Apartment", "architecture", listOf(
            GalleryCardItem("apartment-1", "Apartment", "architecture", "Modern Block", "desc"),
            GalleryCardItem("apartment-2", "Apartment", "architecture", "Contemporary", "desc"),
        )),
        DiscoverSectionItem("villa", "Villa", "architecture", listOf(
            GalleryCardItem("villa-1", "Modern Villa", "architecture", "Modern", "desc"),
        )),
        DiscoverSectionItem("cabin", "Cabin", "architecture", listOf(
            GalleryCardItem("cabin-1", "Rustic Cabin", "architecture", "Rustic", "desc"),
        )),
        DiscoverSectionItem("garden", "Garden", "landscape", listOf(
            GalleryCardItem("garden-1", "Garden", "landscape", "Cozy", "desc"),
            GalleryCardItem("garden-2", "Garden", "landscape", "Backyard", "desc"),
        )),
        DiscoverSectionItem("patio", "Patio", "landscape", listOf(
            GalleryCardItem("patio-1", "Patio", "landscape", "Elegant", "desc"),
        )),
        DiscoverSectionItem("pool", "Pool Area", "landscape", listOf(
            GalleryCardItem("pool-1", "Pool", "landscape", "Luxury", "desc"),
            GalleryCardItem("pool-2", "Pool", "landscape", "Courtyard", "desc"),
        )),
        DiscoverSectionItem("rooftop", "Rooftop", "landscape", listOf(
            GalleryCardItem("rooftop-1", "Rooftop", "landscape", "Modern", "desc"),
        )),
        DiscoverSectionItem("balcony", "Balcony", "landscape", listOf(
            GalleryCardItem("balcony-1", "Balcony", "landscape", "Cozy", "desc"),
        )),
    )

    // ── Board ──
    @Test
    fun testBoardTestTagsExist() {
        assertNotNull(Strings.TestTags.boardScreen)
        assertNotNull(Strings.TestTags.boardGuestHero)
        assertNotNull(Strings.TestTags.boardTabRow)
        assertNotNull(Strings.TestTags.boardTab)
        assertNotNull(Strings.TestTags.boardSignInButton)
    }

    @Test
    fun testBoardTabTagFormat() {
        assertEquals("board_tab_generated", Strings.formatTestTag(Strings.TestTags.boardTab, "generated"))
    }

    @Test
    fun testBoardGeneratedCardTagFormat() {
        assertEquals("board_generated_card_item1", Strings.formatTestTag(Strings.TestTags.boardGeneratedCard, "item1"))
    }

    @Test
    fun testBoardFavoriteCardTagFormat() {
        assertEquals("board_favorite_card_item1", Strings.formatTestTag(Strings.TestTags.boardFavoriteCard, "item1"))
    }

    @Test
    fun testBoardProjectCardTagFormat() {
        assertEquals("board_project_card_item1", Strings.formatTestTag(Strings.TestTags.boardProjectCard, "item1"))
    }

    @Test
    fun testBoardLockedCardTagFormat() {
        assertEquals("board_locked_card_living-room", Strings.formatTestTag(Strings.TestTags.boardLockedCard, "living-room"))
    }

    @Test
    fun testBoardEmptyTitles() {
        assertEquals("No designs yet", Strings.boardEmptyGenerated)
        assertEquals("No favorites yet", Strings.boardEmptyFavorites)
        assertEquals("No projects yet", Strings.boardEmptyProjects)
    }

    @Test
    fun testBoardEmptyDescriptions() {
        assertEquals("Pick a tool and upload a photo to generate your first design.", Strings.boardEmptyGeneratedBody)
        assertEquals("Star a design to save it here for quick access.", Strings.boardEmptyFavoritesBody)
        assertEquals("Create a project to group designs by room and compare options.", Strings.boardEmptyProjectsBody)
    }

    @Test
    fun testBoardA11yLabel() {
        val label = Strings.a11yBoardDesign("Living Room", "Modern", "Generated")
        assertEquals("Living Room \u2014 Modern \u2014 Generated", label)
    }

    @Test
    fun testBoardTabLabels() {
        assertEquals("Generated", Strings.generatedTab)
        assertEquals("Favorites", Strings.favoritesTab)
        assertEquals("Projects", Strings.projectsTab)
    }

    @Test
    fun testBoardSignInCta() {
        assertEquals("Sign in to save this design.", Strings.boardSignInCta)
    }

    @Test
    fun testBoardTabEntries() {
        assertEquals(3, BoardTab.entries.size)
        assertEquals(BoardTab.Generated, BoardTab.entries[0])
        assertEquals(BoardTab.Favorites, BoardTab.entries[1])
        assertEquals(BoardTab.Projects, BoardTab.entries[2])
    }

    // ── Profile ──
    @Test
    fun testProfileTestTagsExist() {
        assertNotNull(Strings.TestTags.profileScreen)
        assertNotNull(Strings.TestTags.profileHeading)
        assertNotNull(Strings.TestTags.profileStatusCard)
        assertNotNull(Strings.TestTags.profileSettingsButton)
        assertNotNull(Strings.TestTags.profileSignInButton)
    }

    @Test
    fun testProfileStatusCardTagFormat() {
        assertEquals("profile_status_card_Diamonds", Strings.formatTestTag(Strings.TestTags.profileStatusCard, "Diamonds"))
    }

    @Test
    fun testProfileHeading() {
        assertEquals("Profile", Strings.myProfileTitle)
    }

    @Test
    fun testProfileSubtitle() {
        assertEquals("Settings, support, and account info", Strings.profileSubtitle)
    }

    @Test
    fun testProfileSignInStrings() {
        assertEquals("Create an account to save your profile preferences and access them from any device.", Strings.profileSignInBody)
        assertEquals("Sign in to get started", Strings.profileSignInRegister)
    }

    @Test
    fun testProfileSettingsStrings() {
        assertEquals("Settings", Strings.settings)
    }

    @Test
    fun testProfileA11yLabels() {
        assertEquals("Profile", Strings.a11yProfileHeading)
        assertNotNull(Strings.a11yDiamondBadge)
    }

    @Test
    fun testProfileScreenStateDefaults() {
        val state = ProfileScreenState()
        assertEquals(true, state.isGuest)
    }

    @Test
    fun testProfileStatusLabels() {
        assertEquals("Diamonds", Strings.profileStatusDiamonds)
        assertEquals("Plan", Strings.profileStatusPlan)
        assertEquals("Saved", Strings.profileStatusSaved)
    }
}
