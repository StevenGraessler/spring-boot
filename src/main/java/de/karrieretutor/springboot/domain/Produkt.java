package de.karrieretutor.springboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.karrieretutor.springboot.enums.Kategorie;
import de.karrieretutor.springboot.enums.Unterkategorie;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO  )
    private Long id;

    @NotBlank(message = "{validation.produkt.name}")
    private String name;

    @NotBlank(message = "{validation.produkt.herkunft}")
    private String herkunft;

    @NotNull(message = "{validation.produkt.kategorie}")
    private Kategorie kategorie;

    @NotNull(message = "{validation.produkt.unterkategorie}")
    private Unterkategorie unterkategorie;

    @Min(value = 1, message = "{validation.produkt.preis}")
    private double preis = 1;

    private String dateiname;


    @JsonIgnore
    private byte[] datei;

    public Produkt() {
    }

    public Produkt(String name, String herkunft, Kategorie kategorie, Unterkategorie unterkategorie) {
        this.name = name;
        this.herkunft = herkunft;
        this.kategorie = kategorie;
        this.unterkategorie = unterkategorie;
    }

    public Produkt(String name, String herkunft, Kategorie kategorie, Unterkategorie unterkategorie, Long id, double preis) {
        this(name, herkunft, kategorie, unterkategorie);
        this.id = id;
        this.preis = preis;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getHerkunft() {
        return herkunft;
    }
    public void setHerkunft(String herkunft) {
        this.herkunft = herkunft;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }
    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    public Unterkategorie getUnterkategorie() {
        return unterkategorie;
    }
    public void setUnterkategorie(Unterkategorie unterkategorie) {
        this.unterkategorie = unterkategorie;
    }

    public double getPreis() {
        return preis;
    }
    public void setPreis(double preis) {
        this.preis = preis;
    }
    public String getPreisFormatiert() {
        return String.format("%.2f", this.preis);
    }

    public String getDateiname() {
        return this.dateiname;
    }
    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }

    public byte[] getDatei() {
        return datei;
    }
    public void setDatei(byte[] datei) {
        this.datei = datei;
    }

    @Override
    public String toString() {
        return "Produkt{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", herkunft='" + herkunft + '\'' +
                ", kategorie=" + kategorie +
                ", unterkategorie=" + unterkategorie +
                ", preis=" + preis +
                ", dateiname='" + dateiname + '\'' +
                '}';
    }
}

