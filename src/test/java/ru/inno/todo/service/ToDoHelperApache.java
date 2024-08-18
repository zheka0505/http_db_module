package ru.inno.todo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.List;

public class ToDoHelperApache{

    private static final String URL = "https://todo-app-sky.herokuapp.com";

    private final HttpClient client;

    public ToDoHelperApache() {
        this.client = HttpClientBuilder.create().build();
    }

    public Task createNewTask(String name) throws IOException {
        System.out.println("Создаем новую задачу");
        HttpPost createItemReq = new HttpPost(URL);
        String myContent = "{\"title\" : \"" + name + "\"}";
        StringEntity entity = new StringEntity(myContent, ContentType.APPLICATION_JSON);
        createItemReq.setEntity(entity);
        HttpResponse response = client.execute(createItemReq);
        String body = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        Task task = mapper.readValue(body, Task.class);
        System.out.println("Новая задача: " + task);
        return task;
    }

    public List<Task> getTasks() throws IOException {
        HttpGet getAll = new HttpGet(URL);
        HttpResponse response = client.execute(getAll);
        String body = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(body, new TypeReference<>() {
        });
    }

    public Task getTask(int id) throws IOException {
        HttpGet getAll = new HttpGet(URL);
        HttpResponse response = client.execute(getAll);
        String body = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        List<Task> tasks = mapper.readValue(body, new TypeReference<>() {
        });

        for (Task task : tasks) {
            if (task.id() == id) {
                return task;
            }
        }
        return null;
    }

    public void deleteTask(Task t) throws IOException {
        System.out.println("Удаляем задачу с id " + t.id());
        HttpDelete delete = new HttpDelete(URL + "/" + t.id());
        client.execute(delete);
        System.out.println("Удалили задачу " + t.id());
    }

    public void setCompleted(Task task) throws IOException {
        HttpPatch update = new HttpPatch(URL + "/" + task.id());
        StringEntity entity = new StringEntity("{\"completed\":true}", ContentType.APPLICATION_JSON);
        update.setEntity(entity);

        client.execute(update);
    }

    public void renameTask(Task task, String name) throws IOException {
        HttpPatch update = new HttpPatch(URL + "/" + task.id());
        StringEntity entity = new StringEntity("{\"title\" : \"" + name + "\"}", ContentType.APPLICATION_JSON);
        update.setEntity(entity);

        client.execute(update);
    }

    public void deleteAllTasks() throws IOException {

        HttpGet getAll = new HttpGet(URL);
        HttpResponse response = client.execute(getAll);
        String body = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();
        List<Task> tasks = mapper.readValue(body, new TypeReference<>() {
        });
        for (Task task : tasks) {
            deleteTask(task);
            }

    }
}
