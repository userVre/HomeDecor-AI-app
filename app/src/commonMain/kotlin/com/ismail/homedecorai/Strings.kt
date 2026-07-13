package com.ismail.homedecorai

object Strings {
    // ── Feature Flags ──────────────────────────────────────────────────────
    /** When false, payment CTAs route to billing support instead of live checkout. */
    const val PAYMENTS_ENABLED = true

    // ── Checkout URLs (Whop) ──────────────────────────────────────────────
    const val CHECKOUT_URL_YEARLY = "https://whop.com/checkout/plan_FCAl4pKNoaC3X/"
    const val CHECKOUT_URL_MONTHLY = "https://whop.com/checkout/plan_x7j37eWwsXTK1/"
    const val CHECKOUT_URL_FAMILY = "https://whop.com/checkout/plan_cBvc9Qkwr476O/"
    const val CUSTOMER_PORTAL_URL = "https://whop.com/account"
    const val BILLING_SUPPORT_URL = "mailto:support@homedecorai.com?subject=Billing%20Help"

    fun checkoutUrlForPlan(planId: String): String = when (planId) {
        "monthly" -> CHECKOUT_URL_MONTHLY
        "family" -> CHECKOUT_URL_FAMILY
        else -> CHECKOUT_URL_YEARLY
    }

    // Navigation
    const val navTools = "Tools"
    const val navDiscover = "Discover"
    const val navUpgrade = "Upgrade"
    const val navProfile = "Profile"
    const val navMyBoard = "My Board"

    // Tools Screen
    const val tryThis = "Try this"
    const val toolsSubtitle = "Pick a tool, upload a photo, and see your space transformed"
    const val a11yOpenDiamondStore = "Open diamond store"
    fun a11yToolCard(title: String, description: String) = "$title: $description"

    // Before / After Preview
    const val previewBeforeLabel = "Before"
    const val previewAfterLabel = "After"
    const val previewDragHint = "Drag to compare"
    const val previewResultTitle = "See what you'll get"
    const val previewResultSubtitle = "Here's an example of what this tool can create"
    const val previewExampleDisclaimer = "Example result \u2014 your design will be unique"

    // Discover Screen
    const val discoverStylesTitle = "Discover"
    const val back = "Back"
    fun ideasCount(count: Int) = "$count ideas"
    const val seeAll = "See all"
    const val createWithStyle = "Create with this style"
    const val favorited = "Favorited"
    const val favorite = "Favorite"
    const val addToMoodboard = "Save to board"
    const val toastFavoriteAdded = "Added to favorites"
    const val toastFavoriteRemoved = "Removed from favorites"
    const val toastMoodboardAdded = "Saved to your board"
    const val toastLinkCopied = "Link copied to clipboard"
    const val toastShareFailed = "Sharing isn't supported here"
    const val toastRestoreNotAvailable = "Manage your subscription in the billing portal"
    const val toastFeedbackSent = "Thanks for your feedback!"
    const val toastContactEmail = "Opening email client..."
    fun a11yDiscoverCluster(clusterLabel: String) = "Cluster: $clusterLabel"
    fun a11ySeeAll(sectionTitle: String) = "See all $sectionTitle"
    fun a11yGalleryCard(itemTitle: String, itemCategory: String) = "$itemTitle, $itemCategory"

    // Profile Screen
    const val myProfileTitle = "Profile"
    const val profileSubtitle = "Settings, support, and account info"
    const val settings = "Settings"
    const val accountSection = "Account"
    const val editProfile = "Edit profile"
    const val editProfileBody = "Update your name and photo"
    const val privacySecurity = "Privacy & security"
    const val privacySecurityBody = "Data, privacy, and security settings"
    const val myDiamonds = "My Diamonds"
    fun myDiamondsBody(diamonds: Int) = "$diamonds diamonds available"
    const val subscriptionStatus = "Subscription"
    const val currentPlan = "Current Plan"
    const val currentPlanBodyPro = "You have unlimited generations"
    const val currentPlanBodyFree = "Upgrade for unlimited generations"
    const val profileSignInBody = "Create an account to save your profile preferences and access them from any device."
    const val profileSignInRegister = "Sign in to get started"
    const val profileGuestBenefit1Title = "Save designs"
    const val profileGuestBenefit1Body = "Every design is saved to your board automatically."
    const val profileGuestBenefit2Title = "Sync everywhere"
    const val profileGuestBenefit2Body = "Your designs follow you across all your devices."
    const val profileGuestBenefit3Title = "Design history"
    const val profileGuestBenefit3Body = "Revisit any design you've created."
    const val profileGuestBenefit4Title = "Diamonds"
    const val profileGuestBenefit4Body = "Use diamonds to generate new designs. Earn daily bonuses."
    const val profileStatusDiamonds = "Diamonds"
    const val profileStatusPlan = "Plan"
    const val profileStatusSaved = "Saved"
    const val freePlan = "Free"
    const val initialsFallback = "??"
    const val guestExplorer = "Guest Explorer"
    const val freeBadge = "Free"
    const val accountConnected = "Account connected"
    const val profileProMember = "Pro Member"
    const val profileSavedDesigns = "Saved Designs"
    const val profileNoDesignsYet = "No saved designs yet"
    const val profileViewAllDesigns = "View all saved designs"

    // Settings Screen
    const val language = "Language"
    const val sendFeedback = "Send Feedback"
    const val contactSupport = "Contact support"
    const val deleteInformation = "Delete information"
    fun versionLabel(version: String) = "Version $version"
    const val toastLanguageSelected = "Language updated"
    const val toastComingSoon = "Coming soon"
    const val toastRateUs = "Rate us on the Play Store!"
    const val languageSystemDefault = "System default"
    fun a11yLanguageSelected(label: String) = "$label (selected)"
    const val a11yCheckIcon = "Selected"

    // Dialogs
    const val feedbackDialogTitle = "Contact Support"
    const val feedbackHint = "Describe your issue or suggestion..."
    const val feedbackSending = "Sending..."
    const val feedbackSend = "Send"
    const val cancel = "Cancel"
    const val deleteAccountTitle = "Delete Account"
    const val deleteAccountBody = "Are you sure you want to delete your account? This action cannot be undone."
    const val deletingAccount = "Deleting..."
    const val deleteAccountConfirm = "Delete"
    const val retry = "Retry"
    const val dismiss = "Dismiss"

    // Discover clusters
    fun discoverCluster(cluster: String): String = when (cluster) {
        "interior" -> "Interior"
        "architecture" -> "Architecture"
        "landscape" -> "Landscape"
        else -> cluster.replaceFirstChar { it.uppercase() }
    }

    // Discover sections
    fun discoverSectionTitle(sectionId: String): String = when (sectionId) {
        "living-room" -> "Living Room"
        "bedroom" -> "Bedroom"
        "kitchen" -> "Kitchen"
        "bathroom" -> "Bathroom"
        "office" -> "Office"
        "dining" -> "Dining Room"
        "modern-house" -> "Modern House"
        "classic-house" -> "Classic House"
        "apartment" -> "Apartment"
        "villa" -> "Villa"
        "cabin" -> "Cabin"
        "garden" -> "Garden"
        "patio" -> "Patio"
        "pool" -> "Pool Area"
        "rooftop" -> "Rooftop"
        "balcony" -> "Balcony"
        else -> sectionId.replaceFirstChar { it.uppercase() }
    }

    // Accessibility labels for gallery cards
    fun a11yInspirationImage(sectionTitle: String): String = "$sectionTitle inspiration image"

    // My Board
    const val myBoardTitle = "My Board"
    const val boardSignInCta = "Sign in to save this design."
    const val boardSignInToUnlock = "Sign in to unlock"
    const val boardEmptyGenerated = "No designs yet"
    const val boardEmptyGeneratedBody = "Pick a tool and upload a photo to generate your first design."
    const val boardEmptyFavorites = "No favorites yet"
    const val boardEmptyFavoritesBody = "Star a design to save it here for quick access."
    const val boardEmptyProjects = "No projects yet"
    const val boardEmptyProjectsBody = "Create a project to group designs by room and compare options."
    const val startADesign = "Start a design"
    const val exploreDiscover = "Explore Discover"
    const val generatedTab = "Generated"
    const val favoritesTab = "Favorites"
    const val projectsTab = "Projects"
    const val generatedImages = "Generated Images"
    const val aiGeneratedBadge = "Generated with HomeDecor AI"
    const val favoritesSection = "Favorites"
    const val savedProjects = "Projects"
    const val boardGuestHeadline = "Sign in to get started"
    const val boardGuestSubtitle = "Sign in to save every design you create, organize them into projects, and access them on any device."
    const val boardGuestSampleSection = "Sign in to save designs like these"
    const val boardHeroGuestTitle = "Sign in to unlock premium styles"
    const val boardGuestBenefitSaved = "Your designs"
    const val boardGuestBenefitSavedBody = "Every design you create is saved to your board."
    const val boardGuestBenefitFavorites = "Favorites"
    const val boardGuestBenefitFavoritesBody = "Tap the star on any design to find it here fast."
    const val boardGuestBenefitProjects = "Projects"
    const val boardGuestBenefitProjectsBody = "Group designs by room to compare and refine."
    const val boardGuestBenefitCrossDevice = "Sync across devices"
    const val boardGuestBenefitCrossDeviceBody = "Your board syncs automatically across phone, tablet, and desktop."
    const val boardGuestCta = "Sign in to start saving"
    const val boardSampleLivingRoom = "Modern Living Room"
    const val boardSampleBedroom = "Cozy Bedroom"
    const val boardSampleKitchen = "Minimalist Kitchen"
    const val boardSampleBathroom = "Luxury Bathroom"
    const val boardSampleOffice = "Home Office"

    // Settings sections
    const val settingsSectionAccount = "Account"
    const val settingsSectionApp = "App"
    const val settingsSectionPurchases = "Purchases"
    const val settingsSectionSupport = "Support"
    const val settingsSectionLegal = "Legal"
    const val settingsSectionAccountActions = "Account Actions"
    const val settingsAccountActionsBody = "Log out or delete your account"
    const val myDiamondsBodySettings = "Diamonds available"
    const val diamondStore = "Diamond Store"
    const val diamondStoreBody = "Get more diamonds to generate designs"
    const val diamondStoreTitle = "Diamond Store"
    const val diamondStoreSubtitle = "Buy diamonds to generate more designs"
    const val diamondStoreLoading = "Loading packages..."
    const val diamondStoreError = "Failed to load packages"
    const val diamondStorePurchaseSuccess = "Purchase successful!"
    const val diamondStorePurchaseError = "Purchase failed. Please try again."
    const val diamondStorePurchaseLoading = "Processing..."
    const val diamondStoreFreeLabel = "Free"
    const val diamondStoreDailyBonus = "Daily Bonus"
    const val diamondStoreDailyBonusBody = "Come back daily for free diamonds"
    const val diamondStoreClaim = "Claim"
    const val restorePurchases = "Restore Purchases"
    const val restorePurchasesBody = "Restore your previous purchases"
    const val rateUsBody = "Give us feedback"
    const val feedbackBody = "Send us your feedback"
    const val termsBody = "Terms of Service"
    const val privacyBody = "Privacy Policy"
    const val logOut = "Log Out"
    const val logOutBody = "Sign out of your account"
    const val close = "Close"
    const val settingsAccountDescription = "Name, photo, and preferences"
    const val settingsAppDescription = "Theme, language, and display"
    const val settingsPurchasesDescription = "Subscriptions and billing"
    const val settingsSupportDescription = "FAQs, contact, and feedback"
    const val settingsLegalDescription = "Terms and privacy"
    const val settingsAppearance = "Appearance"
    const val settingsAppearanceBody = "Dark mode and display"
    const val manageBilling = "Manage Billing"
    const val manageBillingBody = "View subscriptions and payment methods"
    const val restorePurchasesWeb = "Manage Subscription"
    const val restorePurchasesWebBody = "View your subscription in the billing portal"

    // Profile sections
    const val preferencesSection = "Preferences"
    const val supportSection = "Support"
    const val legalSection = "Legal"
    const val appInfoSection = "App Info"
    const val helpCenter = "Help Center"
    const val helpCenterBody = "Browse FAQs and guides"
    const val contactUs = "Contact Us"
    const val contactUsBody = "Get in touch with our team"
    const val rateApp = "Send Feedback"
    const val rateAppBody = "Share your thoughts with us"
    const val termsOfService = "Terms of Service"
    const val termsOfServiceBody = "Review our terms"
    const val privacyPolicyLabel = "Privacy Policy"
    const val privacyPolicyBody = "How we handle your data"
    const val appVersion = "Version"
    const val appBuildNumber = "Build"
    const val savedDesignsEmptyCta = "Start creating"
    const val themeLabel = "Theme"
    const val themeBody = "Dark mode and display"
    const val themeDark = "Dark"
    const val themeLight = "Light"
    const val notificationsLabel = "Notifications"
    const val notificationsBody = "Manage push and email alerts"
    const val languageLabel = "Language"
    const val languageBody = "Change app language"

    // Paywall Sheet - 5 Step Flow
    const val proA11yClose = "Close"
    const val paywallA11yBack = "Go back"

    // Step 1: Plan Selection
    const val pwS1Heading = "Pick your plan"
    const val pwS1HeadingHighlight = "Unlimited AI redesigns, clean exports, no watermarks"
    const val pwS1Benefit1 = "AI-powered redesigns"
    const val pwS1Benefit2 = "Multiple design styles"
    const val pwS1Benefit3 = "Inspiration gallery"
    const val pwS1Benefit4 = "Save your favorites"
    const val pwS1Cta = "Continue"
    const val pwS1SocialProof = "Trusted by thousands of homeowners"

    // Step 2: Benefits reminder
    const val pwS2Heading = "Why go Pro?"
    const val pwS2Option1 = "Unlimited AI generations"
    const val pwS2Option2 = "All premium styles"
    const val pwS2Info = "Get unlimited generations, all styles, and clean exports."
    const val pwS2Cta = "Continue"

    // Step 3: Comparison
    const val pwS3Heading = "What's included in Pro"
    const val pwS3ColFeature = "Feature"
    const val pwS3ColFree = "Free"
    const val pwS3ColPremium = "Pro"
    const val pwS3Row1Feature = "AI room redesigns"
    const val pwS3Row1Free = "1/day"
    const val pwS3Row1Pro = "Unlimited"
    const val pwS3Row2Feature = "Export quality"
    const val pwS3Row2Free = "Standard"
    const val pwS3Row2Pro = "High quality"
    const val pwS3Row3Feature = "Output style"
    const val pwS3Row3Free = "Branded"
    const val pwS3Row3Pro = "No branding"
    const val pwS3Row4Feature = "Premium interior styles"
    const val pwS3Row4Free = "Limited"
    const val pwS3Row4Pro = "Full library"
    const val pwS3Row5Feature = "Processing speed"
    const val pwS3Row5Free = "Standard"
    const val pwS3Row5Pro = "Faster"
    const val pwS3Row6Feature = "Save projects"
    const val pwS3Row6Free = "Limited"
    const val pwS3Row6Pro = "Unlimited"
    const val pwS3Recommendation = "Upgrade for unlimited generations and faster processing."
    const val pwS3Cta = "Continue"

    // Step 4: Plans
    const val pwS4Heading = "Choose your plan"
    const val pwS4PlanYearlyTitle = "Yearly"
    const val pwS4PlanYearlyPrice = "\$39.99"
    const val pwS4PlanYearlyPer = "/year"
    const val pwS4PlanYearlyDetail = "Only \$3.33 / month"
    const val pwS4PlanYearlyBadge = "Most Popular"
    const val pwS4PlanYearlySavings = "Save 58%"
    const val pwS4PlanMonthlyTitle = "Monthly"
    const val pwS4PlanMonthlyPrice = "\$7.99"
    const val pwS4PlanMonthlyPer = "/month"
    const val pwS4PlanFamilyTitle = "Family"
    const val pwS4PlanFamilyPrice = "\$59.99"
    const val pwS4PlanFamilyPer = "/year"
    const val pwS4PlanFamilyDetail = "Share with family"
    const val pwS4Cta = "Subscribe Now"
    const val pwS4Trust = "Cancel anytime."

    // Step 5: Checkout
    const val pwS5Heading = "Confirm your subscription"
    const val pwS5Badge = "Secure Checkout"
    const val pwS5PlanTitle = "Yearly"
    const val pwS5PlanSubtitle = "Annual subscription"
    const val pwS5TrialPeriod = "Plan"
    const val pwS5TrialValue = "Yearly billing"
    const val pwS5Then = "Then"
    const val pwS5RenewalDate = "Renewal date"
    const val pwS5Payment = "Payment"
    const val pwS5PaymentValue = "Secure web checkout"
    const val pwS5BenefitsTitle = "Included benefits"
    const val pwS5Benefit1 = "AI-powered redesigns"
    const val pwS5Benefit2 = "Export options"
    const val pwS5Benefit3 = "Clean output"
    const val pwS5Benefit4 = "Premium styles"
    const val pwS5Benefit5 = "Save projects"
    const val pwS5Benefit6 = "Faster processing"
    const val pwS5Cta = "Subscribe Now"
    const val pwS5Trust = "Cancel anytime."
    const val pwS5Legal = "By subscribing you agree to our Terms of Service and Privacy Policy."
    const val pwS5Restore = "Manage billing"

    // Legacy aliases used by SharedPaywallSheet and App.kt
    const val paywallV3PlanYearly = "Yearly"
    const val paywallV3PlanPerYear = "/year"
    const val paywallV3PlanAnnualDetail = "Billed annually. Best value."
    const val paywallV3PlanMonthly = "Monthly"
    const val paywallV3PlanPerMonth = "/month"
    const val paywallV3PlanMonthlyDetail = "Billed monthly."
    const val paywallV3PlanFamily = "Family"
    const val paywallV3PlanFamilyDetail = "Share with family members."

    const val terms = "Terms"
    const val privacyPolicy = "Privacy Policy"

    // Upgrade Screen
    const val proActivated = "Pro Activated"
    const val activeProAccess = "You have Pro access. Generate unlimited designs."
    const val upgradeV3Headline = "Unlimited Redesigns. Clean Export."
    const val upgradeV3Subtitle = "Unlimited generations, high-resolution export, no watermarks."
    const val upgradeV3TrialBadge = "Cancel anytime"
    const val upgradeV3Trust = "Cancel anytime. No commitment."
    const val upgradeV3Before = "Before"
    const val upgradeV3After = "After"
    const val upgradeV3BenefitGenerations = "AI-powered redesigns"
    const val upgradeV3BenefitExport = "Export options"
    const val upgradeV3BenefitNoWatermark = "Clean, branded output"
    const val upgradeV3BenefitStyles = "All available styles"
    const val upgradeV3BenefitHistory = "Design history"
    const val upgradeV3Cta = "Get Pro"
    const val upgradeV3Secondary = "Compare plans"
    const val upgradePlanMonthly = "Monthly"
    const val upgradePlanMonthlyPrice = "$7.99"
    const val upgradePlanMonthlyPeriod = "/month"
    const val upgradePlanYearly = "Yearly"
    const val upgradePlanYearlyPrice = "$39.99"
    const val upgradePlanYearlyPeriod = "/year"
    const val upgradePlanYearlySave = "Save 58%"
    const val upgradePlanBestValue = "Best Value"
    const val upgradePlanFamily = "Family"
    const val upgradePlanFamilyPrice = "$59.99"
    const val upgradePlanFamilyPeriod = "/year"
    const val upgradePlanFamilySubtitle = "Share with family"
    const val upgradePlanMonthlyDesc = "Try Pro risk-free. Unlimited generations and clean exports, no long commitment."
    const val upgradePlanYearlyDesc = "Best deal. Unlimited generations, HD export, and all premium styles \u2014 save 58%."
    const val upgradePlanFamilyDesc = "Share with up to 5 family members. Each gets their own unlimited generations and clean exports."
    const val upgradePlanFamilySeats = "Up to 5 members"
    const val upgradeBenefitUnlimited = "AI-powered redesigns"
    const val upgradeBenefitExport = "Export options"
    const val upgradeBenefitNoWatermark = "Clean output"
    const val upgradeBenefitStyles = "All available styles"
    const val upgradeBenefitHistory = "Design history"
    const val upgradeBenefitUpdates = "Early access to new features"
    const val upgradeFeatureCompare = "What you get with Pro"
    const val upgradeFreePlan = "Free"
    const val upgradeProPlan = "Pro"
    const val upgradeCompareGenerations = "AI Generations"
    const val upgradeCompareGenerationsFree = "1 per day"
    const val upgradeCompareGenerationsPro = "Unlimited"
    const val upgradeCompareExport = "Export Quality"
    const val upgradeCompareExportFree = "Standard"
    const val upgradeCompareExportPro = "Premium HD"
    const val upgradeCompareWatermark = "Watermark"
    const val upgradeCompareWatermarkFree = "Yes"
    const val upgradeCompareWatermarkPro = "None"
    const val upgradeCompareSpeed = "Processing"
    const val upgradeCompareSpeedFree = "Standard"
    const val upgradeCompareSpeedPro = "Faster"
    const val upgradeCompareStyles = "Design Styles"
    const val upgradeCompareStylesFree = "5 basic"
    const val upgradeCompareStylesPro = "All available"
    const val upgradeCompareSupport = "Support"
    const val upgradeCompareSupportFree = "Community"
    const val upgradeCompareSupportPro = "Email"
    const val upgradePlanBestValueSave = "Best Value \u00b7 Save 58%"
    const val upgradeFeatureTableTitle = "Feature Comparison"
    const val upgradeFeatureTableHeaderFeature = "Features"
    const val upgradeFeatureTableHeaderFree = "Free Plan"
    const val upgradeFeatureTableHeaderPro = "Pro Plan"
    const val upgradeFeatureGenerations = "Unlimited Generations"
    const val upgradeFeatureAiTools = "All 8 AI Tools"
    const val upgradeFeatureExport = "High Resolution Export"
    const val upgradeFeatureNoWatermark = "No Watermark"
    const val upgradeFeatureQueue = "Fast Generation Queue"

    // Feature comparison table (Free vs Pro values)
    const val upgradeFeatureOutput = "Output Style"
    const val upgradeFeatureFreeOutput = "Branded"
    const val upgradeFeatureProOutput = "No branding"
    const val upgradeFeaturePremiumStyles = "Premium Interior Styles"
    const val upgradeFeatureFreePremiumStyles = "Limited"
    const val upgradeFeatureProPremiumStyles = "Full library"
    const val upgradeFeatureSpeed = "Processing Speed"
    const val upgradeFeatureFreeSpeed = "Standard"
    const val upgradeFeatureProSpeed = "Faster"
    const val upgradeFeatureSaveProjects = "Save Projects"
    const val upgradeFeatureFreeSaveProjects = "Limited"
    const val upgradeFeatureProSaveProjects = "Unlimited"
    const val upgradeFeatureFreeGenerations = "1/day"
    const val upgradeFeatureProGenerations = "Unlimited"
    const val upgradeFeatureFreeExport = "Standard"
    const val upgradeFeatureProExport = "High quality"

    // Web Wizard
    const val wizardStepUpload = "Upload Photo"
    const val wizardStepRoom = "Room Type"
    const val wizardStepStyle = "Style"
    const val wizardStepReview = "Review"
    const val wizardUploadTitle = "Add a photo of your room"
    const val wizardUploadSubtitle = "Upload a photo and we'll redesign it with AI in seconds"
    const val wizardChooseImage = "Choose image"
    const val wizardTryExample = "Try with an example"
    const val wizardTryExampleSubtitle = "Use a sample photo to see how it works"
    const val wizardUploadChange = "Change photo"
    const val wizardRoomTitle = "What type of space is this?"
    const val wizardRoomSubtitle = "Select the option that best describes your space"
    const val wizardStyleTitle = "Choose a design style"
    const val wizardStyleSubtitle = "Pick a style to apply to your space"
    const val wizardReviewTitle = "Review your design"
    const val wizardReviewSubtitle = "Check your selections before generating"
    const val wizardReviewPhoto = "Photo"
    const val wizardReviewRoom = "Room"
    const val wizardReviewStyle = "Style"
    const val wizardGenerate = "Generate Design"
    const val wizardGenerateWithCost = "Generate (1 diamond)"
    const val wizardGenerating = "Generating..."
    const val wizardBack = "Back"
    const val wizardNext = "Next"
    const val wizardTryAgain = "Try again"
    const val wizardErrorPhoto = "Upload a photo to continue"
    const val wizardErrorRoom = "Select a room type"
    const val wizardErrorStyle = "Select a style"
    const val wizardClose = "Close"
    const val wizardExampleRoom = "Empty living room"
    const val wizardExampleGarden = "Backyard garden"
    const val wizardExampleExterior = "House facade"
    const val wizardDropHere = "Drop your photo here"
    const val wizardImageFormats = "JPG, PNG or WebP"
    const val wizardPhotoSelected = "Photo added"
    const val wizardRemove = "Remove"
    const val wizardReady = "Ready"
    const val wizardGeneratingBody = "This usually takes 15–30 seconds..."
    const val wizardGeneratingStep1 = "Analyzing your space..."
    const val wizardGeneratingStep2 = "Applying design transformations..."
    const val wizardGeneratingStep3 = "Adding finishing touches..."
    const val wizardErrorTitle = "Generation Failed"

    // Trust & Privacy
    const val wizardPrivacyNote = "Your photo is processed privately and deleted after"
    const val wizardCostNote = "Uses 1 diamond \u2014 free users get 1 generation/day"
    const val wizardProTip = "Tip: clear photos produce the best results"

    // Upload step: workflow explainer
    const val wizardHowItWorks = "How it works"
    const val wizardWorkflowStep1 = "1. Upload your photo"
    const val wizardWorkflowStep2 = "2. Pick a room type & style"
    const val wizardWorkflowStep3 = "3. Get your AI-designed space"
    const val wizardUploadPhotoTip1 = "Show the full room or area"
    const val wizardUploadPhotoTip2 = "Use good lighting, avoid blur"
    const val wizardUploadPhotoTip3 = "Include furniture for best results"
    const val wizardUploadDisabledHint = "Upload a photo or try an example to continue"
    const val wizardExampleCtaLabel = "See it in action"
    const val wizardExampleCtaSubtitle = "Skip the upload \u2014 preview a full transformation with a sample photo"

    // Upload step: post-upload helper
    const val wizardUploadAfterHelper = "After uploading, you'll pick a style and your AI redesign will be ready in 15\u201330 seconds"

    // Step-specific disabled hints (shown below Next when a required field is missing)
    const val wizardHintUpload = "Upload a photo or try an example to continue"
    const val wizardHintRoomType = "Select a room type to continue"
    const val wizardHintStyle = "Pick a style to continue"
    const val wizardHintPalette = "Choose a color palette to continue"
    const val wizardHintMaterial = "Select a material to continue"
    const val wizardHintGoals = "Select at least one goal to continue"
    const val wizardHintMask = "Draw over the object in the photo to select it"
    const val wizardHintReplacementPrompt = "Describe what to put in its place"
    const val wizardHintTransferStrength = "Choose a transfer strength to continue"
    const val wizardHintPaintColor = "Pick a paint color to continue"
    const val wizardHintFloorStyle = "Choose a floor style to continue"
    const val wizardHintFurnitureType = "Select a furniture type to continue"
    const val wizardHintReplacementStyle = "Pick a replacement style to continue"
    const val wizardHintReferenceImage = "Upload a reference image to continue"


    const val wizardExpectNote = "Results typically arrive in 15\u201330 seconds"
    const val wizardResultReady = "Your design is ready"
    const val wizardResultSubtitle = "Here's your AI-transformed space \u2014 compare it with the original"
    const val wizardBeforeLabel = "Original"
    const val wizardAfterLabel = "AI Design"
    const val wizardSaveToBoard = "Save to Board"
    const val wizardShare = "Share"
    const val wizardNewDesign = "New Design"

    // Discover hover overlay
    const val discoverPreview = "Preview"
    const val discoverUseStyle = "Use Style"
    const val discoverSave = "Save"

    // Advanced Controls
    const val advancedControlsTitle = "Advanced Controls"
    const val budgetLabel = "Budget"
    const val avoidLabel = "Avoid"
    const val keepLabel = "Keep"
    const val changeLabel = "Change"
    val budgetModes = listOf("Low budget", "Medium budget", "Luxury")
    val avoidOptions = listOf(
        "No dark colors",
        "No structural changes",
        "No plants",
        "Keep windows",
        "No furniture changes",
    )
    data class AdvancedControlSpec(val keepOptions: List<String>, val changeOptions: List<String>)
    val advancedControlSpecs = mapOf(
        "interior" to AdvancedControlSpec(
            keepOptions = listOf("Layout", "Windows", "Floor", "Main furniture"),
            changeOptions = listOf("Style", "Colors", "Decor", "Lighting"),
        ),
        "facade" to AdvancedControlSpec(
            keepOptions = listOf("Structure", "Windows", "Roof", "Entrance"),
            changeOptions = listOf("Facade", "Colors", "Lighting", "Landscaping"),
        ),
        "garden" to AdvancedControlSpec(
            keepOptions = listOf("Layout", "Trees", "Pool", "Patio", "Fence"),
            changeOptions = listOf("Plants", "Lighting", "Furniture", "Paths"),
        ),
        "layout" to AdvancedControlSpec(
            keepOptions = listOf("Walls", "Windows", "Doors", "Key furniture"),
            changeOptions = listOf("Organization", "Circulation", "Storage", "Zones"),
        ),
        "reference" to AdvancedControlSpec(
            keepOptions = listOf("Layout", "Furniture", "Main colors"),
            changeOptions = listOf("Style", "Ambiance", "Materials", "Decor"),
        ),
    )

    // Tool titles and descriptions
    fun toolTitle(toolId: String): String = when (toolId) {
        "interior" -> "Interior Design"
        "facade" -> "Exterior Design"
        "garden" -> "Garden Design"
        "paint" -> "Smart Wall Paint"
        "floor" -> "Floor Design"
        "layout" -> "Layout Makeover"
        "replace" -> "Replace Furniture"
        "reference" -> "Reference Style"
        else -> toolId.replaceFirstChar { it.uppercase() }
    }

    fun toolDescription(toolId: String): String = when (toolId) {
        "interior" -> "Redesign any room with AI-powered interior concepts"
        "facade" -> "Transform your home's exterior with modern facade styles"
        "garden" -> "Plan and visualize your dream outdoor landscape"
        "paint" -> "Preview smart wall paint colors on your space instantly"
        "floor" -> "Explore premium flooring from hardwood to marble tile"
        "layout" -> "Optimize room layout for better flow and functionality"
        "replace" -> "Swap furniture and decor with AI-generated alternatives"
        "reference" -> "Use any reference image to guide your design direction"
        else -> "Explore this tool to enhance your space"
    }

    fun toolCta(toolId: String): String = when (toolId) {
        "interior" -> "Redesign room"
        "facade" -> "Restyle exterior"
        "garden" -> "Plan garden"
        "paint" -> "Preview colors"
        "floor" -> "Try flooring"
        "layout" -> "Optimize layout"
        "replace" -> "Swap items"
        "reference" -> "Apply style"
        else -> "Try it"
    }

    fun toolCostNote(toolId: String): String = when (toolId) {
        "interior" -> "All styles included"
        "facade" -> "Instant preview"
        "garden" -> "Landscape AI"
        "paint" -> "See it live"
        "floor" -> "HD materials"
        "layout" -> "Smart fit"
        "replace" -> "Mask & swap"
        "reference" -> "Any image works"
        else -> ""
    }

    // Loading & Error
    const val loadingContent = "Loading..."
    const val errorGeneric = "Try again"

    // ── Auth Screen ──────────────────────────────────────────────────────────
    const val authWelcomeBack = "Sign in to continue"
    const val authCreateAccount = "Create your account"
    const val authSignInSubtitle = "Sign in to save your designs and access premium features across devices."
    const val authSignUpSubtitle = "Create an account to save designs and access them on any device."
    const val email = "Email"
    const val password = "Password"
    const val authForgotPassword = "Forgot password?"
    const val authDataProtected = "Photos are encrypted and never used for AI training."
    const val authNoAccountYet = "Don't have an account?"
    const val authHasAccount = "Already have an account?"
    const val authSignUp = "Sign up"
    const val authSignIn = "Sign in"
    const val authSignUpButton = "Get Started"
    const val authSignInButton = "Sign In"
    const val authContinueWithGoogle = "Continue with Google"
    const val authOr = "or"
    const val authLoading = "Signing in..."
    const val authErrorGeneric = "Sign-in failed. Check your email and password."
    const val authErrorNetwork = "No connection. Check your network and try again."
    const val authErrorInvalidCredentials = "Wrong email or password. Try again."
    const val authPasswordMin = "Password must be at least 6 characters"
    const val authTogglePasswordVisibility = "Toggle password visibility"
    const val authClose = "Close"
    const val authSignedInAs = "Signed in as"

    // ── A11y headings / landmark labels ─────────────────────────────────────
    const val a11yToolsHeading = "Tools"
    const val a11yDiscoverHeading = "Discover"
    const val a11yBoardHeading = "My Board"
    const val a11yProfileHeading = "Profile"
    const val a11yUpgradeHeading = "Upgrade to Pro"
    const val a11yBottomBar = "Main navigation"
    const val a11yTopBar = "Top navigation"
    const val a11yWizardHeading = "Design wizard"
    const val a11yPaywallHeading = "Subscription"
    const val a11ySettingsHeading = "Settings"
    const val a11yDiscoverDetailHeading = "Discover detail"

    // ── A11y labels for interactive controls ────────────────────────────────
    const val a11yDiamondBadge = "Credits badge"
    const val a11ySignInBackground = "Create an account to save your profile preferences and access them from any device"
    const val a11yBoardProBanner = "Upgrade to unlock all features"
    const val a11yWizardBack = "Go back to previous step"
    const val a11yWizardClose = "Close wizard"
    const val a11yWizardNext = "Go to next step"
    const val a11yWizardGenerate = "Generate design"
    const val a11yPaywallClose = "Close paywall"
    const val a11yPaywallBack = "Go to previous step"
    const val a11yPaywallCta = "Continue with selected plan"

    // ── A11y labels for loading states ──────────────────────────────────────
    const val a11yLoading = "Loading content"
    const val a11yGenerating = "Generating your design"
    const val a11yUploading = "Uploading image"

    // ── A11y labels for plan cards ──────────────────────────────────────────
    fun a11yPlanCard(title: String, price: String, period: String, selected: Boolean) =
        "$title, $price $period${if (selected) ", selected" else ""}"
    fun a11yUpgradePlanCard(title: String, price: String, period: String, recommended: Boolean) =
        "$title, $price $period${if (recommended) ", recommended" else ""}"

    // ── A11y labels for comparison tables ────────────────────────────────────
    const val a11yComparisonTable = "Feature comparison table"
    fun a11yComparisonRow(feature: String, freeValue: String, proValue: String) =
        "$feature: Free $freeValue, Pro $proValue"

    // ── A11y labels for wizard ───────────────────────────────────────────────
    const val a11yWizardStepIndicator = "Wizard progress"
    fun a11yWizardStepLabel(step: Int, total: Int, label: String) = "Step $step of $total: $label"

    // ── A11y labels for discover ─────────────────────────────────────────────
    fun a11yDiscoverFilterChip(label: String, selected: Boolean) =
        "$label${if (selected) ", selected" else ""}"

    // ── A11y labels for board cards ──────────────────────────────────────────
    fun a11yBoardCard(title: String, subtitle: String) = "$title, $subtitle"

    // ── A11y labels for modal close ──────────────────────────────────────────
    const val a11ySettingsClose = "Close settings"
    const val a11yDiamondStoreClose = "Close diamond store"

    // ── Test Tags (for Compose test / accessibility snapshots) ──────────────
    object TestTags {
        // Navigation
        const val bottomNav = "bottom_nav"
        const val topNav = "top_nav"
        const val bottomNavItem = "bottom_nav_item_%s"
        const val topNavItem = "top_nav_item_%s"

        // Screens
        const val toolsScreen = "tools_screen"
        const val discoverScreen = "discover_screen"
        const val boardScreen = "board_screen"
        const val profileScreen = "profile_screen"
        const val upgradeScreen = "upgrade_screen"
        const val wizardScreen = "wizard_screen"
        const val settingsScreen = "settings_screen"

        // Tools
        const val toolsHeader = "tools_header"
        const val toolCard = "tool_card_%s"

        // Discover
        const val discoverClusterTab = "discover_cluster_tab_%s"
        const val discoverClusterTabRow = "discover_cluster_tab_row"
        const val discoverSectionRow = "discover_section_row_%s"
        const val discoverSectionCard = "discover_section_card_%s"
        const val discoverSectionScroll = "discover_section_scroll"
        const val discoverSeeAll = "discover_see_all_%s"

        // Board
        const val boardTabRow = "board_tab_row"
        const val boardTab = "board_tab_%s"
        const val boardGuestHero = "board_guest_hero"
        const val boardSignInButton = "board_sign_in_button"
        const val boardGeneratedCard = "board_generated_card_%s"
        const val boardFavoriteCard = "board_favorite_card_%s"
        const val boardProjectCard = "board_project_card_%s"
        const val boardLockedCard = "board_locked_card_%s"

        // Profile
        const val profileHeading = "profile_heading"
        const val profileSettingsButton = "profile_settings_button"
        const val profileSignInButton = "profile_sign_in_button"
        const val profileStatusCard = "profile_status_card_%s"
        const val profileRow = "profile_row_%s"

        // Wizard
        const val wizardHeader = "wizard_header"
        const val wizardBackButton = "wizard_back_button"
        const val wizardCloseButton = "wizard_close_button"
        const val wizardProgressBar = "wizard_progress_bar"
        const val wizardUploadDropZone = "wizard_upload_drop_zone"
        const val wizardTryExample = "wizard_try_example"
        const val wizardPhotoPreview = "wizard_photo_preview"
        const val wizardOptionCard = "wizard_option_card_%s"
        const val wizardStyleCard = "wizard_style_card_%s"
        const val wizardGenerateButton = "wizard_generate_button"
        const val wizardBottomBar = "wizard_bottom_bar"
        const val wizardBackStepButton = "wizard_back_step_button"
        const val wizardNextStepButton = "wizard_next_step_button"
        const val wizardReviewEditRoom = "wizard_review_edit_room"
        const val wizardReviewEditStyle = "wizard_review_edit_style"
        const val wizardStepContent = "wizard_step_content_%s"

        // Paywall
        const val paywallSheet = "paywall_sheet"
        const val paywallTopBar = "paywall_top_bar"
        const val paywallCloseButton = "paywall_close_button"
        const val paywallBackButton = "paywall_back_button"
        const val paywallStepIndicator = "paywall_step_indicator"
        const val paywallStepContent = "paywall_step_content_%d"
        const val paywallPlanCard = "paywall_plan_card_%s"
        const val paywallCtaButton = "paywall_cta_button"
        const val paywallRestoreButton = "paywall_restore_button"

        // Upgrade
        const val upgradeCtaButton = "upgrade_cta_button"
        const val upgradePlanCard = "upgrade_plan_card_%s"
        const val upgradeBeforeAfter = "upgrade_before_after"

        // Settings
        const val settingsRow = "settings_row_%s"
    }

    // ── A11y labels for test tags with dynamic values ───────────────────────
    fun a11yBottomNavItem(label: String) = "Navigate to $label"
    fun a11yTopNavItem(label: String) = "Navigate to $label"
    fun a11yBoardTab(label: String, selected: Boolean) = "$label tab${if (selected) ", selected" else ""}"
    fun a11yPaywallPlan(title: String, selected: Boolean) = "$title plan${if (selected) ", selected" else ""}"
    fun a11yUpgradePlan(title: String, recommended: Boolean) = "$title plan${if (recommended) ", recommended" else ""}"
    fun a11yProfileRow(title: String) = title
    fun a11yWizardOption(label: String, selected: Boolean) = "$label${if (selected) ", selected" else ""}"
    fun a11yDiscoverCard(title: String, category: String) = "$title \u2014 $category"
    fun a11yBoardDesign(title: String, style: String, tab: String) = "$title \u2014 $style \u2014 $tab"
    fun a11yPaywallStep(step: Int, heading: String) = "Step $step: $heading"
    fun a11yWizardStep(step: Int, total: Int, label: String) = "Step $step of $total: $label"

    // ── Test tag formatting helper ──────────────────────────────────────────
    fun formatTestTag(pattern: String, vararg args: Any): String {
        var result = pattern
        args.forEach { result = result.replaceFirst("%s", it.toString()) }
        return result
    }
}
