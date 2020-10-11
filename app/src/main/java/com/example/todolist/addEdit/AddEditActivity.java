package com.example.todolist.addEdit;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;
import com.example.todolist.room.TodoItem;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditActivity extends AppCompatActivity {
    private TextInputLayout til_title, til_sDate, til_dDate, til_memo;
    private ImageButton ib_sDate, ib_dDate;
    private final int START_DATE = 0;
    private final int DUE_DATE = 1;
    private int mode;
    private int id;
    private TodoItem selectItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        //mode 가죠오기

        mode = getIntent();

        ActionBar actionbar = getSupportActionBar();

        if (ActionBar != null) {
            if (mode == 0) {
                actionbar.setTitle("Add Todo");
            } else if (mode == 1) {
                actionbar.setTitle("Edit Todo");
            }
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        til_title = findViewById(R.id.addEdit_til_title);
        til_sDate = findViewById(R.id.addEdit_til_start);
        til_dDate = findViewById(R.id.addEdit_til_due);
        til_memo = findViewById(R.id.addEdit_til_memo);

        ib_sDate = findViewById(R.id.addEdit_ibtm_start);
        ib_dDate = findViewById(R.id.addEdit_ibtm_due);

        if (mode == 1) {
            //TODO
        }

        til_title.getEditText().setText(selectItem.getTitle());
        til_sDate.getEditText().setText(selectItem.getStart());
        til_dDate.getEditText().setText(selectItem.getDue());
        til_memo.getEditText().setText(selectItem.getMemo());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}