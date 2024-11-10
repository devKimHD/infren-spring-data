package study.dataJPA.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member
{
    @Id
    @GeneratedValue
    private Long id;
    private String username;

    //public 말고 protected
    protected Member() {
    }

    public Member(String username) {
        this.username = username;
    }
    public void changeUserName(String username)
    {
        this.username = username;
    }
}
