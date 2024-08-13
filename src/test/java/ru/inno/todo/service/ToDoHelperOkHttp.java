package ru.inno.todo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import ru.inno.todo.interceptor.LoggingInterceptor;

import java.io.IOException;
import java.util.List;

public class ToDoHelperOkHttp implements ToDoHelper {
    private static final String URL = "https://todo-app-sky.herokuapp.com";

    private final OkHttpClient client;

    public ToDoHelperOkHttp() {
        Interceptor interceptor = new LoggingInterceptor();

        client = new OkHttpClient.Builder().addNetworkInterceptor(interceptor).build();
    }

    @Override
    public Task createNewTask() throws IOException {
        RequestBody body = RequestBody.create("{\"title\" : \"test\"}", MyMediaTypes.JSON);
        Request request = new Request.Builder().url(URL).post(body).build();
        Response response = client.newCall(request).execute();
        return new ObjectMapper().readValue(response.body().string(), Task.class);
    }

    @Override
    public List<Task> getTasks() throws IOException {
        Request getAll = new Request.Builder().get().url(URL).build();
        Response response = this.client.newCall(getAll).execute();
        String body = response.body().string();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body, new TypeReference<>() {
        });
    }

    @Override
    public void deleteTask(Task t) throws IOException {
        Request delete = new Request.Builder().delete().url(URL + "/" + t.id()).build();
        client.newCall(delete).execute();
    }

    @Override
    public void setCompleted(Task task) throws IOException {
        RequestBody body = RequestBody.create("{\"completed\":true}", MyMediaTypes.JSON);

        Request setCompleted = new Request.Builder()
                .url(URL + "/" + task.id())
                .patch(body)
                .build();

        client.newCall(setCompleted).execute();
    }
}
