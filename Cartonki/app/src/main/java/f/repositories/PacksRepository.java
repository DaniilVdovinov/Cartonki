package f.repositories;

import f.models.Card;
import f.models.Pack;

import java.util.ArrayList;

public interface PacksRepository extends CrudRepository<Pack,Long> {
    ArrayList<Pack> findAll(Long id);
}
