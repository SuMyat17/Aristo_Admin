package com.aristo.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aristo.admin.R
import com.aristo.admin.databinding.ActivityHelpCenterBinding

class HelpCenterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHelpCenterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpCenterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.ibBack.setOnClickListener {
            finish()
        }
    }
}