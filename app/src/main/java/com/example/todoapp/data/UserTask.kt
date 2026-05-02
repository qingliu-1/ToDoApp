package com.example.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserTask(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val account: String,   //账号
    val password: String,  //密码
)