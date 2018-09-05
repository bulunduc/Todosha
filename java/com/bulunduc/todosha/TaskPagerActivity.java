package com.bulunduc.todosha;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.UUID;


public class TaskPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        tasks = TaskLab.get(this).getTasks();

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Task task = tasks.get(position);
                return TaskFragment.newInstance(task.getId());
            }

            @Override
            public int getCount() {
                return tasks.size();
            }
        });
        UUID id = (UUID)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(id)){
                viewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
