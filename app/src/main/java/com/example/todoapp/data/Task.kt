package com.example.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks") //这个类对应数据库里的“tasks”表
data class Task(
    @PrimaryKey(autoGenerate = true) // 主键，自动生成唯一ID
    val id: Long = 0,
    val tile: String = "",           // 任务标题
    val isDone: Boolean = false,     // 是否完成
    val isPinned: Boolean = false    // 是否置顶
)