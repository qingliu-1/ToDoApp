package com.example.todoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // 将数据层的 Flow 转成 LiveData
    // LiveData 会被 Activity 观察，数据一变 UI 就自动更新
    val tasks: LiveData<List<Task>> = repository.allTasks.asLiveData()

    //    添加任务
    fun addTask(title: String) {
        viewModelScope.launch {
            repository.insert(Task(title = title))
        }
    }

    // 删除任务
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    // 切换置顶/取消置顶
    fun togglePin(task: Task) {
        viewModelScope.launch {
            // copy 是 data class 自带的方法，返回一个新的 Task
            // 把 isPinned 取反，其他字段不变，然后更新到数据库
            repository.update(task.copy(isPinned = !task.isPinned))
        }
    }

    // 切换完成/未完成
    fun markDone(task: Task) {
        viewModelScope.launch {
            repository.update(task.copy(isDone = !task.isDone))
        }
    }
}