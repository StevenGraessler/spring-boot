package de.karrieretutor.springboot;

import de.karrieretutor.springboot.domain.BestelltesProdukt;
import de.karrieretutor.springboot.domain.Produkt;
import de.karrieretutor.springboot.domain.Warenkorb;
import de.karrieretutor.springboot.service.ProduktService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

@Controller
@RequestMapping(value = "/")
public class SimpleController {
    Logger LOG = LoggerFactory.getLogger(SimpleController.class);

    @Autowired
    ProduktService produktService;

    @Autowired
    MessageSource messageSource;

    public Warenkorb warenkorb = new Warenkorb();

    @GetMapping("/")
    public String homePage(Model model) {
        return "redirect:/index.html";
    }

    @GetMapping("/app")
    public String app() {
        return "redirect:/app/index.html";
    }

    @GetMapping("/index.html")
    public String shop(Model model, Locale locale) {
        model.addAttribute("titel", "Index");
        model.addAttribute("produkte", produktService.ladeProdukte());
        model.addAttribute("warenkorb", this.warenkorb);
        return "index";
    }

    @GetMapping("/{name}.html")
    public String htmlMapping(@PathVariable(name = "name") String name, Model model) {
        model.addAttribute("titel", StringUtils.capitalize(name));
        model.addAttribute("warenkorb", this.warenkorb);
        return name;
    }

    @GetMapping("/fotos/{id}")
    public ResponseEntity<Resource> fotos(@PathVariable Long id) throws IOException, URISyntaxException {
        byte[] bytes = new byte[0];
        Produkt produkt = produktService.getProdukt(id);
        if (produkt == null || produkt.getDatei() == null || produkt.getDatei().length==0) {
            // wenn kein Bild hochgeladen wurde, dann lade das Standard-Bild
            URL imageURL = this.getClass().getClassLoader().getResource("./static/images/no-image.png");
            bytes = Files.readAllBytes(Paths.get(imageURL.toURI()));
        } else {
            bytes = produkt.getDatei();
        }
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(new ByteArrayResource(bytes));
    }

    @GetMapping("/kaufen")
    public String kaufen(@RequestParam Long id, Model model, RedirectAttributes redirect, Locale locale) {
        String message = messageSource.getMessage("cart.product.id.not.found", new String[]{String.valueOf(id)}, locale);
        if (id != null) {
            Produkt produkt = produktService.getProdukt(id);
            if (produkt != null) {
                warenkorb.produktHinzufuegen(produkt);
                message = messageSource.getMessage("cart.added", new String[]{produkt.getName()}, locale);
            }
        }
        redirect.addFlashAttribute("message", message);
        return "redirect:index.html";
    }

    @GetMapping("/entfernen")
    public String entfernen(@RequestParam Long id, Model model, RedirectAttributes redirect, Locale locale) {
        String message = messageSource .getMessage("cart.not.found", new String[]{String.valueOf(id)}, locale);
        BestelltesProdukt entferntesProdukt = warenkorb.produktEntfernen(id);
        if (entferntesProdukt != null) {
            message = messageSource.getMessage("cart.removed", new Object[]{entferntesProdukt.getProdukt().getName()}, locale);
        }
        redirect.addFlashAttribute("message", message);
        model.addAttribute("titel", "Warenkorb");
        model.addAttribute("warenkorb", warenkorb);
        return "redirect:/warenkorb.html";
    }


}
