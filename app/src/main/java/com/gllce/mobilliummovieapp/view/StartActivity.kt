package com.gllce.mobilliummovieapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gllce.mobilliummovieapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}