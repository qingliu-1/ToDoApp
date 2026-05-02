package com.example.todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Room 数据库的入口类
// @Database 注解声明了数据库包含哪些实体（表）以及版本号
// entities 列表：所有需要建表的 @Entity 类都要写在这里（Task 和 User）
// version：数据库版本号，修改表结构时必须升级版本
@Database(entities = [Task::class, UserTask::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // 声明两个抽象方法，Room 会自动生成实现，返回对应的 Dao 实例
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao

    object DatabaseClient {
        @Volatile  // 确保 instance 的修改对所有线程立即可见
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,  // 使用 Application Context 避免内存泄漏
                    AppDatabase::class.java,
                    "my_database"               // 数据库文件名，会生成 my_database.db
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}