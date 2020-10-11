package com.example.todolist.room;

public abstract class MyDatabase {
    abstract public TodoDao todoDao();

    private static MyDatabase myDatabase;

    public static MyDatabase getInstance(Context context) {
        if(myDatabase == null) {
            myDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    MyDatabase.class, "myDatabase.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return  myDatabase;
    }
}
