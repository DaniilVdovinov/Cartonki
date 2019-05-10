package f.repositories;

import android.content.Context;
import f.models.Card;
import f.models.Pack;

import java.util.ArrayList;

public interface PacksRepository extends CrudRepository<Pack,Long> {
    ArrayList<Pack> findAll(Long id);
    Pack findByName(String name, Context context);

}
