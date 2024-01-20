package be.kuleuven.dbproject.model.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;
import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Factuur;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.User;

public class UserApi {
    
    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;

    private Factuur factuur;

    public UserApi(DbConnection dbConnection){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
    }

    public User chekUserAndWachtwoord(String email, String wachtwoord) throws Exception{
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(User.class);
        var root = query.from(User.class);

        var predicateEmail = criteriaBuilder.equal(root.get("email"), email);

        var result = entityManager.createQuery(query.where(predicateEmail)).getResultList();


        if(!result.isEmpty()){
            if(wachtwoord.equals(result.get(0).getWachtwoord())){
                return result.get(0);
            }
            else{
                throw new Exception("wrong Email or Password");

            }
        }
        else{
            throw new Exception("wrong Email or Password");
        }
    }

    public void createFactuurForVerkoopbaar(List<VerkoopbaarInterface> verkoopbaarLijst, User user) throws Exception{

        if(!verkoopbaarLijst.isEmpty()){

            var verkochteVerkoopbaar = new ArrayList<VerkoopbaarInterface>();
            entityManager.getTransaction().begin();

            var stock = 0;

            for(VerkoopbaarInterface verkoopbaar: verkoopbaarLijst){
                if(!verkochteVerkoopbaar.contains(verkoopbaar)){
                    verkochteVerkoopbaar.add(verkoopbaar);
                    stock = verkoopbaar.getStock();
                }

                if(stock > 0){
                    verkoopbaar.setTempStock(stock-1);
                    stock = stock - 1;

                    System.out.println("hier");
                    
                    if(verkoopbaar.getClass().isAssignableFrom(Game.class)){
                        factuur = new Factuur(0,user,verkoopbaar.getKostPrijs(),(Game)verkoopbaar,null, verkoopbaar.getWinkel());
                    } 

                    else if(verkoopbaar.getClass().isAssignableFrom(Extra.class)){
                        factuur = new Factuur(0,user ,verkoopbaar.getKostPrijs(), null,(Extra) verkoopbaar, verkoopbaar.getWinkel());
                    }
                       
                    entityManager.merge(user);
                    entityManager.persist(factuur);
                }
                else{
                    entityManager.getTransaction().rollback();
                    throw new Exception("more items selected than avaible");
                }
            }
            
            for(VerkoopbaarInterface verkoopbaar: verkochteVerkoopbaar){
                System.out.println("hier2");
                verkoopbaar.setTempToStock();
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

    public void checkIfUserExistWithEmail(String email) throws Exception{
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(User.class);
        var root = query.from(User.class);

        var predicateEmail = criteriaBuilder.equal(root.get("email"), email);

        var result = entityManager.createQuery(query.where(predicateEmail)).getResultList();

        if(!result.isEmpty()){
            throw new Exception("user already exist");
        }
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

    public List<Factuur> getFactuurForUser(User user){
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Factuur.class);
        var root = query.from(Factuur.class);

        var predicateUser = criteriaBuilder.equal(root.get("user"), user);
        var result = entityManager.createQuery(query.where(predicateUser)).getResultList();
        return result;
    }

    public List<Game> getGameByNameForUser(User user,String name){
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(User.class);

        var root = query.from(User.class);

        var predicateUser = criteriaBuilder.equal(root.get("userId"), user.getUserId());

        var result = entityManager.createQuery(query.where(predicateUser)).getResultList();

        var gameList = new ArrayList<Game>();

        for(Game game: result.get(0).getUitgeleendeGames()){
            if(game.getNaam().equals(name)){
                gameList.add(game);
            }
        }
        //TODO: bespreken met edmond
        System.out.println(gameList.size());
        return gameList;
    }
}
