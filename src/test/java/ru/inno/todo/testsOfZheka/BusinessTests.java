package ru.inno.todo.testsOfZheka;

import org.junit.jupiter.api.*;
import ru.inno.todo.service.Task;
import ru.inno.todo.service.ToDoHelperApache;


import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.inno.todo.testsOfZheka.DataForTests.*;

public class BusinessTests {

    ToDoHelperApache service;

    public BusinessTests() {
    }

    @BeforeEach
    public void setService() {
        service = new ToDoHelperApache();
    }

    //зависнет если в списке больше 2х задач
    @Test
    @Tag("Позитивный")
    @DisplayName("Получение пустого списка задач")
    public void getEmptyListOfTasks() throws IOException {

        service.deleteAllTasks();

        List<Task> emptyListOfTasks = service.getTasks();

        assertEquals(0, emptyListOfTasks.size());

    }


    //зависнет если в списке больше 2х задач
    @Test
    @Tag("Позитивный")
    @DisplayName("Получение списка мной созданных задач")
    public void getMyListOfTasks() throws IOException {

        service.deleteAllTasks();

        service.createNewTask(RUSSIANNAME);
        service.createNewTask(LATINNAME);
        service.createNewTask(EMPTY);

        List<Task> myTasks = service.getTasks();

        assertEquals(RUSSIANNAME, myTasks.get(0).title());
        assertEquals(LATINNAME, myTasks.get(1).title());
        assertEquals(EMPTY, myTasks.get(2).title());

    }

    //зависнет если в списке больше 2х задач
    @Test
    @Tag("Позитивный")
    @DisplayName("Получение длиного списка задач")
    public void getLongListOfTasks() throws IOException {

        service.deleteAllTasks();

        for (int i = 1; i < 11; i++) {
            service.createNewTask("задача №" + i);
        }

        List<Task> listOfTasks = service.getTasks();

        assertEquals(10, listOfTasks.size());

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Создание задачи название русскими буквами")
    public void createRussianNameOfTask() throws IOException {
        Task myTask = service.createNewTask(RUSSIANNAME);

        assertEquals(RUSSIANNAME, myTask.title());
        assertFalse(myTask.completed());

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Создание задачи название латинскими буквами")
    public void createLatinNameOfTask() throws IOException {
        Task myTask = service.createNewTask(LATINNAME);

        assertEquals(LATINNAME, myTask.title());
        assertFalse(myTask.completed());

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Создание задачи длинное название")
    public void createLongNameOfTask() throws IOException {
        Task myTask = service.createNewTask(LONGNAME);

        assertEquals(LONGNAME, myTask.title());
        assertFalse(myTask.completed());

    }

    @Test
    @Tag("Негативный")
    @DisplayName("Создание дубля задачи")
    public void createDuplicateTask() throws IOException {
        service.createNewTask(RUSSIANNAME);
        Task myTask = service.createNewTask(RUSSIANNAME);

        assertEquals(RUSSIANNAME, myTask.title());
        assertFalse(myTask.completed());

    }

    @Test
    @Tag("Негативный")
    @DisplayName("Создание задачи с пустым названием")
    public void createEmptyNameOfTask() throws IOException {
        Task myTask = service.createNewTask(EMPTY);

        assertEquals(EMPTY, myTask.title());
        assertFalse(myTask.completed());

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Переименование задачи в наименование на латинском")
    public void renameMyTaskOnLatin() throws IOException {
        Task myTask = service.createNewTask(RUSSIANNAME);

        service.renameTask(myTask, LATINNAME);

        Task taskToAssert = service.getTask(myTask.id());

        assertEquals(LATINNAME, taskToAssert.title());
        assertFalse(taskToAssert.completed());

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Переименование задачи на длинное наименование")
    public void renameMyTaskWithLongName() throws IOException {

        Task myTask = service.createNewTask(LATINNAME);

        service.renameTask(myTask, LONGNAME);

        Task taskToAssert = service.getTask(myTask.id());

        assertEquals(LONGNAME, taskToAssert.title());
        assertFalse(taskToAssert.completed());

    }

    @Test
    @Tag("Негативный")
    @DisplayName("Переименование задачи на пустое")
    public void renameMyTaskOnEmptyName() throws IOException {
        Task myTask = service.createNewTask(RUSSIANNAME);

        service.renameTask(myTask, EMPTY);

        Task taskToAssert = service.getTask(myTask.id());

        assertEquals(EMPTY, taskToAssert.title());
        assertFalse(taskToAssert.completed());

    }


    @Test
    @Tag("Негативный")
    @DisplayName("Переименование задачи на такое же как у другой задачи")
    public void renameMyTaskOnDuplicateName() throws IOException {
        service.createNewTask(RUSSIANNAME);

        Task myTask = service.createNewTask(LATINNAME);

        service.renameTask(myTask, RUSSIANNAME);

        Task taskToAssert = service.getTask(myTask.id());

        assertEquals(RUSSIANNAME, taskToAssert.title());
        assertFalse(taskToAssert.completed());

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Поставить метку выполнена для невыполненной задачи")
    public void setCompleteToIncompletedTask() throws IOException {
        Task myTask = service.createNewTask(RUSSIANNAME);
        service.setCompleted(myTask);

        Task taskToAssert = service.getTask(myTask.id());

        assertTrue(taskToAssert.completed());
    }

    //зависает
    @Test
    @Tag("Негативный")
    @DisplayName("Поставить метку выполнена для выполненной задачи")
    public void setCompleteToCompletedTask() throws IOException {
        Task myTask = service.createNewTask(RUSSIANNAME);

        service.setCompleted(myTask);
        service.setCompleted(myTask);

        Task taskToAssert = service.getTask(myTask.id());

        assertTrue(taskToAssert.completed());
    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Удаление задачи")
    public void deleteTask() throws IOException {
        Task myTask = service.createNewTask("Задача для удаления");
        service.deleteTask(myTask);
        List<Task> tasks = service.getTasks();

        assertFalse(tasks.contains(myTask));
    }

    @Test
    @Tag("Негативный")
    @DisplayName("Удаление несуществующей задачи")
    public void deleteNotExistedTask() throws IOException {
        Task myTask = new Task(-1, "несуществующая", true);
        service.deleteTask(myTask);
        List<Task> tasks = service.getTasks();

        assertFalse(tasks.contains(myTask));
    }

    //зависнет если в списке больше 2х задач
    @Test
    @Tag("Позитивный")
    @DisplayName("Удаление списка задач")
    public void deleteListOfTasks() throws IOException {

        service.deleteAllTasks();

        List<Task> emptyListOfTasks = service.getTasks();

        assertEquals(0, emptyListOfTasks.size());
    }

}
