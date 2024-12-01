package study.dataJPA.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Item implements Persistable<String> {

//    @Id //@GeneratedValue
//    private Long id;
    // id를 모종의 이유로 자동생성이 아닌 입력 후 돌릴시 merge로 동작
    // 방지하기 위해 Persistable 구현
    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id)
    {
        this.id=id;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
