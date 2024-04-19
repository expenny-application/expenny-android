package org.expenny.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.common.models.DrawableResource

@Composable
fun DrawableResource.asRawPainter() = painterResource(id)