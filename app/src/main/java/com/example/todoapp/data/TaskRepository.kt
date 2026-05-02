package com.example.todoapp.data

import kotlinx.coroutines.flow.Flow

//任务仓库
//对 ViewModel 屏蔽底层数据来源（现在是 Room，以后可以是网络）
//所有数据库操作都通过 TaskDao 完成
class TaskRepository(private val taskDao: TaskDao) {

    // 将 Dao 的 Flow 原样暴露，供 ViewModel 转换为 LiveData
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    // 以下三个方法都是挂起函数，需要在协程中调用
    suspend fun insert(task: Task) = taskDao.insert(task)

    suspend fun update(task: Task) = taskDao.update(task)

    suspend fun delete(task: Task) = taskDao.delete(task)
}