package com.example.todoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userName: String
    private lateinit var userPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        登录的逻辑设计
        binding.btnLogin.setOnClickListener{
            login(binding,userName,userPassword)
        }
    }
}

private fun LoginActivity.login(binding: ActivityLoginBinding,userName: String,userPassword: String) {
    var account: String = binding.edAccountLogin.text.toString().trim()
    var password: String = binding.edPasswordLogin.text.toString().trim()
    if (TextUtils.isEmpty(account)) {
        Toast.makeText(this, "还没有注册账号！", Toast.LENGTH_SHORT).show()
        return
    }
}
