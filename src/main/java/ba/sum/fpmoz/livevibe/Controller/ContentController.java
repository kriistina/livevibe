package ba.sum.fpmoz.livevibe.Controller;

import ba.sum.fpmoz.livevibe.Model.Concert;
import ba.sum.fpmoz.livevibe.Model.User;
import ba.sum.fpmoz.livevibe.service.ConcertService;
import ba.sum.fpmoz.livevibe.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class ContentController {

    private final ConcertService concertService;
    private final UserService userService;

    public ContentController(ConcertService concertService, UserService userService) {
        this.concertService = concertService;
        this.userService = userService;
    }

    // Početna stranica - svi koncerti ("/" i "/index")
    @GetMapping({"/", "/index"})
    public String prikaziNaslovnu(Model model, @ModelAttribute("successMessage") String successMessage) {
        List<Concert> concerts = concertService.getAllConcerts();
        model.addAttribute("concerts", concerts);
        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }
        return "index";
    }

    // Login i registracija
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }


    // Forma za dodavanje koncerta (admin)
    @GetMapping("/admin/addConcert")
    public String showAddConcertForm(Model model) {
        model.addAttribute("concert", new Concert());
        return "addconcert";
    }

    // Spremanje novog koncerta (admin)
    @PostMapping("/admin/addConcert")
    public String addConcert(@ModelAttribute("concert") Concert concert) {
        concertService.saveConcert(concert);
        return "redirect:/admin";
    }

    // Brisanje koncerta
    @PostMapping("/concert/delete/{id}")
    public String deleteConcert(@PathVariable Long id) {
        concertService.deleteConcert(id);
        return "redirect:/";
    }

    // "Moji koncerti" stranica - prikazuje koncerte za prijavljenog korisnika
    @GetMapping("/mojikoncerti")
    public String mojiKoncerti(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Concert> kupljeniKoncerti = user.getKupljeniKoncerti();
            model.addAttribute("kupljeniKoncerti", kupljeniKoncerti);
        }
        return "mojikoncerti";
    }


    // Spremanje koncerta općenito (npr. korisnik kreira koncert)
    @PostMapping("/concert")
    public String createConcert(@ModelAttribute Concert concert) {
        concertService.saveConcert(concert);
        return "redirect:/";
    }

    // Kupovina ulaznice - dodaje koncert u korisnikove kupljene koncerte
    @PostMapping("/concert/kupi/{id}")
    public String kupiUlaznicu(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        Optional<Concert> concertOpt = concertService.getConcertById(id);

        if (userOpt.isPresent() && concertOpt.isPresent()) {
            User user = userOpt.get();
            Concert concert = concertOpt.get();

            if (!user.getKupljeniKoncerti().contains(concert)) {
                user.getKupljeniKoncerti().add(concert);
                userService.saveUser(user);
                redirectAttributes.addFlashAttribute("successMessage", "Ulaznica uspješno kupljena i dodana u Moji koncerti!");
            } else {
                redirectAttributes.addFlashAttribute("infoMessage", "Već ste kupili ulaznicu za ovaj koncert.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Došlo je do greške prilikom kupovine ulaznice.");
        }

        return "redirect:/";
    }
    @PostMapping("/concert/ukloni/{id}")
    public String ukloniKoncert(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            boolean removed = user.getKupljeniKoncerti().removeIf(concert -> concert.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                redirectAttributes.addFlashAttribute("successMessage", "Koncert je uspješno uklonjen iz Mojih koncerata.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Koncert nije pronađen u Mojim koncertima.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Korisnik nije pronađen.");
        }
        return "redirect:/mojikoncerti";
    }



}
