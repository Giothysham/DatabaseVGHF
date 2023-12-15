package be.kuleuven.dbproject.interfaces;

import be.kuleuven.dbproject.model.Winkel;

import java.util.List;

public interface VerkoopbaarApiInterface {

    Winkel getSearchWinkel();

    <T> void creatSearchQuerry(T filterValue);

    <T> void removeFilterByClass(T filterValue);

    void deleteVerkoopbaar(List<VerkoopbaarInterface> verkoopbaar);

    List<VerkoopbaarInterface> getVerkoopbaarVoorUser();

    List<VerkoopbaarInterface> searchVerkoopbaarByFilters(String naam);

    VerkoopbaarInterface getVerkoopbaarById(String ID) throws Exception;
    
}
