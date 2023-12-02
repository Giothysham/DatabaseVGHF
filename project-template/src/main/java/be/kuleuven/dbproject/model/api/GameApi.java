package be.kuleuven.dbproject.model.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import be.kuleuven.dbproject.model.Game;

public class GameApi {

    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;
    
    public GameApi(DbConnection dbConnection){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
    }
    
    // private void createGames(){

    //     Game game1 = new Game(10, 0, Console.Atari,0, 0, 10, 0, "ledgends of zeld", null);
    //     Game game2 = new Game(5, 0, Console.MacOs, 0,1, 30, 2, "call of duty", null);
    //     Game game3 = new Game(4, 0, Console.Windows, 0,2, 10.2, 3, "the sims", null);
    //     Game game4 = new Game(20, 0, Console.ps, 0, 0,10.5, 2, "gta V", null);
    //     Game game5 = new Game(6, 0, Console.MacOs, 0,2, 60.5, 1, "halo reach", null);
    //     Game game6 = new Game(100, 0, Console.xbox, 0,1, 1.3, 0, "fifa 2014", null);

    //     entityManager.getTransaction().begin();
    //     entityManager.persist(game1);
    //     entityManager.persist(game2);
    //     entityManager.persist(game3);
    //     entityManager.persist(game4);
    //     entityManager.persist(game5);
    //     entityManager.persist(game6);
    //     entityManager.getTransaction().commit();
    // } TODO: DELETE
    
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
}
