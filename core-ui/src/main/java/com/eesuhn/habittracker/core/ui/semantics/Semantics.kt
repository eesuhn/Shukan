package com.eesuhn.habittracker.core.ui.semantics

import android.text.format.DateUtils
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import com.eesuhn.habittracker.core.model.Action
import com.eesuhn.habittracker.core.ui.R

fun Modifier.habitActionSemantics(action: Action) = composed {
    val state = stringResource(
        if (action.toggled) R.string.action_state_toggled else R.string.action_state_not_toggled
    )
    val timestampLabel = if (action.timestamp != null) {
        DateUtils.getRelativeDateTimeString(
            LocalContext.current,
            action.timestamp!!.toEpochMilli(),
            DateUtils.DAY_IN_MILLIS,
            DateUtils.WEEK_IN_MILLIS,
            0
        ).toString()
    } else stringResource(R.string.action_text_no_date)

    semantics(mergeDescendants = true) {
        stateDescription = state
        text = AnnotatedString.Builder(timestampLabel).toAnnotatedString()
    }
}