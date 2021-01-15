package com.km.app.todo_mvp

interface TaskDataSource {

    interface LoadTaskCallBack {

        fun onLoadTask(tasks: List<Task>)

        fun onError(error: Throwable)
    }

    interface CallBack {

        fun onSuccess()

        fun onError(error: Throwable)
    }

    fun loadTasks(callBack: LoadTaskCallBack)

    fun insertTask(task: Task, callBack: CallBack)

    fun updateTask(task: Task, callBack: CallBack)

    fun deleteTask(task: Task, callBack: CallBack)
}