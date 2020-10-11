package com.example.todolist.addEdit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;
import com.example.todolist.room.MyDatabase;
import com.example.todolist.room.TodoItem;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

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

        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
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
            //TODO: edit

            //id 가져오기
            if (id == -1) {
                Log.d("todo_id", "item id wrong");
                Toast.makeText(AddEditActivity.this, "item id wrong", Toast.LENGTH_SHORT).show();
            } else {
                MyDatabase myDatabase = MyDatabase.getInstance(AddEditActivity.this);
                try {
                    selectItem = myDatabase.todoDao().getTodo(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                til_title.getEditText().setText(selectItem.getTitle());
                til_sDate.getEditText().setText(selectItem.getStart());
                til_dDate.getEditText().setText(selectItem.getDue());
                til_memo.getEditText().setText(selectItem.getMemo());
            }
        }

        ib_sDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar(START_DATE);
            }
        });

        ib_dDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar(DUE_DATE);
            }
        });
    }

    public void showCalendar(final int mode) {
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDate = cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(AddEditActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String s_month = new Integer(month).toString();
                String s_date = new Integer(dayOfMonth).toString();
                if (month + 1 < 10) {
                    s_month = "0" + new Integer(month + 1).toString();
                }
                if (dayOfMonth < 10) {
                    s_date = "0" + new Integer(dayOfMonth).toString();
                }

                String date = year + "/" + s_month + "/" + s_date;
                if (mode == START_DATE) {
                    til_sDate.getEditText().setText(date);
                } else if (mode == DUE_DATE) {
                    til_dDate.getEditText().setText(date);
                }
            }
        }, mYear, mMonth, mDate).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                break;
            case R.id.save_todo:
                String title = til_title.getEditText().getText().toString();
                String sDate = til_sDate.getEditText().getText().toString();
                String dDate = til_dDate.getEditText().getText().toString();
                String memo = til_memo.getEditText().getText().toString();

                if (title.equals("")) {
                    til_title.setError("필수요소입니다!");
                } else if (sDate.equals("")) {
                    til_sDate.setError("필수요소입니다!");
                } else til_sDate.setError(null);
                if (dDate.equals("")) {
                    til_dDate.setError("필수요소입니다!");
                } else til_dDate.setError(null);
                if (title.equals("") && !sDate.equals("") && !dDate.equals("")) {
                    if (sDate.compareTo(dDate) > 0) {
                        til_sDate.setError("시작 날짜가 더 느려!");
                        til_dDate.setError("끝나는 날짜가 더 빨라요!");
                    } else {
                        MyDatabase myDatabase = MyDatabase.getInstance(AddEditActivity.this);

                        if (mode == 0) {
                            //add
                            TodoItem todoItem = new TodoItem(title, sDate, dDate, memo);
                            myDatabase.todoDao().insertTodo(todoItem);
                        } else if (mode == 1) {
                            selectItem.setTitle(title);
                            selectItem.setStart(sDate);
                            selectItem.setDue(dDate);
                            selectItem.setMemo(memo);

                            myDatabase.todoDao().editTodo(selectItem);
                            Toast.makeText(AddEditActivity.this, "저장성공!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
                return super.onOptionsItemSelected(item);
        }
    }
}