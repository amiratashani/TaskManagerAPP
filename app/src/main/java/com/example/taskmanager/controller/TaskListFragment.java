package com.example.taskmanager.controller;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Repository;
import com.example.taskmanager.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */


public class TaskListFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String ARG_LIST_TASK = "ListTask";
    private static final String TASK_EDIT_FRAGMENT_TAG = "taskEdit";

    private ImageView mImageViewTask;
    private TextView mTextViewTask;

    private RecyclerView mRecyclerView;
    private TaskAdapter mTaskAdapter;



    public static TaskListFragment newInstance(Task.State state) {

        Bundle args = new Bundle();
        args.putString(ARG_LIST_TASK, state.toString());
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;

    }

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        mRecyclerView = view.findViewById(R.id.recycle_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mImageViewTask=view.findViewById(R.id.image_view_task);
        mTextViewTask=view.findViewById(R.id.text_view_task);


        updateUi();

        return view;
    }

    public void updateUi() {
        assert getArguments() != null;
        List<Task> tasks = Repository.getInstance(getContext()).getTasksSeparateState(getArguments().getString(ARG_LIST_TASK));

        if (tasks.size()==0) {
            mImageViewTask.setVisibility(View.VISIBLE);
            mTextViewTask.setVisibility(View.VISIBLE);
        }


        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(tasks);
            mRecyclerView.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem menuItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onQueryTextChange(String newText) {

        String userInput = newText.toLowerCase();
        List<Task> tasks = Repository.getInstance(getActivity()).getTasksSeparateState(getArguments().getString(ARG_LIST_TASK));
        List<Task> newTasks = new ArrayList<>();

            tasks.stream()
                    .filter(task -> task.getTitle().toLowerCase().startsWith(userInput))
                    .forEach(newTasks::add);

        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(newTasks);
            mRecyclerView.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.setTasks(newTasks);
            mTaskAdapter.notifyDataSetChanged();
        }

        if (userInput.equals("")) {
            mTaskAdapter.setTasks(tasks);
            mTaskAdapter.notifyDataSetChanged();
        }

        return true;
    }

    private class TaskHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTaskTitle, mTextViewTaskDate, mIcon;
        private Task mTask;

        private TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTaskTitle = itemView.findViewById(R.id.text_view_task_title);
            mTextViewTaskDate = itemView.findViewById(R.id.text_view_task_date);
            mIcon = itemView.findViewById(R.id.tvIcon);

            itemView.setOnClickListener(v -> {
                TaskEditFragment taskEditFragment = TaskEditFragment.newInstance(mTask.getId());
                assert getFragmentManager() != null;
                taskEditFragment.show(getFragmentManager(), TASK_EDIT_FRAGMENT_TAG);
            });

        }

        private void bindTask(Task task) {
            mTextViewTaskTitle.setText(task.getTitle());
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            mTextViewTaskDate.setText(formatter.format(task.getDate()));

            mIcon.setText(task.getTitle().substring(0,1).toUpperCase());
            Random mRandom = new Random();
            int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
            ((GradientDrawable) mIcon.getBackground()).setColor(color);

            mTask = task;
        }
    }


    public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> mTasks;

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        private TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.task_item_holder, parent, false);
            return new TaskHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {

            holder.bindTask(mTasks.get(position));

        }

        @Override
        public int getItemCount() {

            return mTasks.size();

        }
    }

}
