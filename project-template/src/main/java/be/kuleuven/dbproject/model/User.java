package be.kuleuven.dbproject.model;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

//later fixen specifieke imports

import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;


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
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",pkColumnName="name", valueColumnName="seq",pkColumnValue="user")
    @Id
    private int userId;

    @Column(name = "email")
    private String email;

    @Column(name = "wachtwoord")
    private String wachtwoord;

    @Column(name = "bevoegdheid")
    private Integer bevoegdheid;

    @ManyToMany(fetch = FetchType.EAGER,targetEntity = Game.class, cascade = { CascadeType.MERGE })
	@JoinTable(name = "game_user",
    joinColumns = { @JoinColumn(name = "userID") }, 
    inverseJoinColumns = { @JoinColumn(name = "gameID") })
	private List<Game> uitgeleendeGame;

    @OneToMany(mappedBy = "user")
    private List<Factuur> factuur;

    @Transient
    private SecretKey key;

    @Transient
    private IvParameterSpec iv;

    @Transient
    private String algorithm;

    @Transient
    private KeyGenerator keygen;

    public User() {
    }


    public User(String achternaam, String voornaam, String telefoonnummer, String adres, String stad, String postcode, String provincie, String land, int userId, String email, String wachtwoord, Integer bevoegdheid, List<Game> uitgeleendeGame) {
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

        try {
            keygen = KeyGenerator.getInstance("AES");
            key = keygen.generateKey();
            iv = new IvParameterSpec(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("wachtwoord encryptie mislukt");
        }
        algorithm = "AES/CBC/PKCS5Padding";
        this.wachtwoord = encryptePassWord(wachtwoord);
        this.bevoegdheid = bevoegdheid;
        this.uitgeleendeGame = uitgeleendeGame;
    }

    private String encryptePassWord(String password){
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encText = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("wachtwoord encryptie mislukt");
        }
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
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] wachtwoordByte = cipher.doFinal(Base64.getDecoder().decode(this.wachtwoord));
            return new String(wachtwoordByte);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("wachtwoord decryptie mislukt");
        }
    }

    public Integer getBevoegdheid() {
        return this.bevoegdheid;
    }

    public void setBevoegdheid(boolean bevoegd){
        if(bevoegd){
            bevoegdheid = 1;
        }
        else{
            bevoegdheid = 0;
        }
    }

}
