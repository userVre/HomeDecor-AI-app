package com.ismail.homedecorai.ui.store

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.model.DiamondPack
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.R
import com.ismail.homedecorai.purchaseAttemptMessageRes
import com.ismail.homedecorai.rawServiceMessageToKind
import com.ismail.homedecorai.storeMessageRes
import com.ismail.homedecorai.ui.components.*
import com.ismail.homedecorai.ui.components.PurchaseSyncNotice
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.*
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.models.StoreTransaction

@Composable
fun DiamondStoreSheet(
    state: HomeDecorUiState,
    onClose: () -> Unit,
    onFulfill: (String, String, String, String?, Double, String, Double) -> Unit,
    onRetrySync: () -> Unit,
    onDailyRewardClaim: () -> Boolean,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val isDark = isSystemInDarkTheme()
    val scrimTapBlocker = remember { MutableInteractionSource() }
    val sheetTapBlocker = remember { MutableInteractionSource() }
    var packages by remember { mutableStateOf<List<Package>>(emptyList()) }
    var storeLoading by remember { mutableStateOf(true) }
    var loadAttempt by remember { mutableStateOf(0) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var loadingPack by remember { mutableStateOf<String?>(null) }
    var syncingPack by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(state.purchaseBusy) {
        if (!state.purchaseBusy) syncingPack = null
    }
    LaunchedEffect(loadAttempt) {
        storeLoading = true
        loadError = null
        message = null
        packages = emptyList()
        if (!Purchases.isConfigured) {
            storeLoading = false
            loadError = resources.getString(R.string.store_purchases_unavailable)
            return@LaunchedEffect
        }
        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: com.revenuecat.purchases.Offerings) {
                packages = ((offerings.current?.availablePackages ?: emptyList()) + offerings.all.values.flatMap { it.availablePackages })
                    .distinctBy { "${it.identifier}:${it.product.id}" }
                storeLoading = false
            }

            override fun onError(error: PurchasesError) {
                storeLoading = false
                loadError = resources.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.store_load_packs_failed))
            }
        })
    }
    val mappedPackages = remember(packages) {
        HomeDecorCatalog.diamondPacks.associate { pack ->
            val matches = packages.filter { it.matchesDiamondPack(pack) }
            pack.id to matches.singleOrNull()
        }
    }
    val mappedProductCounts = mappedPackages.values.filterNotNull().groupingBy { it.product.id }.eachCount()
    fun packageFor(pack: DiamondPack): Package? {
        val productPackage = mappedPackages[pack.id] ?: return null
        return productPackage.takeIf { mappedProductCounts[it.product.id] == 1 }
    }
    fun buy(pack: DiamondPack) {
        val productPackage = packageFor(pack)
        val activity = context.findActivity()
        if (storeLoading || loadingPack != null || state.purchaseBusy) return
        if (productPackage == null || activity == null || !Purchases.isConfigured) {
            message = resources.getString(R.string.pack_unavailable_google)
            return
        }
        loadingPack = pack.id
        syncingPack = null
        message = null
        Purchases.sharedInstance.purchase(PurchaseParams.Builder(activity, productPackage).build(), object : PurchaseCallback {
            override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                loadingPack = null
                syncingPack = pack.id
                message = resources.getString(R.string.purchase_google_confirmed)
                onFulfill(
                    pack.id,
                    storeTransaction.orderId ?: storeTransaction.purchaseToken,
                    productPackage.product.id,
                    productPackage.identifier,
                    productPackage.product.price.amountMicros / 1_000_000.0,
                    productPackage.product.price.currencyCode,
                    storeTransaction.purchaseTime.toDouble(),
                )
            }

            override fun onError(error: PurchasesError, userCancelled: Boolean) {
                loadingPack = null
                syncingPack = null
                message = if (userCancelled) {
                    resources.getString(R.string.purchase_cancelled)
                } else {
                    resources.getString(rawServiceMessageToKind(context, error.message).purchaseAttemptMessageRes(R.string.purchase_failed))
                }
            }
        })
    }
    val hasPartialMapping = !storeLoading && loadError == null && packages.isNotEmpty() && HomeDecorCatalog.diamondPacks.any { packageFor(it) == null }
    val notice = when {
        loadError != null -> loadError
        message != null -> message
        hasPartialMapping -> stringResource(R.string.store_some_packs_unavailable)
        else -> state.purchaseMessage
    }
    val closeDescription = stringResource(R.string.close)

    val sheetContainerColor = if (isDark) {
        MaterialTheme.colorScheme.surfaceContainerLow
    } else {
        MaterialTheme.colorScheme.surface
    }

    val handleColor = if (isDark) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    }

    val titleColor = if (isDark) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val subtitleColor = if (isDark) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val sectionLabelColor = if (isDark) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = if (isDark) 0.6f else 0.35f))
            .clickable(
                interactionSource = scrimTapBlocker,
                indication = null,
                onClick = onClose,
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = sheetContainerColor,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .clickable(
                    interactionSource = sheetTapBlocker,
                    indication = null,
                    onClick = {},
                ),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = HomeDecorSpacing.Xxl),
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
            ) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(top = HomeDecorSpacing.Md),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(handleColor),
                        )
                    }
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.diamond_store_title),
                                color = titleColor,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = (-0.5).sp,
                            )
                            Spacer(Modifier.height(HomeDecorSpacing.Xxs))
                            Text(
                                stringResource(R.string.diamond_store_subtitle),
                                color = subtitleColor,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isDark) {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            } else {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            },
                        ) {
                            Row(
                                Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                            ) {
                                Icon(
                                    Icons.Rounded.Diamond,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = HomeDecorExtra.diamondAccent,
                                )
                                Text(
                                    "${state.diamonds}",
                                    color = titleColor,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .padding(start = HomeDecorSpacing.Sm)
                                .minimumTouchTarget()
                                .semantics {
                                    contentDescription = closeDescription
                                    role = Role.Button
                                },
                        ) {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = stringResource(R.string.close),
                                tint = subtitleColor,
                            )
                        }
                    }
                }

                item {
                    DailyRewardCard(
                        state = state,
                        onClaim = onDailyRewardClaim,
                    )
                }

                item {
                    Text(
                        stringResource(R.string.get_more_credits),
                        color = sectionLabelColor,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = HomeDecorSpacing.Lg, top = HomeDecorSpacing.Lg, bottom = HomeDecorSpacing.Sm),
                    )
                }

                if (storeLoading && loadError == null) {
                    item {
                        Row(
                            Modifier.padding(horizontal = HomeDecorSpacing.Lg),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = HomeDecorExtra.diamondAccent,
                                strokeWidth = 2.dp,
                            )
                            Text(
                                stringResource(R.string.loading_packs),
                                color = subtitleColor,
                            )
                        }
                    }
                }

                if (loadError != null) {
                    item {
                        ElevatedCard(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = if (isDark) {
                                    MaterialTheme.colorScheme.surfaceContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainerLow
                                },
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = HomeDecorSpacing.Lg),
                        ) {
                            Column(
                                modifier = Modifier.padding(HomeDecorSpacing.Base),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
                            ) {
                                Icon(
                                    Icons.Rounded.Diamond,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = HomeDecorExtra.diamondAccent.copy(alpha = 0.5f),
                                )
                                Text(
                                    loadError.orEmpty(),
                                    color = subtitleColor,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                )
                                OutlinedButton(
                                    onClick = { loadAttempt += 1 },
                                    enabled = !storeLoading && loadingPack == null && !state.purchaseBusy,
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = HomeDecorExtra.diamondAccent,
                                    ),
                                    modifier = Modifier.height(HomeDecorSpacing.Xxl),
                                ) {
                                    Icon(Icons.Rounded.Refresh, null, Modifier.size(14.dp))
                                    Spacer(Modifier.width(HomeDecorSpacing.Sm))
                                    Text(
                                        stringResource(R.string.retry),
                                        style = MaterialTheme.typography.labelMedium,
                                    )
                                }
                            }
                        }
                    }
                }

                if (notice != null && loadError == null) {
                    item {
                        if (state.pendingPurchaseSync != null && message == null) {
                            PurchaseSyncNotice(
                                message = notice.orEmpty(),
                                pending = true,
                                busy = state.purchaseBusy,
                                onRetry = onRetrySync,
                            )
                        } else {
                            Text(
                                notice.orEmpty(),
                                color = subtitleColor,
                                modifier = Modifier.padding(horizontal = HomeDecorSpacing.Lg),
                            )
                        }
                    }
                }

                if (loadError == null) {
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(start = HomeDecorSpacing.Lg, end = HomeDecorSpacing.Lg),
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                            modifier = Modifier.padding(top = HomeDecorSpacing.Xs),
                        ) {
                            items(
                                count = HomeDecorCatalog.diamondPacks.size,
                                key = { HomeDecorCatalog.diamondPacks[it].id },
                            ) { index ->
                                val pack = HomeDecorCatalog.diamondPacks[index]
                                val productPackage = packageFor(pack)
                                val purchaseBlocked = loadingPack != null || state.purchaseBusy
                                DiamondOfferCard(
                                    pack = pack.copy(
                                        price = productPackage?.product?.price?.formatted
                                            ?: pack.price,
                                    ),
                                    packIndex = index,
                                    unavailable = !storeLoading && productPackage == null,
                                    loading = storeLoading || loadingPack == pack.id || syncingPack == pack.id,
                                    purchaseBlocked = purchaseBlocked,
                                    isDark = isDark,
                                    onClick = { buy(pack) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DiamondOfferCard(
    pack: DiamondPack,
    packIndex: Int,
    unavailable: Boolean = false,
    loading: Boolean = false,
    purchaseBlocked: Boolean = false,
    isDark: Boolean,
    onClick: () -> Unit,
) {
    val enabled = !loading && !unavailable && !purchaseBlocked
    val displayBadge = when (pack.id) {
        "architect" -> stringResource(R.string.pack_badge_best_value)
        "designer" -> stringResource(R.string.pack_badge_popular)
        "estate" -> stringResource(R.string.pack_badge_premium)
        else -> null
    }
    val packTitleRes = diamondPackTitleRes(pack)

    val infiniteTransition = rememberInfiniteTransition(label = "card_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow",
    )

    val accentColor = when (packIndex) {
        0 -> DiamondTeal
        1 -> PurpleGlow
        2 -> GreenGlow
        else -> GoldGlow
    }

    val cardContainerColor = when {
        unavailable -> {
            if (isDark) MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.5f)
        }
        isDark -> MaterialTheme.colorScheme.surfaceContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val cardBorderColor = when {
        unavailable -> {
            if (isDark) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        }
        displayBadge != null -> accentColor.copy(alpha = 0.35f)
        else -> {
            if (isDark) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
        }
    }

    val cardTopColor = when {
        unavailable -> {
            if (isDark) MaterialTheme.colorScheme.surfaceContainerLow
            else MaterialTheme.colorScheme.surfaceContainerLowest
        }
        packIndex == 0 -> if (isDark) Color(0xFF0D2233) else Color(0xFFE0F5F7)
        packIndex == 1 -> if (isDark) Color(0xFF1D1540) else Color(0xFFEDE5FF)
        packIndex == 2 -> if (isDark) Color(0xFF0D2A1A) else Color(0xFFE0F5E8)
        else -> if (isDark) Color(0xFF2A1D0A) else Color(0xFFFFF5E0)
    }

    val cardBottomColor = if (isDark) {
        MaterialTheme.colorScheme.surfaceContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val titleTextColor = when {
        unavailable -> {
            if (isDark) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        }
        isDark -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurface
    }

    val diamondCountColor = when {
        unavailable -> {
            if (isDark) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        }
        isDark -> accentColor
        else -> accentColor.copy(alpha = 0.9f)
    }

    val badgeTextColor = accentColor
    val badgeBgColor = if (isDark) {
        accentColor.copy(alpha = 0.15f)
    } else {
        accentColor.copy(alpha = 0.12f)
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = cardContainerColor,
        modifier = Modifier
            .width(172.dp)
            .height(280.dp),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = cardBorderColor,
                    shape = RoundedCornerShape(20.dp),
                ),
        ) {
            if (!unavailable && displayBadge != null) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = glowAlpha * 0.2f),
                                    Color.Transparent,
                                ),
                                radius = 180f,
                            ),
                        ),
                )
            }

            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    cardTopColor,
                                    cardBottomColor,
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    DiamondPackVisual(
                        packIndex = packIndex,
                        modifier = Modifier.fillMaxSize(),
                        unavailable = unavailable,
                    )

                    if (displayBadge != null && !unavailable) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = badgeBgColor,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp),
                        ) {
                            Text(
                                displayBadge,
                                modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xxs),
                                color = badgeTextColor,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp,
                            )
                        }
                    }

                    if (loading) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(
                                    if (isDark) Color.Black.copy(alpha = 0.4f)
                                    else Color.White.copy(alpha = 0.6f),
                                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = if (isDark) Color.White else HomeDecorExtra.diamondAccent,
                                strokeWidth = 2.5.dp,
                            )
                        }
                    }
                }

                Column(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = HomeDecorSpacing.Base, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(packTitleRes),
                        color = titleTextColor,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(HomeDecorSpacing.Xs))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            Icons.Rounded.Diamond,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = diamondCountColor,
                        )
                        Spacer(Modifier.width(HomeDecorSpacing.Xs))
                        Text(
                            stringResource(R.string.diamonds_amount, pack.diamonds),
                            color = diamondCountColor,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                if (unavailable) {
                    Text(
                        stringResource(R.string.unavailable),
                        modifier = Modifier
                            .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Md)
                            .height(HomeDecorSpacing.TouchTarget)
                            .fillMaxWidth(),
                        color = if (isDark) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    val buttonContainerColor = when {
                        isDark -> accentColor
                        else -> accentColor
                    }
                    val buttonContentColor = when {
                        packIndex == 3 && isDark -> Color(0xFF1A1000)
                        packIndex == 3 && !isDark -> Color(0xFF1A1000)
                        else -> Color.White
                    }
                    val disabledButtonContainer = if (isDark) {
                        MaterialTheme.colorScheme.surfaceContainerHighest
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                    val disabledButtonContent = if (isDark) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    }

                    Button(
                        onClick = onClick,
                        enabled = enabled,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonContainerColor,
                            contentColor = buttonContentColor,
                            disabledContainerColor = disabledButtonContainer,
                            disabledContentColor = disabledButtonContent,
                        ),
                        modifier = Modifier
                            .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Md)
                            .fillMaxWidth()
                            .height(HomeDecorSpacing.TouchTarget),
                        contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                    ) {
                        Text(
                            when {
                                loading -> stringResource(R.string.ellipsis)
                                else -> pack.price
                            },
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }
    }
}

private val DiamondTeal = Color(0xFF4DD9E0)
private val PurpleGlow = Color(0xFF9B6EFF)
private val GreenGlow = Color(0xFF34D399)
private val GoldGlow = Color(0xFFFFD166)

@Composable
private fun DiamondPackVisual(
    packIndex: Int,
    modifier: Modifier = Modifier,
    unavailable: Boolean = false,
) {
    val teal = DiamondTeal
    val tealLight = Color(0xFF7AEAEA)
    val purple = PurpleGlow
    val purpleLight = Color(0xFFBBA8FF)
    val green = GreenGlow
    val greenLight = Color(0xFF6EE7B7)
    val gold = GoldGlow
    val goldLight = Color(0xFFFFE680)
    val boxBrown = Color(0xFF6B4226)
    val boxDark = Color(0xFF4A2E18)
    val goldMetal = Color(0xFFD4A843)

    val alphaMod = if (unavailable) 0.35f else 1f

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h * 0.55f

        val glowAlpha = 0.1f + packIndex * 0.08f
        val glowRadius = w * (0.3f + packIndex * 0.08f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    when (packIndex) {
                        1 -> purple.copy(alpha = glowAlpha * alphaMod)
                        2 -> green.copy(alpha = glowAlpha * alphaMod)
                        3 -> gold.copy(alpha = glowAlpha * alphaMod)
                        else -> teal.copy(alpha = glowAlpha * alphaMod)
                    },
                    Color.Transparent,
                ),
                center = Offset(cx, cy),
                radius = glowRadius,
            ),
            radius = glowRadius,
            center = Offset(cx, cy),
        )

        when (packIndex) {
            0 -> drawSmallBox(cx, cy, boxBrown, boxDark, goldMetal, teal, tealLight, alphaMod)
            1 -> drawMediumChest(cx, cy, boxBrown, boxDark, goldMetal, purple, purpleLight, alphaMod)
            2 -> drawLargeChest(cx, cy, boxBrown, boxDark, goldMetal, green, greenLight, alphaMod)
            else -> drawGrandCart(cx, cy, boxBrown, boxDark, goldMetal, gold, goldLight, alphaMod)
        }
    }
}

private fun DrawScope.drawSmallBox(
    cx: Float, cy: Float,
    boxBrown: Color, boxDark: Color, goldMetal: Color,
    teal: Color, tealLight: Color,
    alpha: Float = 1f,
) {
    drawRoundRect(
        boxBrown.copy(alpha = alpha),
        Offset(cx - 22f, cy + 2f),
        Size(44f, 24f),
        CornerRadius(5f),
    )
    drawRoundRect(
        boxDark.copy(alpha = alpha),
        Offset(cx - 24f, cy - 6f),
        Size(48f, 10f),
        CornerRadius(4f),
    )
    drawRect(goldMetal.copy(alpha = alpha), Offset(cx - 22f, cy + 8f), Size(44f, 2f))
    drawPath(diamondPath(cx - 6f, cy + 4f, 7f), teal.copy(alpha = alpha))
    drawPath(diamondPath(cx + 8f, cy + 2f, 5f), tealLight.copy(alpha = alpha))
    drawPath(diamondPath(cx + 1f, cy + 12f, 4f), teal.copy(alpha = 0.7f * alpha))
    drawCircle(Color.White.copy(alpha = 0.8f * alpha), 1.5f, Offset(cx - 10f, cy - 2f))
    drawCircle(Color.White.copy(alpha = 0.5f * alpha), 1f, Offset(cx + 12f, cy - 4f))
}

private fun DrawScope.drawMediumChest(
    cx: Float, cy: Float,
    boxBrown: Color, boxDark: Color, goldMetal: Color,
    purple: Color, purpleLight: Color,
    alpha: Float = 1f,
) {
    drawRoundRect(
        boxBrown.copy(alpha = alpha),
        Offset(cx - 30f, cy),
        Size(60f, 34f),
        CornerRadius(6f),
    )
    val lid = Path().apply {
        moveTo(cx - 32f, cy)
        quadraticBezierTo(cx, cy - 24f, cx + 32f, cy)
        close()
    }
    drawPath(lid, boxDark.copy(alpha = alpha))
    drawRect(goldMetal.copy(alpha = alpha), Offset(cx - 30f, cy + 10f), Size(60f, 2.5f))
    drawCircle(goldMetal.copy(alpha = alpha), 4f, Offset(cx, cy + 10f))
    drawPath(diamondPath(cx - 12f, cy + 4f, 10f), purple.copy(alpha = alpha))
    drawPath(diamondPath(cx + 4f, cy + 2f, 11f), purple.copy(alpha = alpha))
    drawPath(diamondPath(cx + 18f, cy + 6f, 8f), purpleLight.copy(alpha = alpha))
    drawPath(diamondPath(cx - 4f, cy + 16f, 7f), purple.copy(alpha = 0.75f * alpha))
    drawPath(diamondPath(cx + 12f, cy + 18f, 6f), purpleLight.copy(alpha = 0.6f * alpha))
    drawCircle(Color.White.copy(alpha = 0.85f * alpha), 2f, Offset(cx - 16f, cy - 2f))
    drawCircle(Color.White.copy(alpha = 0.6f * alpha), 1.5f, Offset(cx + 20f, cy - 6f))
    drawCircle(Color.White.copy(alpha = 0.4f * alpha), 1f, Offset(cx + 8f, cy - 12f))
}

private fun DrawScope.drawLargeChest(
    cx: Float, cy: Float,
    boxBrown: Color, boxDark: Color, goldMetal: Color,
    green: Color, greenLight: Color,
    alpha: Float = 1f,
) {
    drawRoundRect(
        boxBrown.copy(alpha = alpha),
        Offset(cx - 36f, cy + 2f),
        Size(72f, 38f),
        CornerRadius(6f),
    )
    val lid = Path().apply {
        moveTo(cx - 38f, cy + 2f)
        quadraticBezierTo(cx, cy - 24f, cx + 38f, cy + 2f)
        close()
    }
    drawPath(lid, boxDark.copy(alpha = alpha))
    drawRect(goldMetal.copy(alpha = alpha), Offset(cx - 36f, cy + 12f), Size(72f, 2.5f))
    drawRect(goldMetal.copy(alpha = alpha), Offset(cx - 36f, cy + 30f), Size(72f, 2.5f))
    drawCircle(goldMetal.copy(alpha = alpha), 5f, Offset(cx, cy + 12f))
    drawPath(diamondPath(cx - 16f, cy + 4f, 11f), green.copy(alpha = alpha))
    drawPath(diamondPath(cx + 2f, cy + 2f, 12f), green.copy(alpha = alpha))
    drawPath(diamondPath(cx + 18f, cy + 5f, 10f), greenLight.copy(alpha = alpha))
    drawPath(diamondPath(cx - 8f, cy + 16f, 8f), green.copy(alpha = alpha))
    drawPath(diamondPath(cx + 10f, cy + 18f, 7f), greenLight.copy(alpha = 0.7f * alpha))
    drawPath(diamondPath(cx - 26f, cy + 8f, 7f), green.copy(alpha = 0.55f * alpha))
    drawPath(diamondPath(cx + 26f, cy + 10f, 6f), greenLight.copy(alpha = 0.5f * alpha))
    drawPath(diamondPath(cx - 18f, cy + 22f, 5f), green.copy(alpha = 0.4f * alpha))
    drawPath(diamondPath(cx + 20f, cy + 24f, 5f), green.copy(alpha = 0.4f * alpha))
    drawPath(diamondPath(cx, cy - 8f, 9f), greenLight.copy(alpha = 0.85f * alpha))
    drawPath(diamondPath(cx - 14f, cy - 6f, 7f), green.copy(alpha = 0.65f * alpha))
    drawCircle(Color.White.copy(alpha = 0.85f * alpha), 2f, Offset(cx - 22f, cy - 4f))
    drawCircle(Color.White.copy(alpha = 0.6f * alpha), 1.5f, Offset(cx + 24f, cy - 8f))
    drawCircle(Color.White.copy(alpha = 0.5f * alpha), 1.5f, Offset(cx + 6f, cy - 16f))
    drawCircle(Color.White.copy(alpha = 0.35f * alpha), 1f, Offset(cx - 10f, cy - 14f))
}

private fun DrawScope.drawGrandCart(
    cx: Float, cy: Float,
    boxBrown: Color, boxDark: Color, goldMetal: Color,
    gold: Color, goldLight: Color,
    alpha: Float = 1f,
) {
    val cart = Path().apply {
        moveTo(cx - 32f, cy - 2f)
        lineTo(cx - 38f, cy + 22f)
        lineTo(cx + 38f, cy + 22f)
        lineTo(cx + 32f, cy - 2f)
        close()
    }
    drawPath(cart, boxBrown.copy(alpha = alpha))
    drawRect(goldMetal.copy(alpha = alpha), Offset(cx - 38f, cy - 4f), Size(76f, 4f))
    drawCircle(boxDark.copy(alpha = alpha), 7f, Offset(cx - 20f, cy + 30f))
    drawCircle(boxDark.copy(alpha = alpha), 7f, Offset(cx + 20f, cy + 30f))
    drawCircle(goldMetal.copy(alpha = alpha), 3.5f, Offset(cx - 20f, cy + 30f))
    drawCircle(goldMetal.copy(alpha = alpha), 3.5f, Offset(cx + 20f, cy + 30f))

    drawPath(diamondPath(cx - 14f, cy - 0f, 11f), gold.copy(alpha = alpha))
    drawPath(diamondPath(cx + 2f, cy - 6f, 13f), gold.copy(alpha = alpha))
    drawPath(diamondPath(cx + 16f, cy - 0f, 10f), goldLight.copy(alpha = alpha))
    drawPath(diamondPath(cx - 6f, cy - 12f, 9f), gold.copy(alpha = alpha))
    drawPath(diamondPath(cx + 10f, cy - 12f, 9f), goldLight.copy(alpha = alpha))
    drawPath(diamondPath(cx + 2f, cy - 20f, 8f), gold.copy(alpha = 0.9f * alpha))

    drawPath(diamondPath(cx - 26f, cy + 4f, 7f), gold.copy(alpha = 0.6f * alpha))
    drawPath(diamondPath(cx + 26f, cy + 6f, 6f), goldLight.copy(alpha = 0.55f * alpha))
    drawPath(diamondPath(cx - 10f, cy + 12f, 5f), gold.copy(alpha = 0.5f * alpha))
    drawPath(diamondPath(cx + 12f, cy + 14f, 5f), gold.copy(alpha = 0.5f * alpha))
    drawPath(diamondPath(cx - 20f, cy - 10f, 6f), goldLight.copy(alpha = 0.45f * alpha))
    drawPath(diamondPath(cx + 20f, cy - 10f, 6f), gold.copy(alpha = 0.45f * alpha))

    drawPath(diamondPath(cx - 30f, cy + 8f, 5f), goldLight.copy(alpha = 0.35f * alpha))
    drawPath(diamondPath(cx + 30f, cy + 10f, 5f), gold.copy(alpha = 0.35f * alpha))
    drawPath(diamondPath(cx - 8f, cy + 18f, 4f), goldLight.copy(alpha = 0.3f * alpha))
    drawPath(diamondPath(cx + 14f, cy + 20f, 4f), gold.copy(alpha = 0.3f * alpha))

    drawCircle(Color.White.copy(alpha = 0.9f * alpha), 2.5f, Offset(cx - 22f, cy - 18f))
    drawCircle(Color.White.copy(alpha = 0.7f * alpha), 2f, Offset(cx + 18f, cy - 24f))
    drawCircle(Color.White.copy(alpha = 0.6f * alpha), 1.5f, Offset(cx + 26f, cy - 8f))
    drawCircle(Color.White.copy(alpha = 0.5f * alpha), 1.5f, Offset(cx - 28f, cy - 6f))
    drawCircle(Color.White.copy(alpha = 0.4f * alpha), 1f, Offset(cx - 16f, cy - 26f))
    drawCircle(Color.White.copy(alpha = 0.3f * alpha), 1f, Offset(cx + 14f, cy - 28f))
    drawCircle(Color.White.copy(alpha = 0.25f * alpha), 1f, Offset(cx, cy - 32f))

    val spark = Path().apply {
        moveTo(cx + 28f, cy - 16f)
        lineTo(cx + 30f, cy - 20f)
        lineTo(cx + 32f, cy - 16f)
        lineTo(cx + 36f, cy - 14f)
        lineTo(cx + 32f, cy - 12f)
        lineTo(cx + 30f, cy - 8f)
        lineTo(cx + 28f, cy - 12f)
        lineTo(cx + 24f, cy - 14f)
        close()
    }
    drawPath(spark, goldLight.copy(alpha = 0.5f * alpha))

    val spark2 = Path().apply {
        moveTo(cx - 26f, cy - 20f)
        lineTo(cx - 24f, cy - 24f)
        lineTo(cx - 22f, cy - 20f)
        lineTo(cx - 18f, cy - 18f)
        lineTo(cx - 22f, cy - 16f)
        lineTo(cx - 24f, cy - 12f)
        lineTo(cx - 26f, cy - 16f)
        lineTo(cx - 30f, cy - 18f)
        close()
    }
    drawPath(spark2, goldLight.copy(alpha = 0.35f * alpha))
}

private fun diamondPath(cx: Float, cy: Float, size: Float): Path = Path().apply {
    moveTo(cx, cy - size)
    lineTo(cx + size * 0.65f, cy)
    lineTo(cx, cy + size * 0.5f)
    lineTo(cx - size * 0.65f, cy)
    close()
}
