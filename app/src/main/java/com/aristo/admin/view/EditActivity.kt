package com.aristo.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aristo.admin.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibBack.setOnClickListener {
            finish()
        }
    }
}