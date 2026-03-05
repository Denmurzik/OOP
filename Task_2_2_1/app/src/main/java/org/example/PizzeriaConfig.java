package org.example;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Конфигурация пиццерии из JSON.
 */
public class PizzeriaConfig {

    public static class BakerConfig {
        public String name;
        public int cookingTimeMs;
    }


    public static class CourierConfig {
        public String name;
        public int trunkCapacity;
    }

    public static class OrderConfig {
        public int id;
    }

    public BakerConfig[] bakers;
    public CourierConfig[] couriers;
    public int storageCapacity;
    public int workingTimeMs;
    public OrderConfig[] orders;

    /**
     * Загрузка конфига.
     *
     * @param resourcePath путь к файлу
     * @return конфигурация
     */
    public static PizzeriaConfig load(String resourcePath) {
        InputStream stream = PizzeriaConfig.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new RuntimeException("Файл не найден: " + resourcePath);
        }
        Reader reader = new InputStreamReader(stream);
        Gson gson = new Gson();
        return gson.fromJson(reader, PizzeriaConfig.class);
    }
}
