package org.example.config;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.example.exception.ConfigLoadException;

/**
 * Конфигурация пиццерии из JSON.
 */
public class PizzeriaConfig {

    /**
     * Конфиг пекаря.
     */
    public static class BakerConfig {
        public String name;
        public int cookingTimeMs;
    }

    /**
     * Конфиг курьера.
     */
    public static class CourierConfig {
        public String name;
        public int trunkCapacity;
    }

    /**
     * Конфиг заказа.
     */
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
            throw new ConfigLoadException("Файл не найден: " + resourcePath);
        }
        Reader reader = new InputStreamReader(stream);
        Gson gson = new Gson();
        return gson.fromJson(reader, PizzeriaConfig.class);
    }
}
