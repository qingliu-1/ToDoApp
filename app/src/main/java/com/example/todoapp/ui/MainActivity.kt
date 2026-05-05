package com.example.todoapp.ui

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.adapter.SimpleItemTouchHelperCallback
import com.example.todoapp.adapter.TaskAdapter
import com.example.todoapp.data.AppDatabase
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskRepository
import com.example.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = TaskAdapter(
            onDeleteClick = { task -> showDeleteConfirm(task) },
            onPinClick = { task -> viewModel.togglePin(task) },
            onDoneClick = { task -> viewModel.markDone(task) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val callback = SimpleItemTouchHelperCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        // 初始化数据库、仓库、ViewModel
        val dao = AppDatabase.DatabaseClient.getDatabase(this).taskDao()
        val repository = TaskRepository(dao)
        viewModel = ViewModelProvider(this, TaskViewModelFactory(repository))
            .get(TaskViewModel::class.java)

        // 观察 LiveData：当数据库数据变化时，自动把新的 List 交给 Adapter
        viewModel.tasks.observe(this) { taskList ->
            adapter.submitList(taskList)  // submitList 会利用 DiffUtil 高效刷新
        }

        // 添加按钮的点击事件
        binding.add.setOnClickListener {
            showAddTaskDialog()
        }
    }

//      弹出对话框，输入任务标题并添加
    private fun showAddTaskDialog() {
        val input = EditText(this)  // 创建一个输入框
        AlertDialog.Builder(this)
            .setTitle("新建任务")
            .setView(input)  // 把输入框嵌入对话框
            .setPositiveButton("确定") { _, _ ->
                val title = input.text.toString().trim()  // 去除首尾空格
                if (title.isNotEmpty()) {         // 空标题不算数
                    viewModel.addTask(title)      // 通知 ViewModel 添加
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }


//      删除前的确认弹窗
//      task要删除的任务
    private fun showDeleteConfirm(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("删除任务")
            .setMessage("确定要删除「${task.title}」吗？")
            .setPositiveButton("删除") { _, _ ->
                viewModel.deleteTask(task)       // 确认删除
            }
            .setNegativeButton("取消"){_, _ ->
                adapter.notifyItemChanged(adapter.currentList.indexOf(task))
            }
            .show()
    }
}