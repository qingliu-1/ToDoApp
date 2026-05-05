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
import com.example.todoapp.data.LoginManager
import com.example.todoapp.data.UserRepository
import com.example.todoapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loginManager: LoginManager

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
        val userRepository = UserRepository(userDao)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository))
            .get(UserViewModel::class.java)

        loginManager = LoginManager(this)

        binding.rbRecord.isChecked = loginManager.isRemember()
        binding.rbAutomatic.isChecked = loginManager.isAutoLogin()

        if (loginManager.isRemember()) {
            binding.edAccountLogin.setText(loginManager.getAccount())
            binding.edPasswordLogin.setText(loginManager.getPassword())
        }

        if (loginManager.isAutoLogin()) {
            val account = loginManager.getAccount()
            val password = loginManager.getPassword()
            if (!account.isNullOrEmpty() && !password.isNullOrEmpty()) {
                userViewModel.login(account, password)
            }
        }

        // 观察登录结果：登录成功跳转 MainActivity
        userViewModel.mLoginResult.observe(this) { user ->
            if (user != null) {
                val account = binding.edAccountLogin.text.toString().trim()
                val password = binding.edPasswordLogin.text.toString().trim()
                loginManager.loginInfo(account,password,binding.rbRecord.isChecked,binding.rbAutomatic.isChecked)
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
