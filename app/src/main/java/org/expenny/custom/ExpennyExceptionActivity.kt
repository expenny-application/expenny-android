package org.expenny.custom

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyFlatButton
import org.expenny.core.ui.theme.ExpennyTheme
import org.expenny.main.MainActivity

class ExpennyExceptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ExpennyTheme {
                ExceptionContent(
                    onRestartClick = {
                        finishAffinity()
                        startActivity(Intent(this@ExpennyExceptionActivity, MainActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
private fun ExceptionContent(
    onRestartClick: () -> Unit,
) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(R.drawable.ic_bug),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                    Text(
                        text = "Unexpected Error Occurred",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Sorry for inconvenience. Please report this error to us.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    ExpennyFlatButton(
                        modifier = Modifier.width(200.dp),
                        onClick = {},
                        label = {
                            Text(text = "Report")
                        }
                    )
                    Text(
                        modifier = Modifier.clickable { onRestartClick() },
                        text = "Sorry for inconvenience. Please report this error to us.",
                        style = MaterialTheme.typography.labelLarge,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}
@Composable
@Preview
private fun ExceptionContentPreview() {
    ExpennyTheme {
        ExceptionContent(
            onRestartClick = {}
        )
    }
}