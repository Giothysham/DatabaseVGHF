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
    
    private void createWinkel() {
        Winkel winkel1 = new Winkel(0, 0, "winkelWandel straat", "diest", "3290", "vlaams-brabant","belgie");
        Winkel winkel2 = new Winkel(0, 0, "straat 3", "antwerpen", "3290", "antwerpen","belgie");
        Winkel winkel3 = new Winkel(0, 0, "straat doet raar", "brussel", "3290", "brussel","belgie");

        entityManager.getTransaction().begin();
        entityManager.persist(winkel1);
        entityManager.persist(winkel2);
        entityManager.persist(winkel3);
        entityManager.getTransaction().commit();
    }

    public List<Winkel> getWinkels(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Winkel.class);
        var root = query.from(Winkel.class);
        var select = query.select(root);
        
        return entityManager.createQuery(select).getResultList();
    }
}
