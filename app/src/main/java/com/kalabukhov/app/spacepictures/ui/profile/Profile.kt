package com.kalabukhov.app.spacepictures.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kalabukhov.app.spacepictures.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {

    private lateinit var binbing: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binbing = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binbing.root)

    }

    companion object {
        fun profileScreen(context: Context): Intent {
            return Intent(context, Profile::class.java)
        }
    }
}