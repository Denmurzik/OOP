package org.example;

import java.io.File;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import org.example.dsl.DslLoader;
import org.example.report.HtmlReporter;
import org.example.runner.GitAuthCheck;

/** Точка входа приложения oop-checker. */
public class App {

    /** Парсит команду, находит DSL-скрипт и запускает проверку. */
    public static void main(String[] args) {
        try {
            int exitCode = run(args, System.out, System.err);
            if (exitCode != 0) {
                System.exit(exitCode);
            }
        } catch (Exception e) {
            System.err.println("Ошибка запуска oop-checker: " + e.getMessage());
            System.exit(1);
        }
    }

    static int run(String[] args, PrintStream out, PrintStream err) throws Exception {
        if (args.length == 0) {
            err.println("Использование: oop-checker <команда>");
            err.println("Команды: test | report");
            return 1;
        }
        String command = args[0];

        File workDir = new File(".").getCanonicalFile();
        GitAuthCheck gitAuthCheck = new GitAuthCheck();
        Map<String, AppCommand> commands = commandRegistry();
        AppCommand appCommand = commands.get(command);

        if (appCommand == null) {
            err.println("Неизвестная команда: " + command);
            return 1;
        }

        if (!gitAuthCheck.check()) {
            err.println("git недоступен или настроен на запрос пароля");
            return 1;
        }

        appCommand.run(workDir, out);
        return 0;
    }

    static Map<String, AppCommand> commandRegistry() {
        AppCommand reportCommand = new ReportCommand(new DslLoader(), new HtmlReporter());
        Map<String, AppCommand> commands = new LinkedHashMap<>();
        commands.put("test", reportCommand);
        commands.put("report", reportCommand);
        return commands;
    }
}
