package com.example.todoapp.data

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserTask): Long

    @Update
    suspend fun updateUser(user: UserTask)

    @Delete
    suspend fun deleteUser(user: UserTask)

    @Query("SELECT * FROM user_table WHERE account = :account AND password = :password")
    suspend fun login(account: String, password: String): UserTask?   // 登录验证

    @Query("SELECT * FROM user_table WHERE account = :account")
    suspend fun getUserByAccount(account: String): UserTask?          // 查重（注册时用）
}