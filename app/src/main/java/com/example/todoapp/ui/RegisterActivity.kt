package com.example.todoapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.R
import com.example.todoapp.data.AppDatabase
import com.example.todoapp.data.UserRepository
import com.example.todoapp.databinding.ActivityLoginBinding
import com.example.todoapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 初始化 UserViewModel
        val userDao = AppDatabase.DatabaseClient.getDatabase(this).userDao()
        val userRepo = UserRepository(userDao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepo))
            .get(UserViewModel::class.java)

        // 观察注册结果
        userViewModel.mErrorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                // 如果提示“注册成功，请登录”，可以按需关闭注册页
                if (message == "注册成功，请登录") {
                    finish()
                }
            }
        }

        // 注册按钮点击
        binding.btnRegisterAgree.setOnClickListener {
            val account = binding.edAccountRegister.text.toString().trim()
            val password = binding.edPasswordRegister.text.toString().trim()
            val confirmPassword = binding.edPasswordAgree.text.toString().trim()

            if (account.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            userViewModel.register(account, password)
            finish()
        }
    }
}