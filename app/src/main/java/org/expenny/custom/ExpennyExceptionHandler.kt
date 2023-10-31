package org.expenny.custom

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Thread.UncaughtExceptionHandler

class ExpennyExceptionHandler private constructor(
    private val context: Context,
    private val defaultHandler: UncaughtExceptionHandler,
    private val activityToBeLaunched: Class<*>
) : UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            launchActivity(context, activityToBeLaunched, e)
        } catch (e: Exception) {
            defaultHandler.uncaughtException(t, e)
        }
    }

    private fun launchActivity(
        context: Context,
        activity: Class<*>,
        exception: Throwable
    ) {
        val intent = Intent(context, activity).apply {
            putExtra(INTENT_DATA_NAME, Gson().toJson(exception))
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        println(StringWriter().also { exception.printStackTrace(PrintWriter(it)) }.toString().trim())

        context.startActivity(intent)
    }

    companion object {
        private const val INTENT_DATA_NAME = "stacktrace"

        fun init(
            context: Context,
            activityToBeLaunched: Class<*>
        ) {
            val handler = ExpennyExceptionHandler(
                context,
                Thread.getDefaultUncaughtExceptionHandler() as UncaughtExceptionHandler,
                activityToBeLaunched
            )
            Thread.setDefaultUncaughtExceptionHandler(handler)
        }

        fun getThrowableFromIntent(intent: Intent): Throwable? {
            return try {
                Gson().fromJson(intent.getStringExtra(INTENT_DATA_NAME), Throwable::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}