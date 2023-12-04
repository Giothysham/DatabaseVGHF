package be.kuleuven.dbproject.model.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import be.kuleuven.dbproject.model.Winkel;

public class WinkelApi {
    
    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;
    
    public WinkelApi(DbConnection dbConnection){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
    }

    public List<Winkel> getWinkels(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Winkel.class);
        var root = query.from(Winkel.class);
        var select = query.select(root);
        
        return entityManager.createQuery(select).getResultList();
    }

    public Winkel getWinkelById(int id){
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Winkel.class);
        var root = query.from(Winkel.class);

        query.where(criteriaBuilder.equal(root.get("winkelID"), id));

        var result = entityManager.createQuery(query).getResultList();

        return result.get(0);
    }

    public void postWinkel(Winkel winkel){
        entityManager.getTransaction().begin();
        entityManager.persist(winkel);
        entityManager.getTransaction().commit();
    }
}
