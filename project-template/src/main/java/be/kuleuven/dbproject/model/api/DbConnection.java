package be.kuleuven.dbproject.model.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.enums.Console;

public class DbConnection {
    
    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;


    public DbConnection() {

        //make different class for the game and so on conection
        if(sessionFactory == null || entityManager == null || !sessionFactory.isOpen()){
            sessionFactory = Persistence.createEntityManagerFactory("be.kuleuven.dbproject.model");
            entityManager = sessionFactory.createEntityManager();

            //creatWinkel();
            //creatGames();
            //creatGenre();

        }
    }  
    
    public EntityManager getEntityManager(){
        return entityManager;
    }

    public EntityManagerFactory getsessionFactory(){
        return sessionFactory;
    }
    

    private void creatWinkel() {
        Winkel winkel1 = new Winkel(0, 0, "winkelWandel straat", "diest", "3290", "vlaams-brabant","belgie");
        Winkel winkel2 = new Winkel(0, 0, "straat 3", "antwerpen", "3290", "antwerpen","belgie");
        Winkel winkel3 = new Winkel(0, 0, "straat doet raar", "brussel", "3290", "brussel","belgie");

        entityManager.getTransaction().begin();
        entityManager.persist(winkel1);
        entityManager.persist(winkel2);
        entityManager.persist(winkel3);
        entityManager.getTransaction().commit();
    }

    private void creatGames(){

        Game game1 = new Game(10, 0, Console.Atari,0, 0, 10, 0, "ledgends of zeld", null);
        Game game2 = new Game(5, 0, Console.MacOs, 0,1, 30, 2, "call of duty", null);
        Game game3 = new Game(4, 0, Console.Windows, 0,2, 10.2, 3, "the sims", null);
        Game game4 = new Game(20, 0, Console.ps, 0, 0,10.5, 2, "gta V", null);
        Game game5 = new Game(6, 0, Console.MacOs, 0,2, 60.5, 1, "halo reach", null);
        Game game6 = new Game(100, 0, Console.xbox, 0,1, 1.3, 0, "fifa 2014", null);

        entityManager.getTransaction().begin();
        entityManager.persist(game1);
        entityManager.persist(game2);
        entityManager.persist(game3);
        entityManager.persist(game4);
        entityManager.persist(game5);
        entityManager.persist(game6);
        entityManager.getTransaction().commit();
    }

    private void creatGenre(){
        Genre genre1 = new Genre(0, "Real-time strategy", "test niet goed");
        Genre genre2 = new Genre(0, "Fixed shooter", "test niet goed");
        Genre genre3 = new Genre(0, "FPS", "test niet goed");
        Genre genre4 = new Genre(0, "Action", "test niet goed");

        entityManager.getTransaction().begin();
        entityManager.persist(genre1);
        entityManager.persist(genre2);
        entityManager.persist(genre3);
        entityManager.persist(genre4);
        entityManager.getTransaction().commit();
    }
    
    public List<Game> getGames(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);
        var select = query.select(root);
        
        return entityManager.createQuery(select).getResultList();
    }

    public List<Winkel> getWinkels(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Winkel.class);
        var root = query.from(Winkel.class);
        var select = query.select(root);
        
        return entityManager.createQuery(select).getResultList();
    }

    public Game getGameById(String ID){
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);

        query.where(criteriaBuilder.equal(root.get("gameID"), ID));
        var result = entityManager.createQuery(query).getResultList();
        
        // maak chek zodat lengte van de list 1 is 
        return result.get(0);
    }

    public List<Game> SearchGamesByName(String naam){

        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);

        query.where(criteriaBuilder.equal(root.get("naam"), naam));

        var result = entityManager.createQuery(query).getResultList();

        return result;
    }

    public void postGame(Game game){
        entityManager.getTransaction().begin();
        entityManager.persist(game);
        entityManager.getTransaction().commit();
    }

    public List<Genre> getGenres() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Genre.class);
        var root = query.from(Genre.class);
        var select = query.select(root);
        
        return entityManager.createQuery(select).getResultList();
    }

    public int getGenreIdByName(String naam) {
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Genre.class);
        var root = query.from(Genre.class);

        query.where(criteriaBuilder.equal(root.get("naam"), naam));

        var result = entityManager.createQuery(query).getResultList();

        if(result.size() > 1){
            //throw exception
            return 0;
        }
        else{
            return result.get(0).getGenreID();
        }
    }

}
