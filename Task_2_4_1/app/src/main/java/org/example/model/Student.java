package org.example.model;

public class Student {
    private String nick;
    private String fullName;
    private String repo;

    public Student() {}

    public Student(String nick, String fullName, String repo) {
        this.nick = nick;
        this.fullName = fullName;
        this.repo = repo;
    }

    public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRepo() { return repo; }
    public void setRepo(String repo) { this.repo = repo; }
}
