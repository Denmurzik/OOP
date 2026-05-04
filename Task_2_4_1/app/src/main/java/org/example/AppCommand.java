package org.example;

import java.io.File;
import java.io.PrintStream;

/** Контракт для CLI-команд приложения. */
public interface AppCommand {

    /** Выполняет команду и пишет результат в указанный поток. */
    void run(File workDir, PrintStream out) throws Exception;
}
