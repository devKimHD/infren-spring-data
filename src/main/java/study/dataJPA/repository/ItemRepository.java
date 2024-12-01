package study.dataJPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.dataJPA.entity.Item;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
