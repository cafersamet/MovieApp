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

fun ImageView.downloadPath(path: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions.placeholderOf(progressDrawable)
        .error(R.drawable.ic_launcher_foreground)

    Glide
        .with(context)
        .setDefaultRequestOptions(options)
        .load(IMAGE_DOWNLOAD_URL_w500 + path)
        .into(this)
}

fun placeholderProgressBar(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

@BindingAdapter("android:downloadPath")
fun downloadPath(view: ImageView, path: String?) {
    view.downloadPath(path, placeholderProgressBar(view.context))
}

fun isOnline(context: Context): Boolean {
    val conManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val internetInfo = conManager.activeNetworkInfo
    return internetInfo != null && internetInfo.isConnected
}