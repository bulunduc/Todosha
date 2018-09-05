package com.bulunduc.todosha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;

public abstract class SingleFragmentActivity extends AppCompatActivity implements View.OnClickListener{
    protected abstract Fragment createFragment();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fragment);

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_task);
        floatingActionButton.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_task:
                Task task = new Task();
                TaskLab.get(this).addTask(task);
                Intent intent = new Intent(this, TaskActivity.class);
                intent.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
                startActivityForResult(intent, 0);
                break;
        }
    }
}
