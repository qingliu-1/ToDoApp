package com.example.todoapp.data

class UserRepository(private val userDao: UserDao) {

    suspend fun login(account: String, password: String): UserTask? {
        return userDao.login(account, password)
    }

    suspend fun register(user: UserTask): Long {
        return userDao.insertUser(user)
    }

    suspend fun isAccountExist(account: String): Boolean {
        return userDao.getUserByAccount(account) != null
    }
}