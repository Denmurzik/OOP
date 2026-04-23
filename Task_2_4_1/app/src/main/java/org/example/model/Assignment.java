package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Assignment {
    private String nick;
    private List<String> labIds = new ArrayList<>();

    public Assignment() {}

    public Assignment(String nick, List<String> labIds) {
        this.nick = nick;
        this.labIds = labIds;
    }

    public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }

    public List<String> getLabIds() { return labIds; }
    public void setLabIds(List<String> labIds) { this.labIds = labIds; }
}
