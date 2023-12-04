package be.kuleuven.dbproject.model.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Factuur;
import be.kuleuven.dbproject.model.Game;
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

        if(!gameList.isEmpty()){

            var rentedgames = new ArrayList<Game>();
            entityManager.getTransaction().begin();

            var stock = 0;

            for(Game game: gameList){
                if(!rentedgames.contains(game)){
                    rentedgames.add(game);
                    stock = game.getStock();
                }

                if(stock > 0){
                    //fix => vragen aan wouter
                    game.setTempStock(stock-1);
                    stock = stock - 1;
                    
                    var factuur = new Factuur(0,user,game.getKostPrijs(),game,null, game.getWinkel());
                    
                    //entityManager.persist(user);
                    entityManager.persist(factuur);
                }
                else{
                    //over gaan werkt maar als er dan een element verwijderd wordt wordt het programma boos. 
                    //veranderingen blijven bestaan zelfs na de rol back => vragen aan wouter. => game wordt zwz geupdate => roll back fixed dit niet
                    entityManager.getTransaction().rollback();
                    throw new Exception("more items selected than avaible");
                }
            }
            
            for(Game game: rentedgames){
                game.setTempToStock();
            }

            entityManager.getTransaction().commit();
        }
        else{
            throw new Exception("no items selected");
        }
    }

    public void createFactuurForExtra(List<Extra> extraList, User user) throws Exception{

        if(!extraList.isEmpty()){

            var boughtExtra = new ArrayList<Extra>();
            entityManager.getTransaction().begin();

            var stock = 0;

            for(Extra extra: extraList){
                if(!boughtExtra.contains(extra)){
                    boughtExtra.add(extra);
                    stock = extra.getStock();
                }

                if(stock > 0){
                    //fix => vragen aan wouter
                    extra.setTempStock(stock-1);
                    stock = stock - 1;
                    
                    var factuur = new Factuur(0,user ,extra.getKostprijs(), null,extra, extra.getWinkel());
                    
                    entityManager.persist(user);
                    entityManager.persist(factuur);
                }
                else{
                    //over gaan werkt maar als er dan een element verwijderd wordt wordt het programma boos. 
                    //veranderingen blijven bestaan zelfs na de rol back => vragen aan wouter. => game wordt zwz geupdate => roll back fixed dit niet
                    entityManager.getTransaction().rollback();
                    throw new Exception("more items selected than avaible");
                }
            }
            
            for(Extra extra: boughtExtra){
                extra.setTempToStock();
                //TODO: mauro voeg extra chek bij voor te zien of data recent verander is. 
            }

            entityManager.getTransaction().commit();
        }
        else{
            throw new Exception("no items selected");
        }
    }

    public void creatUser(User user){
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

    public List<User> getUsers() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(User.class);
        var root = query.from(User.class);
        var select = query.select(root);
        
        return entityManager.createQuery(select).getResultList();
    }

    public void updateUser(User user){
        try{
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        } catch(Exception e){
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }
}
