package ba.sum.fpmoz.livevibe.repositories;

import ba.sum.fpmoz.livevibe.Model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

}
