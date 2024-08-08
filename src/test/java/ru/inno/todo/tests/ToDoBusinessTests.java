package ru.inno.todo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.todo.service.Task;
import ru.inno.todo.service.ToDoHelper;
import ru.inno.todo.service.ToDoHelperApache;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoBusinessTests {
    ToDoHelper service;

    @BeforeEach
    public void setService() {
        service = new ToDoHelperApache();
    }

    @Test
    @DisplayName("Я могу удалить задачу")
    public void iCanDeleteMyTask() throws IOException {
        Task task = service.createNewTask();
        service.deleteTask(task);
        List<Task> tasks = service.getTasks();

        assertFalse(tasks.contains(task));
    }

    @Test
    @DisplayName("Я могу отметить задачу выполненной")
    public void iCanSetMyTaskCompleted() throws IOException {
        Task myTask = service.createNewTask();
        service.setCompleted(myTask);

        List<Task> tasks = service.getTasks();

        Task taskToAssert = null;
        for (Task task : tasks) {
            if (task.id() == myTask.id()) {
                taskToAssert = task;
            }
        }

        assertNotNull(taskToAssert);
        assertTrue(taskToAssert.completed());
    }
}
