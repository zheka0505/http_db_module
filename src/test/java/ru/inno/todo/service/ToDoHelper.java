package ru.inno.todo.service;

import java.io.IOException;
import java.util.List;

public interface ToDoHelper {
     Task createNewTask() throws IOException ;

     List<Task> getTasks() throws IOException;

    public void deleteTask(Task t) throws IOException ;

    void setCompleted(Task task) throws IOException;

}
