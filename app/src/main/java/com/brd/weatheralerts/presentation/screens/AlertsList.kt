package com.brd.weatheralerts.presentation.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.brd.weatheralerts.R
import com.brd.weatheralerts.data.ImageRequestProvider
import com.brd.weatheralerts.presentation.ListItemsUiState
import com.brd.weatheralerts.presentation.theme.WeatherAlertsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsList(
    uiState: ListItemsUiState,
    tapHandler: (String) -> Unit,
    onErrorDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState,
    itemsLazyListState: LazyListState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) { innerPadding ->
        if (uiState.isLoading) {
            FullScreenLoading()
        } else {
            LazyColumn(
                state = itemsLazyListState,
                modifier = Modifier.padding(innerPadding)
            ) {
                items(uiState.items) { item ->
                    AlertRowContent(item, tapHandler)
                }
            }
        }
        if (uiState.errorMessage != null) {
            val error = remember(uiState) { uiState.errorMessage }
            val errorMessageText: String = stringResource(error.messageId)
            val onErrorDismissState by rememberUpdatedState(onErrorDismiss)

            LaunchedEffect(errorMessageText, snackbarHostState) {
                snackbarHostState.showSnackbar(message = errorMessageText)
                onErrorDismissState()
            }
        }
    }
}

@Composable
private fun AlertRowContent(item: ListAlertUiState, tapHandler: (String) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(PaddingValues(start = 5.dp, end = 5.dp, top = 5.dp))
            .clickable { tapHandler.invoke(item.id) }
    ) {
        val (image, name, source, startDate, endDate, divider) = createRefs()
        createHorizontalChain(image, name)
        createVerticalChain(name, source, startDate, endDate, divider)

        AsyncImage(
            model = ImageRequestProvider.withCacheKey(LocalContext.current, item.id),
            placeholder = debugPlaceholder(R.drawable.placeholder_1_1),
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent {
                    val colors = listOf(
                        Color.Black,
                        Color.Transparent
                    )
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(colors),
                        blendMode = BlendMode.DstIn
                    )
                }
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(divider.top)
                    start.linkTo(parent.start)
                    end.linkTo(name.start)
                }
                .fillMaxWidth(0.2f)
        )
        Text(
            text = stringResource(R.string.event_name, item.name),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(name) {
                    top.linkTo(image.top)
                    end.linkTo(parent.end)
                    start.linkTo(image.end)
                    bottom.linkTo(source.top)
                }
                .fillMaxWidth(0.8f)
        )
        Text(
            text = stringResource(R.string.event_source, item.source),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(source) {
                    top.linkTo(name.bottom)
                    end.linkTo(name.end)
                    start.linkTo(name.start)
                    bottom.linkTo(startDate.top)
                }
                .fillMaxWidth(0.8f)
        )
        Text(
            text = stringResource(R.string.event_onset, item.startTime),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(startDate) {
                    top.linkTo(source.bottom)
                    end.linkTo(name.end)
                    start.linkTo(name.start)
                    bottom.linkTo(endDate.top)
                }
                .fillMaxWidth(0.8f)
        )
        Text(
            text = stringResource(R.string.event_expires, item.endTime),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(endDate) {
                    top.linkTo(startDate.bottom)
                    end.linkTo(name.end)
                    start.linkTo(name.start)
                    bottom.linkTo(divider.top)
                }
                .fillMaxWidth(0.8f)
        )
        Divider(
            modifier = Modifier
                .constrainAs(divider) {
                    top.linkTo(endDate.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 5.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Row")
@Composable
fun PreviewAlertRowContent() {
    WeatherAlertsTheme {
        Surface {
            AlertRowContent(
                ListAlertUiState(
                    id = "x",
                    thumbnailUrl = "",
                    name = "Long nameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
                    source = "Source",
                    startTime = "10:40 12/12/2023",
                    endTime = "11:40"
                )
            ) {

            }
        }
    }

}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun debugPlaceholder(@DrawableRes debugPreview: Int) =
    if (LocalInspectionMode.current) {
        painterResource(id = debugPreview)
    } else {
        null
    }

data class ListAlertUiState(
    val id: String,
    val thumbnailUrl: String = "https://picsum.photos/1000",
    val name: String,
    val source: String,
    val startTime: String,
    val endTime: String
)