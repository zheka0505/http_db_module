package ru.inno.todo.interceptor;

import kotlin.Pair;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();
        System.out.println(">>> " + request.method() + " " + request.url());
        for (Pair<? extends String, ? extends String> header : request.headers()) {
            System.out.println(header.getFirst() + " : " + header.getSecond());
        }

        Response response = chain.proceed(request);
        System.out.println("<<< " + response.code() + " " + response.message());
        for (Pair<? extends String, ? extends String> header : response.headers()) {
            System.out.println(header.getFirst() + " : " + header.getSecond());
        }

        return response;
    }
}
