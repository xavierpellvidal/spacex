package com.spacex.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri

// Check if internet connection
fun haveConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        else -> return false
    }
}

// Open youtube app or browser
fun openYoutubeVideo(id: String, context: Context) {
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://www.youtube.com/watch?v=$id")
    )
    try {
        context.startActivity(appIntent)
    } catch (ex: ActivityNotFoundException) {
        context.startActivity(webIntent)
    }
}

// Open browser
fun openWebPage(page: String, context: Context) {
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(page)
    )
    try {
        context.startActivity(webIntent)
    } catch (ex: ActivityNotFoundException) {
    }
}

