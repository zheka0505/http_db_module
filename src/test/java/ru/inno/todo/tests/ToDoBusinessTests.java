package ru.inno.todo.tests;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.todo.service.Task;
import ru.inno.todo.service.ToDoHelper;
import ru.inno.todo.service.ToDoHelperOkHttp;

import java.io.IOException;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

public class ToDoBusinessTests {
    ToDoHelper service;

    @BeforeEach
    public void setService() {
        service = new ToDoHelperOkHttp();
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

    @Test
    public void instancioTest1() {
        Task task = Instancio.create(Task.class);

        Task myTask = Instancio.of(Task.class)
                .generate(field("title"), gen -> gen.finance().creditCard().masterCard())
                .generate(field("id"), gen -> gen.ints().max(10))
                .set(field("completed"), true)
                .create();

        List<Task> list = Instancio.ofList(Task.class).size(7).create();
        assertNotNull(task);
        System.out.println(task);
    }
}
