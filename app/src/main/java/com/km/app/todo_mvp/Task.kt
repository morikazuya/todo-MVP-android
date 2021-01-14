package com.km.app.todo_mvp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Int, //インデックス

    @ColumnInfo(name = "state")
    val state: Int, //状態

    @ColumnInfo(name = "description")
    var description: String //タスク内容
        )