package ba.sum.fpmoz.livevibe.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_concerts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "concert_id")
    )
    private List<Concert> kupljeniKoncerti = new ArrayList<>();

}
