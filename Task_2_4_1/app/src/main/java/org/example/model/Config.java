package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private List<Lab> labs = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Assignment> assignments = new ArrayList<>();
    private List<Checkpoint> checkpoints = new ArrayList<>();
    private Settings settings = new Settings();

    public List<Lab> getLabs() { return labs; }
    public void setLabs(List<Lab> labs) { this.labs = labs; }

    public List<Group> getGroups() { return groups; }
    public void setGroups(List<Group> groups) { this.groups = groups; }

    public List<Assignment> getAssignments() { return assignments; }
    public void setAssignments(List<Assignment> assignments) { this.assignments = assignments; }

    public List<Checkpoint> getCheckpoints() { return checkpoints; }
    public void setCheckpoints(List<Checkpoint> checkpoints) { this.checkpoints = checkpoints; }

    public Settings getSettings() { return settings; }
    public void setSettings(Settings settings) { this.settings = settings; }

    public Lab findLab(String id) {
        for (Lab l : labs) {
            if (l.getId().equals(id)) return l;
        }
        return null;
    }

    public Student findStudent(String nick) {
        for (Group g : groups) {
            for (Student s : g.getStudents()) {
                if (s.getNick().equals(nick)) return s;
            }
        }
        return null;
    }

    public Group findGroupOf(String nick) {
        for (Group g : groups) {
            for (Student s : g.getStudents()) {
                if (s.getNick().equals(nick)) return g;
            }
        }
        return null;
    }
}
