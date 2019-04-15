package f.repositories;

import f.models.Card;

import java.util.Optional;

public interface CardsRepository extends CrudRepository<Card, Long> {
    Optional<Card> findOneByQuestion(String question);
    //оставил в качестве примера для других методов
}
