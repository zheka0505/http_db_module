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

public class ToDoHelperApache implements ToDoHelper {

    private static final String URL = "https://todo-app-sky.herokuapp.com";

    private final HttpClient client;

    public ToDoHelperApache() {
        this.client = HttpClientBuilder.create().build();
    }

    public Task createNewTask() throws IOException {
        HttpPost createItemReq = new HttpPost(URL);
        String myContent = "{\"title\" : \"test\"}";
        StringEntity entity = new StringEntity(myContent, ContentType.APPLICATION_JSON);
        createItemReq.setEntity(entity);
        HttpResponse response = client.execute(createItemReq);
        String body = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(body, Task.class);
    }

    public List<Task> getTasks() throws IOException {
        HttpGet getAll = new HttpGet(URL); // [ {}, {}, {} ]
        HttpResponse response = client.execute(getAll);
        String body = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(body, new TypeReference<>() {
        });
    }

    public void deleteTask(Task t) throws IOException {
        HttpDelete delete = new HttpDelete(URL + "/" + t.id());
        client.execute(delete);
    }

    @Override
    public void setCompleted(Task task) throws IOException {
        HttpPatch update = new HttpPatch(URL + "/" + task.id());
        StringEntity entity = new StringEntity("{\"completed\":true}", ContentType.APPLICATION_JSON);
        update.setEntity(entity);

        client.execute(update);
    }
}
