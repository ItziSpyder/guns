package io.github.itzispyder.guns.data;

import com.google.gson.Gson;

public interface PersistentDataSerializable {

    Gson gson = new Gson();

    default String serialize() {
        try {
            return gson.toJson(this);
        }
        catch (Exception ex) {
            return "{}";
        }
    }
}
