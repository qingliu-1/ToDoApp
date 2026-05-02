package com.example.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // 查询所有任务，按置顶优先、新建靠前排序
    // 返回 Flow 意味着数据库数据有变化时会自动发送新数据
    @Query("SELECT * FROM tasks ORDER BY isPinned DESC,id DESC")
    fun getAllTasks(): Flow<List<Task>>

    // 插入一条任务，如果主键冲突则用新数据覆盖
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    // 更新一条任务
    @Update
    suspend fun update(task: Task)

    // 删除一条任务
    @Delete
    suspend fun delete(task: Task)
}