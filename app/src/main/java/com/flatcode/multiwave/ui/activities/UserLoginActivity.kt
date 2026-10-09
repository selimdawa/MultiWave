package com.flatcode.multiwave.ui.activities

import android.content.Intent
import android.os.Bundle
import com.flatcode.multiwave.databinding.ActivityUserLoginBinding

class UserLoginActivity : BaseActivity() {

    private lateinit var binding: ActivityUserLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}