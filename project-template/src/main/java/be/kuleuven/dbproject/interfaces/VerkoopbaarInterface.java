package be.kuleuven.dbproject.interfaces;

import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.Winkel;

public interface VerkoopbaarInterface {

    int getID();

    String getNaam();

    int getStock();

    int getVerkocht();

    Winkel getWinkel();

    Uitgever getUitgever();

    double getKostPrijs();

    void setKostPrijs(double kostPrijs);

    void setNaam(String naam);

    void setStock(int stock);

    void setUitgever(Uitgever uitgever);

    void setTempStock(Integer tempStock);

    void setTempToStock();
}
