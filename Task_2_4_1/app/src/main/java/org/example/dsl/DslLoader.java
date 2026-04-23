package org.example.dsl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.example.model.Config;

/** Загружает Groovy-DSL-скрипт и возвращает построенный Config. */
public class DslLoader {

    /** Читает DSL из файла и возвращает заполненный конфиг. */
    public Config load(File file) throws IOException {
        if (!file.exists()) {
            throw new IllegalArgumentException("Не найден DSL-файл: " + file.getAbsolutePath());
        }

        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass("org.example.dsl.ConfigScript");

        GroovyShell shell = new GroovyShell(new Binding(), cc);
        ConfigScript script = (ConfigScript) shell.parse(file);

        Config config = new Config();
        script.setConfig(config);
        script.setBaseDir(file.getParentFile());
        script.run();
        return config;
    }
}
