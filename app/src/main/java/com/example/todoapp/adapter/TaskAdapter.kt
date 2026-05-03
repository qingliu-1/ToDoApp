package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ItemTaskLayoutBinding

class TaskAdapter(private val onDeleteClick:(Task)-> Unit,
                  private val onPinClick:(Task)-> Unit,
                  private val onDoneClick:(Task)-> Unit
                 ) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val binding = ItemTaskLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }


    inner class TaskViewHolder(private val binding: ItemTaskLayoutBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.cbDone.isChecked = task.isDone

            binding.btnDelete.setOnClickListener {
                onDeleteClick(task)
            }

            binding.btnPin.setOnClickListener {
                onPinClick(task)
            }

            binding.cbDone.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked != task.isDone) {
                    onDoneClick(task)
                }
            }

            if (task.isPinned) {
                binding.root.setBackgroundColor(0xFFFFF9C4.toInt())  // 淡黄色
            } else {
                binding.root.setBackgroundColor(0xFFFFFFFF.toInt())  // 白色
            }
        }
    }
}

class TaskDiffCallback: DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(
        oldItem: Task,
        newItem: Task
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Task,
        newItem: Task
    ): Boolean {
        return oldItem == newItem
    }

}