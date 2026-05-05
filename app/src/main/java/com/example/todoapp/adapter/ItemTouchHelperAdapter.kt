package com.example.todoapp.adapter

interface ItemTouchHelperAdapter {
    // 拖拽时交换数据
    fun onItemMove(fromPosition: Int, toPosition: Int)
    // 侧滑时删除数据
    fun onItemDismiss(position: Int)
}