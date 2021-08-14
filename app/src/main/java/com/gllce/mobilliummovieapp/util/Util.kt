package com.gllce.mobilliummovieapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gllce.mobilliummovieapp.R


const val BASE_URL = "https://api.themoviedb.org/3/movie/"
private const val IMAGE_DOWNLOAD_URL_w500 = "https://image.tmdb.org/t/p/w500/"
private const val IMAGE_DOWNLOAD_URL_original = "https://image.tmdb.org/t/p/original/"

const val QUERY_PAGE_SIZE = 20

fun ImageView.downloadPath(
    path: String?,
    progressDrawable: CircularProgressDrawable,
    isOriginal: Boolean
) {
    val options = RequestOptions.placeholderOf(progressDrawable)
        .error(R.drawable.ic_launcher_foreground)

    var url = IMAGE_DOWNLOAD_URL_w500
    if (isOriginal) {
        url = IMAGE_DOWNLOAD_URL_original
    }

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url + path)
        .into(this)
}

fun placeholderProgressBar(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

@BindingAdapter("android:downloadSmallImage")
fun downloadSmallImage(view: ImageView, path: String?) {
    view.downloadPath(path, placeholderProgressBar(view.context), false)
}

@BindingAdapter("android:downloadOriginalImage")
fun downloadOriginalImage(view: ImageView, path: String?) {
    view.downloadPath(path, placeholderProgressBar(view.context), true)
}

fun isOnline(context: Context): Boolean {
    val conManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val internetInfo = conManager.activeNetworkInfo
    return internetInfo != null && internetInfo.isConnected
}