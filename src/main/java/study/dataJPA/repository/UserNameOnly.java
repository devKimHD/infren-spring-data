package study.dataJPA.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UserNameOnly {

//    @Value("#{target.username + ' '+target.age + ' ' + target.team.name}")
    String getUsername();
}
