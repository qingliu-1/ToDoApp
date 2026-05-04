package com.example.todoapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.UserRepository
import com.example.todoapp.data.UserTask
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository): ViewModel() {
    // 登录成功后的用户信息（只读，外部观察用）
    private val loginResult = MutableLiveData<UserTask?>()
    val mLoginResult: LiveData<UserTask?> = loginResult

    // 登录/注册时的提示信息（如“账号不存在”、“密码错误”等）
    private val errorMessage = MutableLiveData<String>()
    val mErrorMessage: LiveData<String> = errorMessage

    // 加载状态：true 表示正在请求中，false 表示已完成
    private val isLoading = MutableLiveData(false)
    val mIsLoading: LiveData<Boolean> = isLoading


    fun login(account: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            // 调用仓库的 login 方法，查找是否有匹配的账号密码
            val user = repository.login(account, password)
            if (user != null) {
                // 找到用户，登录成功
                loginResult.value = user
                errorMessage.value = ""
            } else {
                // 没找到，说明账号或密码错误
                loginResult.value = null
                errorMessage.value = "账号或密码错误"
            }
            isLoading.value = false
        }
    }


    fun register(account: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            // 先查一下该账号是否已被注册
            val existUser = repository.isAccountExist(account)
            Log.d("tag","$existUser")
            if (existUser) {
                errorMessage.value = "该账号已被注册"
                isLoading.value = false
                return@launch
            }
            // 没被注册，创建新用户并插入数据库
            repository.register(UserTask(account = account, password = password))
            Log.d("tag","${repository.register(UserTask(account=account, password = password))}")
            errorMessage.value = "注册成功，请登录"
            isLoading.value = false
        }
    }

//    重置错误信息

    fun clearError() {
        errorMessage.value = ""
    }
}