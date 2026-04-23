package org.example.runner;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CheckstyleRunner {

    /** Количество нарушений стиля в src/main/java, или -1 при ошибке. */
    public int countViolations(File projectDir) {
        File srcRoot = findJavaSrc(projectDir);
        if (srcRoot == null) return -1;

        List<File> javaFiles = new ArrayList<>();
        collectJava(srcRoot, javaFiles);
        if (javaFiles.isEmpty()) return 0;

        try {
            File configFile = extractConfigToTempFile();
            Configuration config = ConfigurationLoader.loadConfiguration(
                    configFile.getAbsolutePath(),
                    new PropertiesExpander(new Properties()));

            CountingListener listener = new CountingListener();
            Checker checker = new Checker();
            checker.setModuleClassLoader(Checker.class.getClassLoader());
            checker.configure(config);
            checker.addListener(listener);

            checker.process(javaFiles);
            checker.destroy();
            return listener.count;
        } catch (Exception e) {
            return -1;
        }
    }

    private File extractConfigToTempFile() throws Exception {
        InputStream in = getClass().getResourceAsStream("/google_checks.xml");
        if (in == null) throw new IllegalStateException("google_checks.xml нет в ресурсах");
        File tmp = File.createTempFile("google_checks", ".xml");
        tmp.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tmp);
        byte[] buf = new byte[4096];
        int n;
        while ((n = in.read(buf)) > 0) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        return tmp;
    }

    private File findJavaSrc(File projectDir) {
        File a = new File(projectDir, "src/main/java");
        if (a.isDirectory()) return a;
        File[] modules = projectDir.listFiles();
        if (modules != null) {
            for (File m : modules) {
                if (!m.isDirectory()) continue;
                File b = new File(m, "src/main/java");
                if (b.isDirectory()) return b;
            }
        }
        return null;
    }

    private void collectJava(File dir, List<File> out) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) collectJava(f, out);
            else if (f.getName().endsWith(".java")) out.add(f);
        }
    }

    /** Слушатель, считающий ошибки стиля. */
    private static class CountingListener implements AuditListener {
        int count = 0;

        @Override public void auditStarted(AuditEvent e) {}
        @Override public void auditFinished(AuditEvent e) {}
        @Override public void fileStarted(AuditEvent e) {}
        @Override public void fileFinished(AuditEvent e) {}
        @Override public void addException(AuditEvent e, Throwable t) {}

        @Override
        public void addError(AuditEvent e) {
            count++;
        }
    }
}
