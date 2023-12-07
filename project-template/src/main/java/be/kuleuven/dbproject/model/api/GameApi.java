package be.kuleuven.dbproject.model.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.enums.Console;

public class GameApi {

    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;

    private Console searchConsole;

    private Winkel searchWinkel;

    private Genre searchGenre;

    public GameApi(DbConnection dbConnection){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
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
    
    public List<Game> getGames(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);
        var select = query.select(root);
        
        return entityManager.createQuery(select).getResultList();
    }
    
    public Game getGameById(String ID) throws Exception{
        System.out.println(ID);
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);

        query.where(criteriaBuilder.equal(root.get("gameID"), ID));
        var result = entityManager.createQuery(query).getResultList();
        
        if(result.size() > 0){
            return result.get(0);
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
    
    public List<Game> searchGamesByFilters(String naam){

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

        Predicate predicate = criteriaBuilder.and(querryFilterList.toArray(new Predicate[querryFilterList.size()]));

        var result = entityManager.createQuery(query.where(predicate)).getResultList();

        System.out.println(result);

        return result;
    }
    
    public void postGame(Game game){
        entityManager.getTransaction().begin();
        entityManager.persist(game);
        entityManager.getTransaction().commit();
    }

    public void deleteGame(List<Game> games){ //try rond gooien om mislopende transactie te rollbacken4
        try{ 
            if(games.size() > 0){
                entityManager.getTransaction().begin();
                for(Game game: games){
                    var delete = entityManager.find(Game.class, game.getGameID());
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
