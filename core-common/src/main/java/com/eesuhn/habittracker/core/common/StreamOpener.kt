package com.eesuhn.habittracker.core.common

import android.app.Application
import java.io.InputStream
import java.io.OutputStream
import java.net.URI
import javax.inject.Inject

/**
 * Abstraction over Android's Uri and ContentResolver in order to use them in ViewModels
 * without using Android classes directly
 */
interface StreamOpener {
    fun openInputStream(uri: URI): InputStream
    fun openOutputStream(uri: URI): OutputStream
}

class AndroidStreamOpener @Inject constructor(private val app: Application) : StreamOpener {

    override fun openInputStream(uri: URI): InputStream {
        return app.contentResolver.openInputStream(uri.toAndroidURI())!!
    }

    override fun openOutputStream(uri: URI): OutputStream {
        return app.contentResolver.openOutputStream(uri.toAndroidURI())!!
    }

    private fun URI.toAndroidURI() = android.net.Uri.parse(toString())
}