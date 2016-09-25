package me.jin.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nayoung on 2016. 9. 20..
 */
@Entity
@Getter
@Setter
public class Account {

    @GeneratedValue @Id
    private Long id;

    private String username;

    private String password;

    private String email;

    private String fullName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joined;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

}
