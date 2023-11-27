package be.kuleuven.dbproject.model.api;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


import java.util.List;

import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Uitgever;

public class UitgeverApi {

    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;

    public UitgeverApi(DbConnection dbConnection){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
    }
    

    public List<Uitgever> getUitgevers(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Uitgever.class);
        var root = query.from(Uitgever.class);
        var select = query.select(root);
        
        return entityManager.createQuery(select).getResultList();
    }
}
