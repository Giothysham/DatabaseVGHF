package be.kuleuven.dbproject.model.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;

import be.kuleuven.dbproject.interfaces.VerkoopbaarApiInterface;
import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.enums.Console;

public class GameApi implements VerkoopbaarApiInterface {

    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;

    private Console searchConsole;

    private Winkel searchWinkel;

    private Genre searchGenre;

    private User user;

    public GameApi(DbConnection dbConnection, User user){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
        this.user = user;
    }

    public Console getSearchConsole() {
        return this.searchConsole;
    }

    public Winkel getSearchWinkel() {
        return this.searchWinkel;
    }

    public Genre getSearchGenre() {
        return this.searchGenre;
    }
    
    public List<VerkoopbaarInterface> getVerkoopbaarVoorUser(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);
        List<Game> gameList = new ArrayList<>();

        if(user.getBevoegdheid() == 1){
            var select = query.select(root); 
            gameList = entityManager.createQuery(select).getResultList();
        }
        else{    
            //query.select(root);
            query.where(criteriaBuilder.greaterThan(root.get("stock"), 0));
            gameList = entityManager.createQuery(query).getResultList();
        }
        
        List<VerkoopbaarInterface> verkoopbaarList = new ArrayList<>();
     
        for(Game game : gameList){
            verkoopbaarList.add((VerkoopbaarInterface) game);
        }

        return verkoopbaarList;
    }
    
    public VerkoopbaarInterface getVerkoopbaarById(String ID) throws Exception{
        System.out.println(ID);
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);

        query.where(criteriaBuilder.equal(root.get("gameID"), ID));
        var result = entityManager.createQuery(query).getResultList();
        
        if(result.size() > 0){
            return (VerkoopbaarInterface) result.get(0);
        }
        else{
            throw new Exception("no game found with id: "+ ID);
        }
    
    }

    public <T> void creatSearchQuerry(T filterValue){
        if(filterValue.getClass() == Console.class){
            searchConsole = (Console) filterValue;
            System.out.println(searchConsole.name());
        }
        else if(filterValue.getClass() == Winkel.class){
            searchWinkel = (Winkel) filterValue;
            System.out.println(((Winkel) filterValue).getSmallAdress());
        }
        else if(filterValue.getClass() == Genre.class){
            searchGenre = (Genre) filterValue;
            System.out.println(searchGenre.getNaam());
        }
    }

    public void clearSearchQuerry(){
        searchConsole = null;
        searchWinkel = null;
        searchGenre = null;
    }
    
    public List<VerkoopbaarInterface> searchVerkoopbaarByFilters(String naam){

        List<Predicate> querryFilterList = new ArrayList<Predicate>();

        var criteriaBuilder = sessionFactory.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);

        if(searchConsole != null){
            System.out.println("searching for console: "+searchConsole);
           querryFilterList.add(criteriaBuilder.equal(root.get("console"), searchConsole));
        }
        
        if(searchGenre != null){
            System.out.println("searching for genre: "+searchGenre);
            querryFilterList.add(criteriaBuilder.equal(root.get("genre"), searchGenre));
        }
        
        if(searchWinkel != null){
            System.out.println("searching for winkel: "+searchWinkel);
            querryFilterList.add(criteriaBuilder.equal(root.get("winkel"), searchWinkel));
        }

        if(naam != null){
            System.out.println("searching for name: "+naam);
            querryFilterList.add( criteriaBuilder.equal(root.get("naam"), naam));
        }
        
        if(user.getBevoegdheid() == 0){
            querryFilterList.add(criteriaBuilder.greaterThan(root.get("stock"), 0));
        }

        Predicate predicate = criteriaBuilder.and(querryFilterList.toArray(new Predicate[querryFilterList.size()]));

        var result = entityManager.createQuery(query.where(predicate)).getResultList();

        System.out.println(result);

        List<VerkoopbaarInterface> verkoopbaarList = new ArrayList<>();
     
        for(Game game : result){
            verkoopbaarList.add((VerkoopbaarInterface) game);
        }

        return verkoopbaarList;
    }
    
    public void postVerkoopbaar(VerkoopbaarInterface game){
        entityManager.getTransaction().begin();
        entityManager.persist(game);
        entityManager.getTransaction().commit();
    }

    public void deleteVerkoopbaar(List<VerkoopbaarInterface> games){ //try rond gooien om mislopende transactie te rollbacken4
        try{ 
            if(games.size() > 0){
                entityManager.getTransaction().begin();
                for(VerkoopbaarInterface game: games){
                    var delete = entityManager.find(Game.class, game.getID());
                    entityManager.remove(delete);
                    entityManager.getTransaction().commit();
                }
            }
        } catch(Exception e){
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } 
    }

    public <T> void removeFilterByClass(T filterValue) {
        if(filterValue.getClass() == Console.class){
            searchConsole = null;
        }
        else if(filterValue.getClass() == Winkel.class){
            searchWinkel = null;
        }
        else if(filterValue.getClass() == Genre.class){
            searchGenre = null;
        }
    }
}
