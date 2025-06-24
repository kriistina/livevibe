package ba.sum.fpmoz.livevibe.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String artist;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String venue;
    private String city;


    @Column(columnDefinition = "TEXT")
    private Double ticketPrice;
    private String imageUrl;
    private Integer availableTickets;


}

