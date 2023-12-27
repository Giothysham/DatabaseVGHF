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
        //var predicateWachtwoord = criteriaBuilder.equal(root.get("wachtwoord"), wachtwoord);

        //var predicateAnd = criteriaBuilder.and(predicateEmail, predicateWachtwoord); TODO: geen idee als dit een goeie fix is

        User result = entityManager.createQuery(query.where(predicateEmail)).getResultList().get(0);
        System.out.println("ww: " + result.getAchternaam() + "--------------------------------------");
        if(true){ //result.getWachtwoord().equals(wachtwoord) TODO: FIX PLS
            return result;
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
                    //fix => vragen aan wouter
                    verkoopbaar.setTempStock(stock-1);
                    stock = stock - 1;
                    
                    if(verkoopbaar.getClass().isAssignableFrom(Game.class)){
                        factuur = new Factuur(0,user,verkoopbaar.getKostPrijs(),(Game)verkoopbaar,null, verkoopbaar.getWinkel());
                    } 

                    else if(verkoopbaar.getClass().isAssignableFrom(Extra.class)){
                        factuur = new Factuur(0,user ,verkoopbaar.getKostPrijs(), null,(Extra) verkoopbaar, verkoopbaar.getWinkel());
                    }
                       
                    
                    entityManager.persist(factuur);
                }
                else{
                    //over gaan werkt maar als er dan een element verwijderd wordt wordt het programma boos. 
                    //veranderingen blijven bestaan zelfs na de rol back => vragen aan wouter. => game wordt zwz geupdate => roll back fixed dit niet

                    entityManager.getTransaction().rollback();
                    throw new Exception("more items selected than avaible");
                }
            }

            for(VerkoopbaarInterface verkoopbaar: verkochteVerkoopbaar){
                verkoopbaar.setTempToStock();
                if(verkoopbaar.getClass().isAssignableFrom(Game.class)){
                    user.getUitgeleendeGames().add((Game)verkoopbaar);
                }
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
