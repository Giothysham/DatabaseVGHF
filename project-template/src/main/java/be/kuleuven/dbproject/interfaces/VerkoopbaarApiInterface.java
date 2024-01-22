package be.kuleuven.dbproject.interfaces;

import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.Winkel;

import java.util.List;

public interface VerkoopbaarApiInterface {

    Winkel getSearchWinkel();

    Uitgever getSearchUitgever();

    <T> void creatSearchQuerry(T filterValue);

    <T> void removeFilterByClass(T filterValue);

    void deleteVerkoopbaar(List<VerkoopbaarInterface> verkoopbaar);

    void clearSearchQuerry();

    List<VerkoopbaarInterface> getVerkoopbaarVoorUser();

    List<VerkoopbaarInterface> searchVerkoopbaarByFilters(String naam);

    VerkoopbaarInterface getVerkoopbaarById(String ID) throws Exception;

    boolean gebruiktUitgever(Uitgever uitgever);

    boolean gebruiktWinkel(Winkel winkel);

    void updateVerkoopbaar(VerkoopbaarInterface verkoopbaar) throws Exception;
    
}
