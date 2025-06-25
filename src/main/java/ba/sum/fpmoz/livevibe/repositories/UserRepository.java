package ba.sum.fpmoz.livevibe.repositories;

import ba.sum.fpmoz.livevibe.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Metoda za dohvat korisnika prema korisničkom imenu
    Optional<User> findByUsername(String username);

}
