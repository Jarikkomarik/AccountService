package account.dtos.entities;


import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class UserGroup implements Comparable<UserGroup>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonValue
    @Column(unique = true, nullable = false)
    private String name;


    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public UserGroup() {
    }

    public UserGroup(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }



    @Override
    public String toString() {
        return  name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGroup userGroup = (UserGroup) o;
        return name.equals(userGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(UserGroup o) {
        return this.name.compareTo(o.getName());
    }
}
