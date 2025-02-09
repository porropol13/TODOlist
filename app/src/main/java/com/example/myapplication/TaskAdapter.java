package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private List<Task> taskList;
    private OnTaskClickListener onTaskClickListener;
    private OnTaskLongClickListener onTaskLongClickListener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public interface OnTaskLongClickListener {
        void onTaskLongClick(Task task);
    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskClickListener onTaskClickListener, OnTaskLongClickListener onTaskLongClickListener) {
        this.context = context;
        this.taskList = taskList;
        this.onTaskClickListener = onTaskClickListener;
        this.onTaskLongClickListener = onTaskLongClickListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());
        holder.itemView.setOnClickListener(v -> onTaskClickListener.onTaskClick(task));
        holder.itemView.setOnLongClickListener(v -> {
            onTaskLongClickListener.onTaskLongClick(task);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
