package com.km.app.todo_mvp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat


class MainFragment : Fragment(), TaskContract.View {

    private lateinit var adapter: TasksAdapter
    private lateinit var presenter: TaskPresenter

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = context?: return

        val listView = view.findViewById<ListView>(R.id.listView)
        adapter = TasksAdapter(itemListener)
        listView.adapter = adapter

        val db = TaskDatabase.getInstance(context)
        presenter = TaskPresenter(TaskRepository(db.taskDao()), this)
        presenter.loadTasks()

        val fab: View = view.findViewById(R.id.addTaskButton)
        fab.setOnClickListener {
            presenter.insertTask("New Task")
        }
    }

    override fun onLoadTasks(tasks: List<Task>) {
        adapter.tasks = tasks
    }

    override fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private class TasksAdapter(private val listener: TaskItemListener): BaseAdapter() {

        var tasks: List<Task> = listOf()
            set(tasks) {
                field = tasks
                notifyDataSetChanged()
            }

        private val stateTexts = listOf(R.string.todo, R.string.doing, R.string.done)
        private val stateColors = listOf(R.color.todo, R.color.doing, R.color.done)

        override fun getCount() = tasks.size

        override fun getItem(i: Int) = tasks[i]

        override fun getItemId(i: Int) = i.toLong()

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
            val task = getItem(i)
            val rowView = view ?: LayoutInflater.from(viewGroup?.context).inflate(R.layout.task_item, viewGroup, false)
            rowView.findViewById<TextView>(R.id.taskState).apply {
                text = context.getString(stateTexts[task.state])
                setTextColor(ContextCompat.getColor(context, stateColors[task.state]))
                setOnClickListener {
                    listener.onStateClick(task)
                }
            }

            rowView.findViewById<TextView>(R.id.taskDescription).apply {
                text = task.description
                setOnClickListener {
                    listener.onDescriptionClick(task)
                }
            }

            rowView.findViewById<ImageView>(R.id.taskDeleteButton).setOnClickListener {
                listener.onDeleteClick(task)
            }

            return rowView
        }
    }

    interface TaskItemListener {
        fun onStateClick(task: Task)

        fun onDescriptionClick(task: Task)

        fun onDeleteClick(task: Task)
    }

    val itemListener = object : TaskItemListener {
        override fun onStateClick(task: Task) {
            presenter.updateTaskState(task)
        }

        override fun onDescriptionClick(task: Task) {
            presenter.updateTaskDescription(task, "Update Task")
        }

        override fun onDeleteClick(task: Task) {
            presenter.deleteTask(task)
        }
    }

}