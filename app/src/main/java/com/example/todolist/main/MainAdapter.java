package com.example.todolist.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.addEdit.AddEditActivity;
import com.example.todolist.room.MyDatabase;
import com.example.todolist.room.TodoItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private List<TodoItem> itemList = new ArrayList<>();

    public void submitAll(List<TodoItem> list) {
        itemList.clear();
        itemList.addAll(list);
        Collections.sort(itemList);
        notifyDataSetChanged();
    }
    public void removeAll() {
        itemList.clear();
        notifyDataSetChanged();
    }
    public void removeAllItem(List<TodoItem> newList) {
        itemList.clear();
        itemList = newList;
        notifyDataSetChanged();
    }
    public boolean checkItem(TodoItem item){
        return item.isChecked();
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final MainViewHolder viewHolder = new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo,
                parent, false));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoItem temp = itemList.get(viewHolder.getAdapterPosition());
                temp.setChecked(!temp.isChecked());
                MyDatabase myDatabase = MyDatabase.getInstance(parent.getContext());
                myDatabase.todoDao().editTodo(temp);
                notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final TodoItem temp = itemList.get(viewHolder.getAdapterPosition());
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(temp.getTitle());
                final String[] items = {"수정, 삭제, 취소"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (items[which]) {
                            case "수정":
                                Intent intent = new Intent(parent.getContext(), AddEditActivity.class);
                                intent.putExtra("mode", 1);
                                intent.putExtra("item_id", temp.getId());
                                break;
                            case "삭제":
                                itemList.remove(temp);
                                MyDatabase myDatabase = MyDatabase.getInstance(parent.getContext());
                                myDatabase.todoDao().deleteTodo(temp);
                                notifyItemRemoved(viewHolder.getAdapterPosition());
                                break;
                            case "취소":
                                break;
                        }
                    }
                });
                return false;
            }
        });

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        try {
            holder.onBind(itemList.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
