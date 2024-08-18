package ru.inno.todo.testsOfZheka;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.inno.todo.service.Task;
import ru.inno.todo.service.ToDoHelperApache;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.inno.todo.testsOfZheka.DataForTests.URL;

public class ContractTests {

    private HttpClient client;
    ToDoHelperApache service = new ToDoHelperApache();

    @BeforeEach
    public void setUp() {
        client = HttpClientBuilder.create().build();
    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Получение списка задач. Проверяем статус-код 200")
    public void getListStatus200() throws IOException {

        HttpGet getListReq = new HttpGet(URL);
        HttpResponse response = client.execute(getListReq);

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Создание задачи. Проверяем статус-код 200")
    public void createTaskStatus200() throws IOException {

        HttpResponse response = createNewTask();
        String body = EntityUtils.toString(response.getEntity());

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());
        assertTrue(body.startsWith("{"));
        assertTrue(body.endsWith("}"));

    }


    @Test
    @Tag("Позитивный")
    @DisplayName("Переименование задачи. Проверяем статус-код 200")
    public void renameTaskStatus200() throws IOException {

        Task newTask = service.createNewTask("primer name");

        HttpPatch updateTask = new HttpPatch(URL + "/" + newTask.id());
        StringEntity entity = new StringEntity("{\"title\" : \"new name\"}", ContentType.APPLICATION_JSON);
        updateTask.setEntity(entity);

        HttpResponse response = client.execute(updateTask);

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());


    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Отмечаем выполненной. Проверяем статус-код 200")
    public void markDoneForTaskStatus200() throws IOException {

        Task newTask = service.createNewTask("task done");

        HttpPatch updateTask = new HttpPatch(URL + "/" + newTask.id());
        StringEntity entity = new StringEntity("{\"completed\":true}", ContentType.APPLICATION_JSON);
        updateTask.setEntity(entity);

        HttpResponse response = client.execute(updateTask);

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Удаление задачи. Проверяем статус-код 200")
    public void deleteTaskStatus200() throws IOException {

        Task newTask = service.createNewTask("task will be deleted");
        HttpDelete deleteTask = new HttpDelete(URL + "/" + newTask.id());

        HttpResponse response = client.execute(deleteTask);

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("\"todo was deleted\"", EntityUtils.toString(response.getEntity()));
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());

    }

    @Test
    @Tag("Негативный")
    @DisplayName("Создание задачи с несуществующим параметром")
    public void createTaskWithNotExistedParameter() throws IOException {

        HttpPost createTaskReq = new HttpPost(URL);

        String myContent = "{\"color\" : \"red\"}";
        StringEntity entity = new StringEntity(myContent, ContentType.APPLICATION_JSON);
        createTaskReq.setEntity(entity);

        HttpResponse response = client.execute(createTaskReq);

        String bodyAsIs = EntityUtils.toString(response.getEntity());

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(bodyAsIs.endsWith(",\"title\":null,\"completed\":null}"));
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());

    }

    @Test
    @Tag("Негативный")
    @DisplayName("Создание задачи без данных")
    public void createTaskWithEmptyData() throws IOException {

        HttpPost createTaskReq = new HttpPost(URL);
        HttpResponse response = client.execute(createTaskReq);

        String bodyAsIs = EntityUtils.toString(response.getEntity());

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(bodyAsIs.endsWith(",\"title\":null,\"completed\":null}"));
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());

    }

    @Test
    @Tag("Негативный")
    @DisplayName("Переименование задачи с несуществующим id")
    public void renameTaskIdNotExist() throws IOException {

        HttpPatch updateTask = new HttpPatch(URL + "/" + -155);
        StringEntity entity = new StringEntity("{\"title\" : \"new name\"}", ContentType.APPLICATION_JSON);
        updateTask.setEntity(entity);

        HttpResponse response = client.execute(updateTask);
        String bodyAsIs = EntityUtils.toString(response.getEntity());

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(bodyAsIs.contains(""));
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());

    }

    @Test
    @Tag("Негативный")
    @DisplayName("Удаление задачи с несуществующим id")
    public void deleteTaskIdNotExist() throws IOException {

        HttpDelete deleteTask = new HttpDelete(URL + "/" + 1200301);

        HttpResponse response = client.execute(deleteTask);

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("\"todo was deleted\"", EntityUtils.toString(response.getEntity()));
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());

    }

    private HttpResponse createNewTask() throws IOException {
        HttpPost createItemReq = new HttpPost(URL);

        String myContent = "{\"title\" : \"Новая задача\"}";
        StringEntity entity = new StringEntity(myContent, ContentType.APPLICATION_JSON);
        createItemReq.setEntity(entity);

        return client.execute(createItemReq);
    }
}
