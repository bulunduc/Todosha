package com.bulunduc.todosha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.UUID;

public class TaskActivity extends AppCompatActivity {
    private ArrayList<Task> tasks;
    private Task task;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_fragment);

        tasks = TaskLab.get(this).getTasks();
        UUID id = (UUID)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
        task = TaskLab.get(this).getTask(id);

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator (R.drawable.round_clear_white_24);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (NavUtils.getParentActivityName(TaskActivity.this) != null) {
                    if (task.isTaskNew()) tasks.remove(task);
                    NavUtils.navigateUpFromSameTask(TaskActivity.this);
                }
            }
        });
        if (task.isTaskNew())
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + getString(R.string.new_task) + "</font>"));
        else
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + getString(R.string.update_task) + "</font>"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = TaskFragment.newInstance(id);
            fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!task.isTaskNew()) getMenuInflater().inflate(R.menu.fragment_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                TaskLab.get(this).deleteTask(task);
                Intent intent = new Intent(this, TaskListActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
