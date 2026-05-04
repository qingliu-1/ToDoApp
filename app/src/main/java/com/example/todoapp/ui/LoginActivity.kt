package com.example.todoapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.AppDatabase
import com.example.todoapp.data.UserRepository
import com.example.todoapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

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
        // 初始化 UserViewModel
        val userDao = AppDatabase.DatabaseClient.getDatabase(this).userDao()
        val userRepo = UserRepository(userDao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepo))
            .get(UserViewModel::class.java)

        // 观察登录结果：登录成功跳转 MainActivity
        userViewModel.mLoginResult.observe(this) { user ->
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()  // 关掉登录页，避免按返回键回到这里
            }
        }

        // 观察错误信息：吐司提示
        userViewModel.mErrorMessage.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        // 观察加载状态：可以控制按钮点击等（这里简单演示，暂时不处理）
        userViewModel.mIsLoading.observe(this) { isLoading ->
            // 例如：binding.btnLogin.isEnabled = !isLoading
        }

        // 登录按钮点击
        binding.btnLogin.setOnClickListener {
            val account = binding.edAccountLogin.text.toString().trim()
            val password = binding.edPasswordLogin.text.toString().trim()

            if (account.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            userViewModel.login(account, password)
        }

        // 跳转注册按钮
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
