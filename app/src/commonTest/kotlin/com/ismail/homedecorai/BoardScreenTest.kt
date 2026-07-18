package com.ismail.homedecorai

import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.model.BoardScreenState
import com.ismail.homedecorai.model.BoardTab
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BoardScreenTest {

    // ── BoardTab enum ────────────────────────────────────────────────────

    @Test
    fun boardTab_hasThreeEntries() {
        assertEquals(3, BoardTab.entries.size)
    }

    @Test
    fun boardTab_firstIsGenerated() {
        assertEquals(BoardTab.Generated, BoardTab.entries[0])
    }

    @Test
    fun boardTab_secondIsFavorites() {
        assertEquals(BoardTab.Favorites, BoardTab.entries[1])
    }

    @Test
    fun boardTab_thirdIsProjects() {
        assertEquals(BoardTab.Projects, BoardTab.entries[2])
    }

    // ── BoardScreenState ─────────────────────────────────────────────────

    @Test
    fun boardScreenState_defaultGeneratedItemsEmpty() {
        val state = BoardScreenState()
        assertTrue(state.generatedItems.isEmpty())
    }

    @Test
    fun boardScreenState_defaultFavoriteItemsEmpty() {
        val state = BoardScreenState()
        assertTrue(state.favoriteItems.isEmpty())
    }

    @Test
    fun boardScreenState_defaultProjectItemsEmpty() {
        val state = BoardScreenState()
        assertTrue(state.projectItems.isEmpty())
    }

    @Test
    fun boardScreenState_defaultLocalGuestDesignsEmpty() {
        val state = BoardScreenState()
        assertTrue(state.localGuestDesigns.isEmpty())
    }

    @Test
    fun boardScreenState_defaultIsLoadingFalse() {
        val state = BoardScreenState()
        assertFalse(state.isLoading)
    }

    @Test
    fun boardScreenState_defaultErrorNull() {
        val state = BoardScreenState()
        assertEquals(null, state.error)
    }

    // ── BoardItem ────────────────────────────────────────────────────────

    @Test
    fun boardItem_requiredFields() {
        val item = BoardItem(id = "test-1")
        assertEquals("test-1", item.id)
    }

    @Test
    fun boardItem_defaultToolTitle() {
        val item = BoardItem(id = "test-1")
        assertEquals("", item.toolTitle)
    }

    @Test
    fun boardItem_defaultStyle() {
        val item = BoardItem(id = "test-1")
        assertEquals("", item.style)
    }

    @Test
    fun boardItem_defaultRoomType() {
        val item = BoardItem(id = "test-1")
        assertEquals("", item.roomType)
    }

    @Test
    fun boardItem_defaultImageUrlNull() {
        val item = BoardItem(id = "test-1")
        assertEquals(null, item.imageUrl)
    }

    @Test
    fun boardItem_defaultImageUriNull() {
        val item = BoardItem(id = "test-1")
        assertEquals(null, item.imageUri)
    }

    @Test
    fun boardItem_defaultStatusCompleted() {
        val item = BoardItem(id = "test-1")
        assertEquals("completed", item.status)
    }

    @Test
    fun boardItem_defaultErrorMessageNull() {
        val item = BoardItem(id = "test-1")
        assertEquals(null, item.errorMessage)
    }

    @Test
    fun boardItem_defaultIsFavoriteFalse() {
        val item = BoardItem(id = "test-1")
        assertFalse(item.isFavorite)
    }

    @Test
    fun boardItem_withAllFields() {
        val item = BoardItem(
            id = "test-1",
            toolTitle = "Living Room",
            style = "Modern",
            roomType = "interior",
            imageUrl = "https://example.com/img.jpg",
            imageUri = "file:///local/img.jpg",
            sourceImageUri = "file:///local/source.jpg",
            sourceImageUrl = "https://example.com/source.jpg",
            status = "generating",
            errorMessage = "Something went wrong",
            prompt = "Modern living room design",
            budgetLabel = "10 diamonds",
            createdAt = 1234.0,
            isFavorite = true,
        )
        assertEquals("test-1", item.id)
        assertEquals("Living Room", item.toolTitle)
        assertEquals("Modern", item.style)
        assertEquals("interior", item.roomType)
        assertEquals("https://example.com/img.jpg", item.imageUrl)
        assertEquals("file:///local/img.jpg", item.imageUri)
        assertEquals("file:///local/source.jpg", item.sourceImageUri)
        assertEquals("https://example.com/source.jpg", item.sourceImageUrl)
        assertEquals("generating", item.status)
        assertEquals("Something went wrong", item.errorMessage)
        assertEquals("Modern living room design", item.prompt)
        assertEquals("10 diamonds", item.budgetLabel)
        assertEquals(1234.0, item.createdAt)
        assertTrue(item.isFavorite)
    }

    // ── Screen state with items ──────────────────────────────────────────

    @Test
    fun boardScreenState_withGeneratedItems() {
        val items = listOf(
            BoardItem(id = "g1", toolTitle = "Living Room", style = "Modern"),
            BoardItem(id = "g2", toolTitle = "Kitchen", style = "Minimalist"),
        )
        val state = BoardScreenState(generatedItems = items)
        assertEquals(2, state.generatedItems.size)
        assertEquals("Living Room", state.generatedItems[0].toolTitle)
    }

    @Test
    fun boardScreenState_withFavoriteItems() {
        val items = listOf(
            BoardItem(id = "f1", toolTitle = "Bedroom", style = "Scandinavian"),
        )
        val state = BoardScreenState(favoriteItems = items)
        assertEquals(1, state.favoriteItems.size)
        assertEquals("Bedroom", state.favoriteItems[0].toolTitle)
    }

    @Test
    fun boardScreenState_withProjectItems() {
        val items = listOf(
            BoardItem(id = "p1", toolTitle = "Project A"),
            BoardItem(id = "p2", toolTitle = "Project B"),
            BoardItem(id = "p3", toolTitle = "Project C"),
        )
        val state = BoardScreenState(projectItems = items)
        assertEquals(3, state.projectItems.size)
    }

    @Test
    fun boardScreenState_withMixedItems() {
        val state = BoardScreenState(
            generatedItems = listOf(BoardItem(id = "g1")),
            favoriteItems = listOf(BoardItem(id = "f1")),
            projectItems = listOf(BoardItem(id = "p1")),
        )
        assertEquals(1, state.generatedItems.size)
        assertEquals(1, state.favoriteItems.size)
        assertEquals(1, state.projectItems.size)
    }

    @Test
    fun boardScreenState_withLocalGuestDesigns() {
        val local = listOf(
            BoardItem(id = "local-1", toolTitle = "Local Design"),
        )
        val state = BoardScreenState(localGuestDesigns = local)
        assertEquals(1, state.localGuestDesigns.size)
        assertEquals("Local Design", state.localGuestDesigns[0].toolTitle)
    }

    // ── Empty state strings ──────────────────────────────────────────────

    @Test
    fun boardEmptyStrings_exist() {
        assertNotNull(Strings.boardEmptyGenerated)
        assertNotNull(Strings.boardEmptyGeneratedBody)
        assertNotNull(Strings.boardEmptyFavorites)
        assertNotNull(Strings.boardEmptyFavoritesBody)
        assertNotNull(Strings.boardEmptyProjects)
        assertNotNull(Strings.boardEmptyProjectsBody)
    }

    @Test
    fun boardEmptyGenerated_correctValue() {
        assertEquals("No designs yet", Strings.boardEmptyGenerated)
    }

    @Test
    fun boardEmptyGeneratedBody_correctValue() {
        assertEquals("Pick a tool and upload a photo to generate your first design.", Strings.boardEmptyGeneratedBody)
    }

    @Test
    fun boardEmptyFavorites_correctValue() {
        assertEquals("No favorites yet", Strings.boardEmptyFavorites)
    }

    @Test
    fun boardEmptyFavoritesBody_correctValue() {
        assertEquals("Star a design to save it here for quick access.", Strings.boardEmptyFavoritesBody)
    }

    @Test
    fun boardEmptyProjects_correctValue() {
        assertEquals("No projects yet", Strings.boardEmptyProjects)
    }

    @Test
    fun boardEmptyProjectsBody_correctValue() {
        assertEquals("Create a project to group designs by room and compare options.", Strings.boardEmptyProjectsBody)
    }

    @Test
    fun boardGuestEmptyFavorites_correctValue() {
        assertEquals("Favorite designs appear here.", Strings.boardEmptyGuestFavorites)
    }

    @Test
    fun boardGuestEmptyProjects_correctValue() {
        assertEquals("Create your first project.", Strings.boardEmptyGuestProjects)
    }

    // ── Board action strings ─────────────────────────────────────────────

    @Test
    fun boardActionStrings_exist() {
        assertNotNull(Strings.boardSave)
        assertNotNull(Strings.boardRename)
        assertNotNull(Strings.boardDelete)
        assertNotNull(Strings.boardRenameTitle)
        assertNotNull(Strings.boardRenameLabel)
        assertNotNull(Strings.boardDeleteConfirm)
        assertNotNull(Strings.boardDeleteConfirmBody)
        assertNotNull(Strings.boardCancel)
        assertNotNull(Strings.boardConfirm)
    }

    @Test
    fun boardSave_correctValue() {
        assertEquals("Save", Strings.boardSave)
    }

    @Test
    fun boardRename_correctValue() {
        assertEquals("Rename", Strings.boardRename)
    }

    @Test
    fun boardDelete_correctValue() {
        assertEquals("Delete", Strings.boardDelete)
    }

    @Test
    fun boardDeleteConfirm_correctValue() {
        assertEquals("Delete this design?", Strings.boardDeleteConfirm)
    }

    @Test
    fun boardDeleteConfirmBody_correctValue() {
        assertEquals("This action cannot be undone.", Strings.boardDeleteConfirmBody)
    }

    // ── Guest banner strings ─────────────────────────────────────────────

    @Test
    fun boardGuestLocalNote_correctValue() {
        assertEquals("Designs you create are saved locally on this device.", Strings.boardGuestLocalNote)
    }

    @Test
    fun boardGuestSyncNote_correctValue() {
        assertEquals("Sign in to save and sync your designs across devices.", Strings.boardGuestSyncNote)
    }

    @Test
    fun boardGuestLocalTitle_correctValue() {
        assertEquals("Saved to this device", Strings.boardGuestLocalTitle)
    }

    @Test
    fun boardGuestSyncTitle_correctValue() {
        assertEquals("Saved to your account", Strings.boardGuestSyncTitle)
    }

    // ── Test tags ────────────────────────────────────────────────────────

    @Test
    fun boardTestTags_exist() {
        assertNotNull(Strings.TestTags.boardScreen)
        assertNotNull(Strings.TestTags.boardTabRow)
        assertNotNull(Strings.TestTags.boardTab)
        assertNotNull(Strings.TestTags.boardGuestHero)
        assertNotNull(Strings.TestTags.boardSignInButton)
        assertNotNull(Strings.TestTags.boardGeneratedCard)
        assertNotNull(Strings.TestTags.boardFavoriteCard)
        assertNotNull(Strings.TestTags.boardProjectCard)
        assertNotNull(Strings.TestTags.boardLockedCard)
        assertNotNull(Strings.TestTags.boardLocalBanner)
        assertNotNull(Strings.TestTags.boardCardMenu)
        assertNotNull(Strings.TestTags.boardCardMenuItem)
        assertNotNull(Strings.TestTags.boardEmptyState)
        assertNotNull(Strings.TestTags.boardRenameDialog)
        assertNotNull(Strings.TestTags.boardRenameInput)
        assertNotNull(Strings.TestTags.boardRenameConfirm)
        assertNotNull(Strings.TestTags.boardDeleteDialog)
        assertNotNull(Strings.TestTags.boardDeleteConfirm)
        assertNotNull(Strings.TestTags.boardGrid)
    }

    @Test
    fun boardTestTags_format() {
        assertEquals("board_screen", Strings.TestTags.boardScreen)
        assertEquals("board_tab_row", Strings.TestTags.boardTabRow)
        assertEquals("board_guest_hero", Strings.TestTags.boardGuestHero)
        assertEquals("board_sign_in_button", Strings.TestTags.boardSignInButton)
        assertEquals("board_local_banner", Strings.TestTags.boardLocalBanner)
        assertEquals("board_card_menu", Strings.TestTags.boardCardMenu)
        assertEquals("board_rename_dialog", Strings.TestTags.boardRenameDialog)
        assertEquals("board_rename_input", Strings.TestTags.boardRenameInput)
        assertEquals("board_rename_confirm", Strings.TestTags.boardRenameConfirm)
        assertEquals("board_delete_dialog", Strings.TestTags.boardDeleteDialog)
        assertEquals("board_delete_confirm", Strings.TestTags.boardDeleteConfirm)
    }

    @Test
    fun boardTestTags_formatTag_tab() {
        assertEquals("board_tab_generated", Strings.formatTestTag(Strings.TestTags.boardTab, "generated"))
    }

    @Test
    fun boardTestTags_formatTag_generatedCard() {
        assertEquals("board_generated_card_item1", Strings.formatTestTag(Strings.TestTags.boardGeneratedCard, "item1"))
    }

    @Test
    fun boardTestTags_formatTag_favoriteCard() {
        assertEquals("board_favorite_card_item1", Strings.formatTestTag(Strings.TestTags.boardFavoriteCard, "item1"))
    }

    @Test
    fun boardTestTags_formatTag_projectCard() {
        assertEquals("board_project_card_item1", Strings.formatTestTag(Strings.TestTags.boardProjectCard, "item1"))
    }

    @Test
    fun boardTestTags_formatTag_lockedCard() {
        assertEquals("board_locked_card_living-room", Strings.formatTestTag(Strings.TestTags.boardLockedCard, "living-room"))
    }

    @Test
    fun boardTestTags_formatTag_cardMenuItem() {
        assertEquals("board_card_menu_item_Open", Strings.formatTestTag(Strings.TestTags.boardCardMenuItem, "Open"))
    }

    @Test
    fun boardTestTags_formatTag_emptyState() {
        assertEquals("board_empty_state_favorites", Strings.formatTestTag(Strings.TestTags.boardEmptyState, "favorites"))
    }

    @Test
    fun boardTestTags_formatTag_grid() {
        assertEquals("board_grid_generated", Strings.formatTestTag(Strings.TestTags.boardGrid, "generated"))
    }

    // ── Accessibility labels ─────────────────────────────────────────────

    @Test
    fun a11yBoardDesign_label() {
        val label = Strings.a11yBoardDesign("Living Room", "Modern", "Generated")
        assertEquals("Living Room \u2014 Modern \u2014 Generated", label)
    }

    @Test
    fun a11yBoardDesign_labelWithoutStyle() {
        val label = Strings.a11yBoardDesign("Living Room", "", "Generated")
        assertEquals("Living Room \u2014 Generated", label)
    }

    // ── Tab labels ───────────────────────────────────────────────────────

    @Test
    fun tabLabel_generated() {
        assertEquals("Generated", Strings.generatedTab)
    }

    @Test
    fun tabLabel_favorites() {
        assertEquals("Favorites", Strings.favoritesTab)
    }

    @Test
    fun tabLabel_projects() {
        assertEquals("Projects", Strings.projectsTab)
    }

    // ── Sign-in CTA ──────────────────────────────────────────────────────

    @Test
    fun boardSignInCta_correctValue() {
        assertEquals("Sign in to save this design.", Strings.boardSignInCta)
    }

    @Test
    fun boardSignInSaveSync_correctValue() {
        assertEquals("Sign in to save and sync your designs", Strings.boardSignInSaveSync)
    }

    @Test
    fun boardSignInToUnlock_correctValue() {
        assertEquals("Sign in to unlock", Strings.boardSignInToUnlock)
    }

    @Test
    fun boardLocalDesigns_correctValue() {
        assertEquals("Local Designs", Strings.boardLocalDesigns)
    }
}
