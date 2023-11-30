package be.kuleuven.dbproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.*; //later fixen specifieke imports

import java.util.List;


@Entity
public class User {

    @Column(name = "achternaam")
    private String achternaam;

    @Column(name = "voornaam")
    private String voornaam;

    @Column(name = "telefoonnummer")
    private String telefoonnummer;

    @Column(name = "adres")
    private String adres;

    @Column(name = "stad")
    private String stad;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "provincie")
    private String provincie;

    @Column(name = "land")
    private String land;

    @Column(name = "userId")
    @GeneratedValue
    @Id
    private int userId;

    @Column(name = "email")
    private String email;

    @Column(name = "wachtwoord")
    private String wachtwoord;

    @Column(name = "bevoegdheid")
    private Integer bevoegdheid;

    @ManyToMany(targetEntity = Game.class, cascade = { CascadeType.ALL })
	@JoinTable(name = "game_user_test",
				joinColumns = { @JoinColumn(name = "userID") }, 
				inverseJoinColumns = { @JoinColumn(name = "gameID") })
	private List<Game> uitgeleendeGame;

    public User() {
    }


    public User(String achternaam, String voornaam, String telefoonnummer, String adres, String stad, String postcode, String provincie, String land, int userId, String email, String wachtwoord, Integer bevoegdheid) {
        this.achternaam = achternaam;
        this.voornaam = voornaam;
        this.telefoonnummer = telefoonnummer;
        this.adres = adres;
        this.stad = stad;
        this.postcode = postcode;
        this.provincie = provincie;
        this.land = land;
        this.userId = userId;
        this.email = email;
        this.wachtwoord = wachtwoord;
        this.bevoegdheid = bevoegdheid;
    }

    public void addToListGames(List<Game> game){
        uitgeleendeGame.addAll(game);
    }

    public List<Game> getUitgeleendeGames(){
        return uitgeleendeGame;
    }

    public String getAchternaam() {
        return this.achternaam;
    }

    public String getVoornaam() {
        return this.voornaam;
    }

    public String getTelefoonnummer() {
        return this.telefoonnummer;
    }

    public String getAdres() {
        return this.adres;
    }

    public String getStad() {
        return this.stad;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public String getProvincie() {
        return this.provincie;
    }

    public String getLand() {
        return this.land;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getWachtwoord() {
        return this.wachtwoord;
    }

    public Integer getBevoegdheid() {
        return this.bevoegdheid;
    }

}
