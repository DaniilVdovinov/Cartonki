package f.repositories;

import android.content.Context;

import f.models.Card;

import java.util.List;

public interface CardsRepository extends CrudRepository<Card, Long> {
    List<Card> findAllInPack(Long id, Context context);

    List<Card> findDoneInPack(Long id, Context context);

    List<Card> findDone(Context context);
    Card findNewCard(Long id,Context context);
}
