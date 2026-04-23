package org.example.runner;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GitClient {

    private final long timeoutSeconds;

    public GitClient(long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public boolean cloneOrUpdate(String repoUrl, File targetDir) {
        try {
            if (new File(targetDir, ".git").exists()) {
                ProcessHelper.Result fetch = ProcessHelper.run(targetDir, timeoutSeconds, "git", "fetch", "--all", "--prune");
                if (fetch.exitCode != 0) return false;
                String branch = defaultBranch(targetDir);
                if (branch == null) return false;
                ProcessHelper.Result reset = ProcessHelper.run(targetDir, timeoutSeconds,
                        "git", "reset", "--hard", "origin/" + branch);
                return reset.exitCode == 0;
            } else {
                targetDir.getParentFile().mkdirs();
                ProcessHelper.Result clone = ProcessHelper.run(targetDir.getParentFile(), timeoutSeconds,
                        "git", "clone", repoUrl, targetDir.getName());
                return clone.exitCode == 0;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String defaultBranch(File repoDir) {
        try {
            ProcessHelper.Result r = ProcessHelper.run(repoDir, timeoutSeconds,
                    "git", "symbolic-ref", "refs/remotes/origin/HEAD");
            if (r.exitCode == 0) {
                String out = r.output.trim();
                int idx = out.lastIndexOf('/');
                if (idx >= 0) return out.substring(idx + 1);
            }
            // Запасной путь: проверяем наличие main / master
            for (String b : new String[]{"main", "master"}) {
                ProcessHelper.Result chk = ProcessHelper.run(repoDir, timeoutSeconds,
                        "git", "rev-parse", "--verify", "origin/" + b);
                if (chk.exitCode == 0) return b;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /** Даты коммитов в диапазоне, в формате ISO. */
    public List<String> commitDates(File repoDir, LocalDate from, LocalDate to) {
        List<String> list = new ArrayList<>();
        try {
            ProcessHelper.Result r = ProcessHelper.run(repoDir, timeoutSeconds,
                    "git", "log",
                    "--since=" + from.toString(),
                    "--until=" + to.toString(),
                    "--format=%cI");
            if (r.exitCode == 0) {
                for (String line : r.output.split("\n")) {
                    if (!line.isBlank()) list.add(line.trim());
                }
            }
        } catch (Exception ignore) {
        }
        return list;
    }
}
