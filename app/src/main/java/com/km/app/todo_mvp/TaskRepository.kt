package com.km.app.todo_mvp

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class TaskRepository(private val taskDao: TaskDao): TaskDataSource {

    val disposables = CompositeDisposable()

    override fun loadTasks(callBack: TaskDataSource.LoadTaskCallBack) {
        taskDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    callBack.onLoadTask(it)
                },
                onError = {
                    callBack.onError(it)
                }
            ).addTo(disposables)
    }

    override fun insertTask(task: Task, callBack: TaskDataSource.CallBack) {
        taskDao.insert(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    callBack.onSuccess()
                },
                onError = {
                    callBack.onError(it)
                }
            ).addTo(disposables)
    }

    override fun updateTask(task: Task, callBack: TaskDataSource.CallBack) {
        taskDao.update(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    callBack.onSuccess()
                },
                onError = {
                    callBack.onError(it)
                }
            ).addTo(disposables)
    }

    override fun deleteTask(task: Task, callBack: TaskDataSource.CallBack) {
        taskDao.delete(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    callBack.onSuccess()
                },
                onError = {
                    callBack.onError(it)
                }
            ).addTo(disposables)
    }
}