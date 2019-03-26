package io.github.hildi.can.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class User implements Serializable {

    private static final long serialVersionUID = 2185813755505459630L;

    private final Long id;
    private final String nickName;
    private String email;
    private FullName fullName;
    private Collection<String> permissions;
    private Map<String, String> attributes;
    private Date createdAt;

    public User(Long id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public User(Long id, String nickName, String email, FullName fullName, Collection<String> permissions, Map<String, String> attributes, Date createdAt) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.fullName = fullName;
        this.permissions = permissions;
        this.attributes = attributes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FullName getFullName() {
        return fullName;
    }

    public void setFullName(FullName fullName) {
        this.fullName = fullName;
    }

    public Collection<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<String> permissions) {
        this.permissions = permissions;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
            Objects.equals(nickName, user.nickName) &&
            Objects.equals(email, user.email) &&
            Objects.equals(fullName, user.fullName) &&
            Objects.equals(permissions, user.permissions) &&
            Objects.equals(attributes, user.attributes) &&
            Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickName, email, fullName, permissions, attributes, createdAt);
    }

    @Override
    public String toString() {
        return "id = " + id + "\n" +
            "nickName = " + nickName + "\n" +
            "email = " + email + "\n" +
//            //     ?  "firstName = " + fullName.split("\\.") + "\n" +
//            //     ?  "lastName = " + Solohub + "\n" +
            "permissions = " + permissions + "\n" +
            "attributes = " + attributes + "\n" +
            "createdAt = " + createdAt + "\n";
    }
}
