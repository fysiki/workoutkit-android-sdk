package com.fysiki.workoutkitsdkdemo

import WorkoutPreview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.fysiki.workoutkitsdkdemo.type.WorkoutFormat


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WorkoutTile(item: WorkoutPreview, onClick: (String) -> Unit) {

    Card(modifier = Modifier.fillMaxWidth().height(150.dp),
        onClick = {
            onClick.invoke(item.id)
        }) {
        Box {
            GlideImage(
                model = item.picture,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        item.name,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(5f, 5f),
                                blurRadius = 5f
                            )
                        )
                    )
                    if (item.format == WorkoutFormat.PLAY) {
                        Image(
                            painter = painterResource(id = R.drawable.play_icon_black),
                            contentDescription = null, // Provide a proper content description for accessibility
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            }
        }
    }
}