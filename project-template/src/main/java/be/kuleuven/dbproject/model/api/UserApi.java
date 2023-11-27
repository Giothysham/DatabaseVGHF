package be.kuleuven.dbproject.model.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import be.kuleuven.dbproject.model.Factuur;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Game_user;
import be.kuleuven.dbproject.model.User;

public class UserApi {
    
    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;

    public UserApi(DbConnection dbConnection){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
    }

    public User chekUserAndWachtwoord(String email, String Wachtwoord) throws Exception{
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(User.class);
        var root = query.from(User.class);

        var predicateEmail = criteriaBuilder.equal(root.get("email"), email);
        var predicateWachtwoord = criteriaBuilder.equal(root.get("wachtwoord"), Wachtwoord);

        var predicateAnd = criteriaBuilder.and(predicateEmail, predicateWachtwoord);

        var result = entityManager.createQuery(query.where(predicateAnd)).getResultList();

        if(!result.isEmpty()){
            return result.get(0);
        }
        else{
            throw new Exception("wrong Email or Password");
        }
    }

    public void createFactuurForGame(List<Game> gameList, User user) throws Exception{

        //nog kopel tabel maken.
        if(!gameList.isEmpty()){
            entityManager.getTransaction().begin();
            for(Game game: gameList){

                var stock = game.getStock();
                var verkocht = game.getVerkocht();
                System.out.println("elemnt of game:" + game.getNaam() + "   er zijn " + stock + " in stock");
                if(stock > 0){
                    game.setStock(stock-1);
                    game.setVerkocht(verkocht+1);

                    var game_user = new Game_user(user.getUserId(), game.getGameID(),0);
                    var factuur = new Factuur(0,user.getUserId(),game.getKostPrijs(),game.getGameID(),0, game.getWinkelID());
                    
                    entityManager.persist(game_user);
                    entityManager.persist(factuur);
                }
                else{
                    //over gaan werkt maar als er dan een element verwijderd wordt wordt het programma boos. 
                    //veranderingen blijven bestaan zelfs na de rol back => vragen aan wouter. => game wordt zwz geupdate => roll back fixed dit niet
                    entityManager.getTransaction().rollback();
                    throw new Exception("more items selected than avaible");
                }
            }
            entityManager.getTransaction().commit();
        }
        else{
            throw new Exception("no items selected");
        }
    }
}
