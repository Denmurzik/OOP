package org.example;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import org.example.dsl.DslLoader;
import org.example.model.Config;
import org.example.model.Group;
import org.example.model.StudentReport;
import org.example.report.HtmlReporter;

/** Команда, которая строит HTML-отчёт по DSL-конфигурации. */
public class ReportCommand implements AppCommand {

    private final DslLoader dslLoader;
    private final HtmlReporter htmlReporter;

    /** Создаёт команду отчёта с требуемыми зависимостями. */
    public ReportCommand(DslLoader dslLoader, HtmlReporter htmlReporter) {
        this.dslLoader = dslLoader;
        this.htmlReporter = htmlReporter;
    }

    @Override
    public void run(File workDir, PrintStream out) throws Exception {
        File script = new File(workDir, "oop-checker.groovy");
        if (!script.exists()) {
            throw new IllegalStateException("В рабочей директории не найден oop-checker.groovy");
        }

        Config config = dslLoader.load(script);
        Checker checker = new Checker(config, workDir);
        Map<Group, List<StudentReport>> data = checker.run();
        out.println(htmlReporter.build(config, data));
    }
}
