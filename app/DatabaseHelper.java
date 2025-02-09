package com.example.myapplication;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private DatabaseHelper databaseHelper;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        taskList = databaseHelper.getAllTasks();
        taskAdapter = new TaskAdapter(this, taskList, this);
        recyclerView.setAdapter(taskAdapter);

        findViewById(R.id.fab).setOnClickListener(v -> showTaskDialog(null));
    }

    @Override
    public void onTaskClick(Task task) {
        showTaskDialog(task);
    }

    private void showTaskDialog(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);
        builder.setView(dialogView);

        EditText taskTitle = dialogView.findViewById(R.id.taskTitle);
        EditText taskDescription = dialogView.findViewById(R.id.taskDescription);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        if (task != null) {
            taskTitle.setText(task.getTitle());
            taskDescription.setText(task.getDescription());
            saveButton.setText("Update");
        }

        AlertDialog alertDialog = builder.create();

        saveButton.setOnClickListener(v -> {
            String title = taskTitle.getText().toString();
            String description = taskDescription.getText().toString();
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (task == null) {
                Task newTask = new Task(0, title, description);
                databaseHelper.addTask(newTask);
                taskList.add(newTask);
            } else {
                task.setTitle(title);
                task.setDescription(description);
                databaseHelper.updateTask(task);
                int index = taskList.indexOf(task);
                taskList.set(index, task);
            }
            taskAdapter.notifyDataSetChanged();
            alertDialog.dismiss();
        });

        alertDialog.show();
    }
}
