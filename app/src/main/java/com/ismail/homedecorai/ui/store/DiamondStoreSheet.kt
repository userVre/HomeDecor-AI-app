package com.ismail.homedecorai.ui.store

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(
                interactionSource = scrimTapBlocker,
                indication = null,
                onClick = onClose,
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = HomeDecorColors.DarkSurface,
            tonalElevation = 8.dp,
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
                                .background(Color.White.copy(alpha = 0.2f)),
                        )
                    }
                }

                item {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Base),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.diamond_store_title),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                stringResource(R.string.diamond_store_subtitle),
                                color = Color.White.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .minimumTouchTarget()
                                .semantics {
                                    contentDescription = closeDescription
                                    role = Role.Button
                                },
                        ) {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = stringResource(R.string.close),
                                tint = Color.White.copy(alpha = 0.7f),
                            )
                        }
                    }
                }

                item {
                    ElevatedCard(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = HomeDecorColors.DarkOverlay,
                        ),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = HomeDecorSpacing.Lg),
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(HomeDecorSpacing.Base),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
                        ) {
                            Surface(shape = CircleShape, color = StudioAccent.copy(alpha = 0.2f)) {
                                Icon(
                                    Icons.Rounded.Diamond,
                                    null,
                                    Modifier.padding(HomeDecorSpacing.Sm).size(22.dp),
                                    tint = DiamondTeal,
                                )
                            }
                            Column(Modifier.weight(1f)) {
                                Text(
                                    stringResource(R.string.current_balance),
                                    color = Color.White.copy(alpha = 0.6f),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                                Text(
                                    stringResource(R.string.diamonds_amount, state.diamonds),
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                }

                item {
                    DailyRewardCard(
                        state = state,
                        onClaim = onDailyRewardClaim,
                        dark = true,
                    )
                }

                item {
                    Text(
                        stringResource(R.string.get_more_credits),
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Md),
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
                                color = DiamondTeal,
                                strokeWidth = 2.dp,
                            )
                            Text(
                                stringResource(R.string.loading_packs),
                                color = Color.White.copy(alpha = 0.5f),
                            )
                        }
                    }
                }

                if (loadError != null) {
                    item {
                        ElevatedCard(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = HomeDecorColors.DarkOverlay,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = HomeDecorSpacing.Lg),
                        ) {
                            Column(
                                modifier = Modifier.padding(HomeDecorSpacing.Lg),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
                            ) {
                                Icon(
                                    Icons.Rounded.Diamond,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = DiamondTeal.copy(alpha = 0.4f),
                                )
                                Text(
                                    loadError.orEmpty(),
                                    color = Color.White.copy(alpha = 0.6f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                )
                                OutlinedButton(
                                    onClick = { loadAttempt += 1 },
                                    enabled = !storeLoading && loadingPack == null && !state.purchaseBusy,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = DiamondTeal,
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
                                color = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.padding(horizontal = HomeDecorSpacing.Lg),
                            )
                        }
                    }
                }

                if (loadError == null) {
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Lg),
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
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
    onClick: () -> Unit,
) {
    val enabled = !loading && !unavailable && !purchaseBlocked
    val displayBadge = when (pack.id) {
        "architect" -> stringResource(R.string.pack_badge_best_value)
        "designer" -> stringResource(R.string.pack_badge_popular)
        else -> null
    }
    val packTitleRes = diamondPackTitleRes(pack)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = HomeDecorColors.DarkOverlay,
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (displayBadge != null && !unavailable) DiamondTeal.copy(alpha = 0.3f)
            else Color.White.copy(alpha = 0.08f),
        ),
        modifier = Modifier
            .width(160.dp)
            .height(240.dp),
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                StudioAccent.copy(alpha = 0.12f),
                                HomeDecorColors.DarkOverlay,
                            )
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                DiamondPackVisual(
                    packIndex = packIndex,
                    modifier = Modifier.fillMaxSize(),
                )
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = DiamondTeal,
                        strokeWidth = 2.5.dp,
                    )
                }
            }

            Column(
                Modifier
                    .weight(1f)
                    .padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    stringResource(packTitleRes),
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall,
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
                        modifier = Modifier.size(14.dp),
                        tint = DiamondTeal,
                    )
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        stringResource(R.string.diamonds_amount, pack.diamonds),
                        color = DiamondTeal,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                if (displayBadge != null && !unavailable) {
                    Spacer(Modifier.height(HomeDecorSpacing.Sm))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (pack.id == "architect")
                            DiamondTeal.copy(alpha = 0.18f)
                        else
                            HomeDecorColors.PremiumGold.copy(alpha = 0.18f),
                    ) {
                        Text(
                            displayBadge,
                            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
                            color = if (pack.id == "architect") DiamondTeal else Color(0xFFD4A843),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            if (unavailable) {
                Text(
                    stringResource(R.string.pack_unavailable_google),
                    modifier = Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Md).height(HomeDecorSpacing.TouchTarget),
                    color = Color.White.copy(alpha = 0.3f),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {
                Button(
                    onClick = onClick,
                    enabled = enabled,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (pack.id == "architect") DiamondTeal else StudioAccent,
                        contentColor = Color.White,
                        disabledContainerColor = Color.White.copy(alpha = 0.08f),
                        disabledContentColor = Color.White.copy(alpha = 0.3f),
                    ),
                    modifier = Modifier
                        .padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Md)
                        .fillMaxWidth()
                        .height(HomeDecorSpacing.TouchTarget),
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Sm),
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

private val DiamondTeal = Color(0xFF4DD9E0)

@Composable
private fun DiamondPackVisual(
    packIndex: Int,
    modifier: Modifier = Modifier,
) {
    val teal = DiamondTeal
    val tealLight = Color(0xFF7AEAEA)
    val boxBrown = Color(0xFF7A5C30)
    val boxDark = Color(0xFF5C4020)
    val goldMetal = HomeDecorColors.PremiumGold

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h * 0.52f

        val glowAlpha = 0.08f + packIndex * 0.06f
        val glowRadius = w * (0.28f + packIndex * 0.06f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(teal.copy(alpha = glowAlpha), Color.Transparent),
                center = Offset(cx, cy),
                radius = glowRadius,
            ),
            radius = glowRadius,
            center = Offset(cx, cy),
        )

        when (packIndex) {
            0 -> drawSmallBox(cx, cy, boxBrown, boxDark, goldMetal, teal, tealLight)
            1 -> drawMediumChest(cx, cy, boxBrown, boxDark, goldMetal, teal, tealLight)
            2 -> drawLargeChest(cx, cy, boxBrown, boxDark, goldMetal, teal, tealLight)
            else -> drawMiningCart(cx, cy, boxBrown, boxDark, goldMetal, teal, tealLight)
        }
    }
}

private fun DrawScope.drawSmallBox(
    cx: Float, cy: Float,
    boxBrown: Color, boxDark: Color, goldMetal: Color,
    teal: Color, tealLight: Color,
) {
    drawRoundRect(boxBrown, Offset(cx - 22f, cy - 2f), Size(44f, 30f), CornerRadius(4f))
    drawRoundRect(boxDark, Offset(cx - 24f, cy - 12f), Size(48f, 12f), CornerRadius(3f))
    drawPath(diamondPath(cx - 6f, cy, 9f), teal)
    drawPath(diamondPath(cx + 9f, cy - 2f, 7f), tealLight)
}

private fun DrawScope.drawMediumChest(
    cx: Float, cy: Float,
    boxBrown: Color, boxDark: Color, goldMetal: Color,
    teal: Color, tealLight: Color,
) {
    drawRoundRect(boxBrown, Offset(cx - 30f, cy - 4f), Size(60f, 38f), CornerRadius(5f))
    val lid = Path().apply {
        moveTo(cx - 32f, cy - 4f)
        lineTo(cx - 26f, cy - 20f)
        lineTo(cx + 26f, cy - 20f)
        lineTo(cx + 32f, cy - 4f)
        close()
    }
    drawPath(lid, boxDark)
    drawRect(goldMetal, Offset(cx - 30f, cy + 8f), Size(60f, 3f))
    drawPath(diamondPath(cx - 12f, cy + 1f, 10f), teal)
    drawPath(diamondPath(cx + 3f, cy - 2f, 11f), teal)
    drawPath(diamondPath(cx + 16f, cy + 3f, 8f), tealLight)
    drawPath(diamondPath(cx + 5f, cy + 12f, 7f), teal.copy(alpha = 0.8f))
}

private fun DrawScope.drawLargeChest(
    cx: Float, cy: Float,
    boxBrown: Color, boxDark: Color, goldMetal: Color,
    teal: Color, tealLight: Color,
) {
    drawRoundRect(boxBrown, Offset(cx - 36f, cy), Size(72f, 42f), CornerRadius(5f))
    val lid = Path().apply {
        moveTo(cx - 38f, cy)
        lineTo(cx - 30f, cy - 24f)
        lineTo(cx + 30f, cy - 24f)
        lineTo(cx + 38f, cy)
        close()
    }
    drawPath(lid, boxDark)
    drawRect(goldMetal, Offset(cx - 36f, cy + 10f), Size(72f, 3f))
    drawRect(goldMetal, Offset(cx - 36f, cy + 28f), Size(72f, 3f))
    drawPath(diamondPath(cx - 16f, cy + 4f, 11f), teal)
    drawPath(diamondPath(cx, cy + 1f, 12f), teal)
    drawPath(diamondPath(cx + 16f, cy + 5f, 10f), tealLight)
    drawPath(diamondPath(cx + 6f, cy + 15f, 8f), teal)
    drawPath(diamondPath(cx - 26f, cy + 7f, 7f), teal.copy(alpha = 0.65f))
    drawPath(diamondPath(cx + 28f, cy + 9f, 6f), tealLight.copy(alpha = 0.55f))
    drawPath(diamondPath(cx - 20f, cy + 18f, 5f), teal.copy(alpha = 0.45f))
    drawPath(diamondPath(cx + 22f, cy + 20f, 5f), teal.copy(alpha = 0.45f))
}

private fun DrawScope.drawMiningCart(
    cx: Float, cy: Float,
    boxBrown: Color, boxDark: Color, goldMetal: Color,
    teal: Color, tealLight: Color,
) {
    val cart = Path().apply {
        moveTo(cx - 30f, cy - 6f)
        lineTo(cx - 36f, cy + 20f)
        lineTo(cx + 36f, cy + 20f)
        lineTo(cx + 30f, cy - 6f)
        close()
    }
    drawPath(cart, boxBrown)
    drawRect(goldMetal, Offset(cx - 36f, cy - 8f), Size(72f, 4f))
    drawCircle(boxDark, 6f, Offset(cx - 20f, cy + 28f))
    drawCircle(boxDark, 6f, Offset(cx + 20f, cy + 28f))
    drawCircle(goldMetal, 3f, Offset(cx - 20f, cy + 28f))
    drawCircle(goldMetal, 3f, Offset(cx + 20f, cy + 28f))
    drawPath(diamondPath(cx - 14f, cy - 4f, 11f), teal)
    drawPath(diamondPath(cx, cy - 10f, 13f), teal)
    drawPath(diamondPath(cx + 14f, cy - 4f, 10f), tealLight)
    drawPath(diamondPath(cx - 7f, cy - 16f, 9f), teal)
    drawPath(diamondPath(cx + 7f, cy - 16f, 9f), tealLight)
    drawPath(diamondPath(cx, cy - 24f, 8f), teal.copy(alpha = 0.85f))
    drawCircle(Color.White, 2f, Offset(cx - 20f, cy - 20f))
    drawCircle(Color.White, 1.5f, Offset(cx + 16f, cy - 26f))
    drawCircle(Color.White, 1.5f, Offset(cx + 24f, cy - 12f))
    drawCircle(Color.White.copy(alpha = 0.6f), 1f, Offset(cx - 26f, cy - 12f))
}

private fun diamondPath(cx: Float, cy: Float, size: Float): Path = Path().apply {
    moveTo(cx, cy - size)
    lineTo(cx + size * 0.65f, cy)
    lineTo(cx, cy + size * 0.5f)
    lineTo(cx - size * 0.65f, cy)
    close()
}
