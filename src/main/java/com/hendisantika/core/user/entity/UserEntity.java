package com.hendisantika.core.user.entity;

import com.hendisantika.core.security.jpa.SecureToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 10/04/22
 * Time: 13.29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private String token;
    private boolean accountVerified;
    private int failedLoginAttempts;
    private boolean loginDisabled;

    private boolean mfaEnabled;
    private String secret;

    @OneToMany(mappedBy = "user")
    private Set<SecureToken> tokens;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_groups",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"
            ))
    private Set<Group> userGroups = new HashSet<>();

    public void addUserGroups(Group group) {
        userGroups.add(group);
        group.getUsers().add(this);
    }

    public void removeUserGroups(Group group) {
        userGroups.remove(group);
        group.getUsers().remove(this);
    }
}
