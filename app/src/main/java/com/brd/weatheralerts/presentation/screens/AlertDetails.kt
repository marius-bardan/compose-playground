package com.brd.weatheralerts.presentation.screens

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.brd.weatheralerts.R
import com.brd.weatheralerts.data.ImageRequestProvider
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDetails(state: AlertDetailsUiState) {
    var imageOffsetX by remember { mutableStateOf(0f) }
    var imageOffsetY by remember { mutableStateOf(0f) }

    var isDescriptionExpanded by remember { mutableStateOf(false) }
    var areInstructionsExpanded by remember { mutableStateOf(false) }
    var areZonesExpanded by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {

            AsyncImage(
                model = ImageRequestProvider.withCacheKey(LocalContext.current, state.id),
                placeholder = debugPlaceholder(R.drawable.placeholder_1_1),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .offset { IntOffset(imageOffsetX.roundToInt(), imageOffsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                imageOffsetX += dragAmount.x
                                imageOffsetY += dragAmount.y
                            },
                            onDragEnd = {
                                imageOffsetX = 0f
                                imageOffsetY = 0f
                            })
                    }
            )

            Text(
                text = stringResource(id = R.string.event_name, state.name),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = stringResource(id = R.string.event_source, state.source),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = stringResource(id = R.string.event_onset, state.startTime ?: "-"),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = stringResource(id = R.string.event_expires, state.endTime ?: "-"),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = stringResource(id = R.string.event_severity, state.severity),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = stringResource(id = R.string.event_certainty, state.certainty),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = stringResource(id = R.string.event_urgency, state.urgency),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = stringResource(id = R.string.event_description, state.description ?: "-"),
                maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .clickable { isDescriptionExpanded = !isDescriptionExpanded }
            )
            Text(
                text = stringResource(id = R.string.event_instructions, state.instructions ?: "-"),
                maxLines = if (areInstructionsExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .clickable { areInstructionsExpanded = !areInstructionsExpanded }
            )

            Text(
                text = stringResource(
                    id = R.string.event_zones,
                    state.affectedZones.joinToString()
                ),
                maxLines = if (areZonesExpanded) Int.MAX_VALUE else 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .clickable { areZonesExpanded = !areZonesExpanded }
            )
        }
    }
}

@Preview
@Composable
fun AlertDetailsContent() {
    AlertDetails(
        state = AlertDetailsUiState(
            id = "x",
            thumbnailUrl = "https://picsum.photos/1000",
            name = "Nameeeeeeeeeeeeeeeeeeee",
            source = "Source",
            startTime = "12/12/2023",
            endTime = "13/12/2023",
            severity = "Very severe",
            certainty = "ver certain",
            urgency = "Very urgent",
            description = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.",
            instructions = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
            affectedZones = listOf()
        )
    )
}

data class AlertDetailsUiState(
    val id: String,
    val thumbnailUrl: String = "https://picsum.photos/1000",
    val name: String,
    val source: String,
    val startTime: String?,
    val endTime: String?,
    val severity: String,
    val certainty: String,
    val urgency: String,
    val description: String?,
    val instructions: String?,
    val affectedZones: List<Pair<String, Boolean?>>
)