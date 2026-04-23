package org.example;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.example.dsl.DslLoader;
import org.example.model.Config;
import org.example.model.Group;
import org.example.model.StudentReport;
import org.example.report.HtmlReporter;
import org.example.runner.GitAuthCheck;

/** Точка входа приложения oop-checker. */
public class App {

    /** Парсит команду, находит DSL-скрипт и запускает проверку. */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Использование: oop-checker <команда>");
            System.err.println("Команды: test | report");
            System.exit(1);
        }
        String command = args[0];

        File workDir = new File(".").getCanonicalFile();
        File script = new File(workDir, "oop-checker.groovy");
        if (!script.exists()) {
            System.err.println("В рабочей директории не найден oop-checker.groovy");
            System.exit(1);
        }

        if (!new GitAuthCheck().check()) {
            System.err.println("git недоступен или настроен на запрос пароля");
            System.exit(1);
        }

        Config config = new DslLoader().load(script);

        if (command.equals("test") || command.equals("report")) {
            Checker checker = new Checker(config, workDir);
            Map<Group, List<StudentReport>> data = checker.run();
            String html = new HtmlReporter().build(config, data);
            System.out.println(html);
        } else {
            System.err.println("Неизвестная команда: " + command);
            System.exit(1);
        }
    }
}
