package ba.sum.fpmoz.livevibe.service;

import ba.sum.fpmoz.livevibe.Model.Concert;
import ba.sum.fpmoz.livevibe.repositories.ConcertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    public Optional<Concert> getConcertById(Long id) {
        return concertRepository.findById(id);
    }

    public void saveConcert(Concert concert) {
        concertRepository.save(concert);
    }

    public void deleteConcert(Long id) {
        concertRepository.deleteById(id);
    }
}
