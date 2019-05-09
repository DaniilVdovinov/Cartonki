package f.repositories;

import android.content.Context;
import f.cartonki.MainActivity;
import f.models.Pack;

import java.util.List;

public interface CrudRepository<T, ID> {
    void save(T model, Context context);
    void update(T model);
    void delete(ID id);
    T find(ID id);

    List<T> findAll();
}