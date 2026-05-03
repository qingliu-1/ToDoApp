package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.TaskRepository

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
//     create 方法会在 ViewModelProvider 获取 ViewModel 时被调用
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
    // 判断是不是我们要创建的 TaskViewModel
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")// 类型转换是安全的，忽略警告
            return TaskViewModel(repository) as T
        }
    throw IllegalArgumentException("错误的ViewModel类${modelClass.name}")
    }
}