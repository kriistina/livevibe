package ba.sum.fpmoz.livevibe.Controller;

import ba.sum.fpmoz.livevibe.Model.Concert;
import ba.sum.fpmoz.livevibe.Model.User;
import ba.sum.fpmoz.livevibe.repositories.ConcertRepository;
import ba.sum.fpmoz.livevibe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;

    @Autowired
    public AdminController(UserRepository userRepository, ConcertRepository concertRepository) {
        this.userRepository = userRepository;
        this.concertRepository = concertRepository;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<User> users = userRepository.findAll();
        List<Concert> concerts = concertRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("concerts", concerts);

        return "admin";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/deleteConcert/{id}")
    public String deleteConcert(@PathVariable Long id) {
        concertRepository.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/editConcert/{id}")
    public String showEditConcertForm(@PathVariable Long id, Model model) {
        Optional<Concert> concert = concertRepository.findById(id);
        if (concert.isPresent()) {
            model.addAttribute("concert", concert.get());
            return "editconcert";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/admin/editConcert")
    public String editConcert(@ModelAttribute Concert concert) {
        Concert stari = concertRepository.findById(concert.getId())
                .orElseThrow(() -> new IllegalArgumentException("Ne postoji koncert s ID: " + concert.getId()));

        stari.setArtist(concert.getArtist());
        stari.setDate(concert.getDate());
        stari.setVenue(concert.getVenue());
        stari.setCity(concert.getCity());
        stari.setTicketPrice(concert.getTicketPrice());
        stari.setAvailableTickets(concert.getAvailableTickets());
        // Ne diramo imagePath – ostaje kako je bio

        concertRepository.save(stari);
        return "redirect:/admin";
    }
}
