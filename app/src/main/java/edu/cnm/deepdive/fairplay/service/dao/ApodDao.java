package edu.cnm.deepdive.fairplay.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.fairplay.model.Apod;
import edu.cnm.deepdive.fairplay.model.Apod.MediaType;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Dao
public interface ApodDao {

  @Insert
  Single<Long> insert(Apod apod);

  default Single<Apod> insertAndRefresh(Apod apod) {
    return insert(apod)
        .doOnSuccess(apod::setId)
        .map((id) -> apod);
  }

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(List<Apod> apods);

  default Single<List<Apod>> insertAndRefresh(List<Apod> apods) {
    return insert(apods)
        .map((ids) -> {
          Iterator<Long> idIterator = ids.iterator();
          Iterator<Apod> apodIterator = apods.iterator();
          while (idIterator.hasNext() && apodIterator.hasNext()) {
            apodIterator.next().setId(idIterator.next());
          }
          apods.removeIf((apod) -> apod.getId() <= 0);
          return apods;
        });
  }

  @Insert
  Single<List<Long>> insert(Apod... apods);

  @Update
  Single<Integer> update(Apod apod);

  @Update
  Single<Integer> update(Collection<Apod> apods);

  @Delete
  Single<Integer> delete(Apod apod);

  @Query("DELETE FROM apod")
  Single<Integer> deleteAll();

  @Query("SELECT * FROM apod WHERE apod_id = :id")
  LiveData<Apod> get(long id);

  @Query("SELECT * FROM apod WHERE media_type = :mediaType ORDER BY date_created")
  LiveData<List<Apod>> get(MediaType mediaType);

}
