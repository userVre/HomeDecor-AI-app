package com.ismail.homedecorai.ui.store

import android.app.Activity
import android.content.ContextWrapper
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
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
import androidx.compose.ui.semantics.role
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.DiamondPack
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.HomeDecorUiState
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
                if (packages.isEmpty()) {
                    loadError = resources.getString(R.string.store_no_packs)
                }
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
            .background(Color.Black.copy(alpha = 0.58f))
            .clickable(
                interactionSource = scrimTapBlocker,
                indication = null,
                onClick = onClose,
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = sheetTapBlocker,
                    indication = null,
                    onClick = {},
                ),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 36.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            Modifier
                                .minimumTouchTarget()
                                .semantics {
                                    contentDescription = closeDescription
                                    role = Role.Button
                                }
                                .clickable(role = Role.Button) { onClose() },
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                Modifier
                                    .width(44.dp)
                                    .height(5.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.22f)),
                            )
                        }
                    }
                }
                item {
                    ElevatedCard(shape = RoundedCornerShape(20.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper)) {
                        Row(
                            Modifier.fillMaxWidth().padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                                Icon(Icons.Rounded.Diamond, null, Modifier.padding(12.dp).size(26.dp), tint = StudioGold)
                            }
                            Column(Modifier.weight(1f)) {
                                Text(stringResource(R.string.current_balance), color = StudioInk, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                                Text(stringResource(R.string.diamonds_amount, state.diamonds), color = HomeDecorColors.InkSoft)
                            }
                            IconButton(onClick = onClose) {
                                Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close), tint = StudioInk)
                            }
                        }
                    }
                }
                item {
                    DailyRewardCard(
                        state = state,
                        onClaim = onDailyRewardClaim,
                        dark = false,
                    )
                }
                item {
                    Text(stringResource(R.string.get_more_credits), color = StudioInk, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }
                if (storeLoading) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = StudioBlue, strokeWidth = 2.dp)
                            Text(stringResource(R.string.loading_packs), color = HomeDecorColors.InkSoft)
                        }
                    }
                }
                if (notice != null) {
                    item {
                        if (state.pendingPurchaseSync != null && loadError == null && message == null) {
                            PurchaseSyncNotice(
                                message = notice.orEmpty(),
                                pending = true,
                                busy = state.purchaseBusy,
                                onRetry = onRetrySync,
                            )
                        } else {
                            Text(notice.orEmpty(), color = HomeDecorColors.InkSoft)
                        }
                    }
                }
                if (loadError != null) {
                    item {
                        OutlinedButton(
                            onClick = { loadAttempt += 1 },
                            enabled = !storeLoading && loadingPack == null && !state.purchaseBusy,
                            shape = CircleShape,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = HomeDecorColors.InkSoft),
                        ) {
                            Icon(Icons.Rounded.Refresh, null, Modifier.size(17.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
                items(HomeDecorCatalog.diamondPacks, key = { it.id }) { pack ->
                    val productPackage = packageFor(pack)
                    val purchaseBlocked = loadingPack != null || state.purchaseBusy
                    DiamondPackRow(
                        pack = pack.copy(price = productPackage?.product?.price?.formatted ?: stringResource(R.string.unavailable)),
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

@Composable
fun DiamondPackRow(
    pack: DiamondPack,
    unavailable: Boolean = false,
    loading: Boolean = false,
    purchaseBlocked: Boolean = false,
    onClick: () -> Unit,
) {
    val packTitle = stringResource(diamondPackTitleRes(pack))
    val packBadge = diamondPackBadgeRes(pack)?.let { stringResource(it) }
    val packDescription = stringResource(diamondPackDescriptionRes(pack))
    val enabled = !loading && !unavailable && !purchaseBlocked
    val titleColor = if (unavailable) HomeDecorColors.InkSoft.copy(alpha = 0.5f) else StudioInk
    val bodyColor = if (unavailable) HomeDecorColors.InkSoft.copy(alpha = 0.5f) else HomeDecorColors.InkSoft
    ElevatedCard(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = StudioPaper,
            disabledContainerColor = StudioPaper.copy(alpha = 0.72f),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .border(1.dp, if (unavailable) HomeDecorColors.ErrorColor.copy(alpha = 0.36f) else HomeDecorColors.Line, RoundedCornerShape(16.dp)),
    ) {
        Row(
            Modifier.fillMaxSize().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Surface(shape = CircleShape, color = if (unavailable) HomeDecorColors.Mist else StudioPrimaryContainer) {
                Icon(Icons.Rounded.Diamond, null, Modifier.padding(10.dp).size(22.dp), tint = if (unavailable) bodyColor else StudioGold)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.pack_title, packTitle), color = titleColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (packBadge != null) {
                    Surface(shape = RoundedCornerShape(6.dp), color = StudioPrimaryContainer, border = androidx.compose.foundation.BorderStroke(1.dp, HomeDecorColors.Accent.copy(alpha = 0.40f))) {
                        Text(
                            packBadge,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            color = HomeDecorColors.Accent,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Text(stringResource(R.string.diamonds_amount, pack.diamonds), color = bodyColor)
                if (unavailable) {
                    Text(stringResource(R.string.pack_not_available_in_store), color = HomeDecorColors.ErrorColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                } else if (packDescription.isNotBlank()) {
                    Text(packDescription, color = bodyColor, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Button(
                onClick = onClick,
                enabled = enabled,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HomeDecorColors.Accent,
                    contentColor = Color.White,
                    disabledContainerColor = HomeDecorColors.DisabledDarkButton,
                    disabledContentColor = HomeDecorColors.DisabledDarkText,
                ),
                modifier = Modifier.widthIn(min = 106.dp).height(48.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
            ) {
                Text(
                    when {
                        loading -> stringResource(R.string.ellipsis)
                        unavailable -> stringResource(R.string.unavailable)
                        else -> pack.price
                    },
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
