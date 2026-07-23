package com.flatcode.multiwave.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.multiwave.databinding.ActivityUserLoginBinding
import com.flatcode.multiwave.ui.util.applyEdgeToEdge
import com.flatcode.multiwave.ui.util.applyNavigationBarPadding

class UserLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEdgeToEdge()
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.applyNavigationBarPadding()

        binding.login.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}