package org.example.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** Запуск внешних процессов с таймаутом и сбором stdout/stderr. */
public class ProcessHelper {

    /** Параметры запуска процесса. */
    public static class Request {
        public final File workDir;
        public final long timeoutSeconds;
        public final List<String> command;

        /** Создаёт параметры запуска процесса. */
        public Request(File workDir, long timeoutSeconds, List<String> command) {
            this.workDir = workDir;
            this.timeoutSeconds = timeoutSeconds;
            this.command = command;
        }
    }

    /** Результат выполнения процесса. */
    public static class Result {
        public final int exitCode;
        public final String output;
        public final boolean timedOut;

        /** Создаёт результат. */
        public Result(int exitCode, String output, boolean timedOut) {
            this.exitCode = exitCode;
            this.output = output;
            this.timedOut = timedOut;
        }
    }

    /** Запускает команду и возвращает результат. */
    public static Result run(File workDir, long timeoutSeconds, String... command)
            throws IOException, InterruptedException {
        return run(new Request(workDir, timeoutSeconds, Arrays.asList(command)));
    }

    /** Запускает команду и возвращает результат. */
    public static Result run(Request request) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(request.command);
        if (request.workDir != null) {
            pb.directory(request.workDir);
        }
        pb.redirectErrorStream(true);
        Map<String, String> env = pb.environment();
        env.put("GIT_TERMINAL_PROMPT", "0");

        Process p = pb.start();
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }
        br.close();

        boolean finished = p.waitFor(request.timeoutSeconds, TimeUnit.SECONDS);
        if (!finished) {
            p.destroyForcibly();
            return new Result(-1, sb.toString(), true);
        }
        return new Result(p.exitValue(), sb.toString(), false);
    }
}
