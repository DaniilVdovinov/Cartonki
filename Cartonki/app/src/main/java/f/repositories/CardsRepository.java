package f.repositories;

import f.models.Card;

import java.util.List;

public interface CardsRepository extends CrudRepository<Card, Long> {
    List<Card> findAllInPack(Long id);
}
