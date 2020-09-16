package com.zhuinden.simplestackdemoexamplefragments.features.tasks

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.simplestackdemoexamplefragments.R
import com.zhuinden.simplestackdemoexamplefragments.application.Injector
import com.zhuinden.simplestackdemoexamplefragments.core.mvp.MvpPresenter
import com.zhuinden.simplestackdemoexamplefragments.core.navigation.BaseFragment
import com.zhuinden.simplestackdemoexamplefragments.core.navigation.FragmentKey
import com.zhuinden.simplestackdemoexamplefragments.data.models.Task
import com.zhuinden.simplestackdemoexamplefragments.util.*
import kotlinx.android.synthetic.main.path_tasks.*
import java.util.*

/**
 * Created by Zhuinden on 2018. 08. 20.
 */
// UNSCOPED!
class TasksFragment : BaseFragment(), MessageQueue.Receiver {
    companion object {
        const val CONTROLLER_TAG = "TasksView.Presenter"
    }

    interface Presenter: MvpPresenter<TasksFragment> {
        fun onTaskCheckClicked(task: Task)

        fun onTaskRowClicked(task: Task)

        fun onNoTasksAddButtonClicked()

        fun onFilterActiveSelected()

        fun onFilterCompletedSelected()

        fun onFilterAllSelected()

        fun onClearCompletedClicked()

        fun onRefreshClicked()
    }

    private val myResources = Injector.get().resources()
    private val messageQueue = Injector.get().messageQueue()

    lateinit var tasksAdapter: TasksAdapter

    var taskItemListener: TasksAdapter.TaskItemListener = object : TasksAdapter.TaskItemListener {
        override fun onTaskRowClicked(task: Task) {
            presenter.onTaskRowClicked(task)
        }

        override fun onTaskCheckClicked(task: Task) {
            presenter.onTaskCheckClicked(task)
        }
    }

    class SavedSuccessfullyMessage

    val presenter: Presenter by lazy {
        lookup<Presenter>(CONTROLLER_TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        super.onCreateView(inflater, container, savedInstanceState).also {
            tasksAdapter = TasksAdapter(ArrayList(0), taskItemListener)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)

        recyclerTasks.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerTasks.adapter = tasksAdapter

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            ContextCompat.getColor(requireContext(), R.color.colorAccent),
            ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(recyclerTasks)
        swipeRefreshLayout.setOnRefreshListener { presenter.onRefreshClicked() }

        buttonNoTasksAdd.onClick {
            addTaskButtonClicked()
        }

        messageQueue.requestMessages(getKey<FragmentKey>(), this)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden) {
            messageQueue.requestMessages(getKey<FragmentKey>(), this)
        }
    }

    override fun onDestroyView() {
        presenter.detachView(this)

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(null)
        }
        super.onDestroyView()
    }

    override fun receiveMessage(message: Any) {
        if (message is SavedSuccessfullyMessage) {
            showSuccessfullySavedMessage()
        }
    }

    fun addTaskButtonClicked() {
        presenter.onNoTasksAddButtonClicked()
    }

    fun calculateDiff(tasks: List<Task>): Pair<DiffUtil.DiffResult, List<Task>> =
        Pair(DiffUtil.calculateDiff(TasksDiffCallback(tasksAdapter.data, tasks)), tasks)

    fun hideEmptyViews() {
        containerTasks.show()
        viewNoTasks.hide()
    }

    fun showTasks(pairOfDiffResultAndTasks: Pair<DiffUtil.DiffResult, List<Task>>, filterType: TasksFilterType) {
        val (diffResult, tasks) = pairOfDiffResultAndTasks

        tasksAdapter.data = tasks
        diffResult.dispatchUpdatesTo(tasksAdapter)
        if (tasks.isEmpty()) {
            filterType.showEmptyViews(this)
        } else {
            hideEmptyViews()
        }
    }

    fun showFilteringPopupMenu() {
        val popup = PopupMenu(requireContext(), requireContext().findActivity<Activity>().findViewById(R.id.menu_filter))
        popup.menuInflater.inflate(R.menu.filter_tasks, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.active -> presenter.onFilterActiveSelected()
                R.id.completed -> presenter.onFilterCompletedSelected()
                else -> presenter.onFilterAllSelected()
            }
            //loadTasks(false); // reactive data source ftw
            true
        }

        popup.show()
    }

    fun setRefreshing(isRefreshing: Boolean) {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }

    fun showNoActiveTasks() {
        showNoTasksViews(myResources.getString(R.string.no_tasks_active), R.drawable.ic_check_circle_24dp, false)
    }

    fun showNoTasks() {
        showNoTasksViews(myResources.getString(R.string.no_tasks_all), R.drawable.ic_assignment_turned_in_24dp, false)
    }

    fun showNoCompletedTasks() {
        showNoTasksViews(myResources.getString(R.string.no_tasks_completed), R.drawable.ic_verified_user_24dp, false)
    }

    fun showTaskMarkedComplete() {
        showMessage(myResources.getString(R.string.task_marked_complete))
    }

    fun showTaskMarkedActive() {
        showMessage(myResources.getString(R.string.task_marked_active))
    }

    fun showCompletedTasksCleared() {
        showMessage(myResources.getString(R.string.completed_tasks_cleared))
    }

    fun showLoadingTasksError() {
        showMessage(myResources.getString(R.string.loading_tasks_error))
    }

    fun showSuccessfullySavedMessage() {
        showMessage(myResources.getString(R.string.successfully_saved_task_message))
    }

    private fun showMessage(message: String) {
        val view = view
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showNoTasksViews(mainText: String, iconRes: Int, showAddView: Boolean) {
        containerTasks.hide()
        viewNoTasks.show()

        textNoTasks.text = mainText
        imageNoTasksIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), iconRes))
        buttonNoTasksAdd.showIf { showAddView }
    }

    fun setFilterLabelText(filterText: Int) {
        textFilteringLabel.setText(filterText)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_filter -> {
                showFilteringPopupMenu()
                return true
            }
            R.id.menu_clear -> {
                presenter.onClearCompletedClicked()
                return true
            }
            R.id.menu_refresh -> {
                presenter.onRefreshClicked()
                return true
            }
        }
        return false
    }
}
