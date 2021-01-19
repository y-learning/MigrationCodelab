/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.plantdetail

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel

@Composable
fun PlantName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    )
}

@Composable
fun PlantWatering(wateringInterval: Int) {
    Column(Modifier.fillMaxWidth()) {
        val normalPadding = dimensionResource(R.dimen.margin_normal)
        val centerWithPaddingModifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .align(Alignment.CenterHorizontally)

        Text(
            text = stringResource(id = R.string.watering_needs_prefix),
            modifier = centerWithPaddingModifier.padding(top = normalPadding),
            style = TextStyle(
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Bold
            )
        )

        val str = AmbientContext.current.resources.getQuantityString(
            R.plurals.watering_needs_suffix, wateringInterval, wateringInterval
        )
        Text(
            text = str,
            modifier = centerWithPaddingModifier.padding(bottom = normalPadding)
        )
    }
}


@Composable
private fun textView(): TextView = AmbientContext.current.let { context ->
    remember {
        TextView(context).apply {
            movementMethod = LinkMovementMethod.getInstance()
        }
    }
}

@Composable
private fun PlantDescription(description: String) {
    val textView = textView()

    val htmlDescription = remember(description) {
        HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
    AndroidView({ textView }) {
        it.text = htmlDescription
    }
}

@Composable
fun PlantDetailContent(plant: Plant) {
    Surface {
        val paddingDp = dimensionResource(id = R.dimen.margin_normal)
        Column(modifier = Modifier.padding(paddingDp)) {
            PlantName(plant.name)
            PlantWatering(plant.wateringInterval)
            PlantDescription(plant.description)
        }
    }
}

@Composable
fun PlantDetailDescription(plantDetailViewModel: PlantDetailViewModel) {
    val plant by plantDetailViewModel.plant.observeAsState()

    plant?.let { PlantDetailContent(it) }
}

@Composable
@Preview
fun PlantNamePreview() {
    MaterialTheme {
        PlantName("Apple")
    }
}

@Composable
@Preview
fun PlantWateringPreview() {
    MaterialTheme {
        PlantWatering(12)
    }
}

@Preview
@Composable
private fun PlantDescriptionPreview() {
    MaterialTheme {
        PlantDescription("HTML<br><br>description")
    }
}

@Preview
@Composable
private fun PlantDetailContentPreview() {
    val plant = Plant("id", "Apple", "description", 3, 30, "")
    MaterialTheme {
        PlantDetailContent(plant)
    }
}
