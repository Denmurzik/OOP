package org.example.report;

import java.util.List;
import java.util.Map;
import org.example.model.Checkpoint;
import org.example.model.Config;
import org.example.model.Group;
import org.example.model.Lab;
import org.example.model.LabResult;
import org.example.model.StudentReport;

/** Формирует HTML-отчёт по результатам проверки. */
public class HtmlReporter {
    private static final String HTML_HEAD = """
            <!DOCTYPE html>
            <html><head><meta charset="UTF-8"><title>OOP-checker report</title>
            <style>
            body{font-family:monospace;}
            table{border-collapse:collapse;margin:10px 0;}
            th,td{border:1px solid #000;padding:4px 8px;}
            caption{text-align:left;font-weight:bold;padding:4px 0;}
            </style></head><body>
            """;

    /** Собирает HTML с таблицами per-lab, сводной и по контрольным точкам. */
    public String build(Config config, Map<Group, List<StudentReport>> data) {
        StringBuilder html = new StringBuilder();
        html.append(HTML_HEAD);

        for (Map.Entry<Group, List<StudentReport>> e : data.entrySet()) {
            Group group = e.getKey();
            List<StudentReport> reports = e.getValue();
            if (reports.isEmpty()) {
                continue;
            }

            html.append(String.format("<h2>Группа %s</h2>%n", escape(group.getName())));

            for (Lab lab : config.getLabs()) {
                if (!labUsedIn(reports, lab.getId())) {
                    continue;
                }
                html.append(labTable(lab, reports));
            }

            html.append(summaryTable(group, reports, config));
            if (!config.getCheckpoints().isEmpty()) {
                html.append(checkpointTable(group, reports, config));
            }
        }

        html.append("</body></html>\n");
        return html.toString();
    }

    private boolean labUsedIn(List<StudentReport> reports, String labId) {
        for (StudentReport r : reports) {
            if (r.findResult(labId) != null) {
                return true;
            }
        }
        return false;
    }

    private String labTable(Lab lab, List<StudentReport> reports) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
                "<table><caption>Лабораторная %s (%s)</caption>%n",
                escape(lab.getId()), escape(lab.getName())));
        sb.append("<tr><th>Студент</th><th>Сборка</th><th>Документация</th>");
        sb.append("<th>Style guide</th><th>Тесты</th><th>Дата сдачи</th>");
        sb.append("<th>Статус</th><th>Доп. балл</th><th>Общий балл</th></tr>\n");
        for (StudentReport r : reports) {
            LabResult res = r.findResult(lab.getId());
            if (res == null) {
                continue;
            }
            sb.append("<tr>");
            sb.append("<td>").append(escape(r.getStudent().getFullName())).append("</td>");
            sb.append("<td>").append(mark(res.isBuildOk())).append("</td>");
            sb.append("<td>").append(mark(res.isDocOk())).append("</td>");
            sb.append("<td>").append(mark(res.isStyleOk())).append("</td>");
            sb.append("<td>").append(res.getTestsPassed()).append('/')
                    .append(res.getTestsFailed()).append('/')
                    .append(res.getTestsSkipped()).append("</td>");
            sb.append("<td>").append(
                            res.getSubmissionDate() == null ? "-" : res.getSubmissionDate())
                    .append("</td>");
            sb.append("<td>").append(escape(res.getStatus())).append("</td>");
            sb.append("<td>").append(res.getBonus()).append("</td>");
            sb.append("<td>").append(res.getTotalScore()).append("</td>");
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    private String summaryTable(Group group, List<StudentReport> reports, Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
                "<table><caption>Общая статистика группы %s</caption>%n",
                escape(group.getName())));
        sb.append("<tr><th>Студент</th>");
        for (Lab lab : config.getLabs()) {
            if (labUsedIn(reports, lab.getId())) {
                sb.append("<th>").append(escape(lab.getId())).append("</th>");
            }
        }
        sb.append("<th>Сумма</th><th>Активность</th><th>Оценка</th></tr>\n");

        for (StudentReport r : reports) {
            sb.append("<tr><td>").append(escape(r.getStudent().getFullName())).append("</td>");
            for (Lab lab : config.getLabs()) {
                if (!labUsedIn(reports, lab.getId())) {
                    continue;
                }
                LabResult res = r.findResult(lab.getId());
                sb.append("<td>").append(res == null ? 0 : res.getTotalScore()).append("</td>");
            }
            sb.append("<td>").append(r.totalScore()).append("</td>");
            sb.append("<td>").append(Math.round(r.getActivity() * 100)).append("%</td>");
            sb.append("<td>").append(escape(r.getGrade())).append("</td></tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    private String checkpointTable(Group group, List<StudentReport> reports, Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
                "<table><caption>Оценки по контрольным точкам группы %s</caption>%n",
                escape(group.getName())));
        sb.append("<tr><th>Студент</th>");
        for (Checkpoint cp : config.getCheckpoints()) {
            sb.append(String.format("<th>%s (%s)</th>", escape(cp.getName()), cp.getDate()));
        }
        sb.append("<th>Итог</th></tr>\n");

        for (StudentReport r : reports) {
            sb.append("<tr><td>").append(escape(r.getStudent().getFullName())).append("</td>");
            for (Checkpoint cp : config.getCheckpoints()) {
                String g = r.getCheckpointGrades().get(cp.getName());
                sb.append("<td>").append(escape(g == null ? "-" : g)).append("</td>");
            }
            sb.append("<td>").append(escape(r.getGrade())).append("</td></tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    private String mark(boolean ok) {
        if (ok) {
            return "+";
        }
        return "-";
    }

    private String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
