package account.dtos.entities;

import account.dtos.Action;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class SecurityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate date;
    private Action action;
    private String subject;
    private String object;
    private String path;



    public LocalDate getDate() {
        return date;
    }

    public SecurityEvent setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Action getAction() {
        return action;
    }

    public SecurityEvent setAction(Action action) {
        this.action = action;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public SecurityEvent setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getObject() {
        return object;
    }

    public SecurityEvent setObject(String object) {
        this.object = object;
        return this;
    }

    public String getPath() {
        return path;
    }

    public SecurityEvent setPath(String path) {
        this.path = path;
        return this;
    }
}
