package com.p8.common.net.interceptor;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * author : Yannis.Ywx
 * createTime : 2017/9/18  15:51
 * description : 日志拦截器
 */
public final class LoggerInterceptor implements Interceptor {
    private static final int JSON_INDENT = 2;
    private static final String LOG_LEVEL = "LogLevel";

    private final HttpLoggingInterceptor.Logger logger;
    private volatile HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.NONE;

    public LoggerInterceptor() {
        this(HttpLoggingInterceptor.Logger.DEFAULT);
    }

    public LoggerInterceptor(HttpLoggingInterceptor.Logger logger) {
        this.logger = logger;
    }

    /**
     * Change the level at which this interceptor logs.
     */
    public void setLevel(HttpLoggingInterceptor.Level level) {
        if (level == null) {
            throw new NullPointerException("level == null. Use Level.NONE instead.");
        }
        this.level = level;
    }

    public HttpLoggingInterceptor.Level getLevel() {
        return level;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpLoggingInterceptor.Level level = findLevel(chain.request());
        if (level == HttpLoggingInterceptor.Level.NONE) {
            return chain.proceed(chain.request());
        }

        final StringBuilder builder = new StringBuilder();
        HttpLoggingInterceptor httpInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                append(builder, message);
            }
        });
        //可以单独为某个请求设置日志的级别，避免全局设置的局限性
        httpInterceptor.setLevel(level);
        Response response = httpInterceptor.intercept(chain);
        append(builder,response.request().headers().toString());
        logger.log(builder.toString());
        return response;
    }

    @NonNull
    private HttpLoggingInterceptor.Level findLevel(Request request) {
        //可以单独为某个请求设置日志的级别，避免全局设置的局限性
        String logLevel = request.header(LOG_LEVEL);
        if (logLevel != null) {
            if (logLevel.equalsIgnoreCase("NONE")) {
                return HttpLoggingInterceptor.Level.NONE;
            } else if (logLevel.equalsIgnoreCase("BASIC")) {
                return HttpLoggingInterceptor.Level.BASIC;
            } else if (logLevel.equalsIgnoreCase("HEADERS")) {
                return HttpLoggingInterceptor.Level.HEADERS;
            } else if (logLevel.equalsIgnoreCase("BODY")) {
                return HttpLoggingInterceptor.Level.BODY;
            }
        }
        return level;
    }

    private static void append(StringBuilder builder, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        try {
            // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
            if (message.startsWith("{") && message.endsWith("}")) {
                JSONObject jsonObject = new JSONObject(message);
                message = jsonObject.toString(JSON_INDENT);
            } else if (message.startsWith("[") && message.endsWith("]")) {
                JSONArray jsonArray = new JSONArray(message);
                message = jsonArray.toString(JSON_INDENT);
            }
        } catch (JSONException ignored) {
        }
        builder.append(message).append('\n');
    }
}

