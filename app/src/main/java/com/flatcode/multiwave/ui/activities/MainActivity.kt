package com.flatcode.multiwave.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.multiwave.databinding.ActivityMainBinding
import com.flatcode.multiwave.ui.util.applyEdgeToEdge
import com.flatcode.multiwave.ui.util.applySystemBarsPadding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()
    }
}