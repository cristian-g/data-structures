package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.Post;
import models.User;

public class JsonReader {

    public static final Gson gson = new GsonBuilder().create();

    public static User[] parseUsers() throws FileNotFoundException {
        final String FILE_NAME = "datasets/users.json";
        JsonArray jsAll = gson.fromJson(new BufferedReader(new FileReader(FILE_NAME)), JsonArray.class);
        return gson.fromJson(jsAll, new TypeToken<User[]>(){}.getType());
    }

    public static Post[] parsePosts() throws FileNotFoundException {
        final String FILE_NAME = "datasets/posts.json";
        JsonArray jsAll = gson.fromJson(new BufferedReader(new FileReader(FILE_NAME)), JsonArray.class);
        return gson.fromJson(jsAll, new TypeToken<Post[]>(){}.getType());
    }
}
