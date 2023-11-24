package be.kuleuven.dbproject.model.api;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.User;

public class UserApi {
    
    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;

    private DbConnection dbConnection;

    public UserApi(DbConnection dbConnection){
        this.dbConnection = dbConnection;
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
}
