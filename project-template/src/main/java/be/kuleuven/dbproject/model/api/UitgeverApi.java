package be.kuleuven.dbproject.model.api;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


import java.util.List;

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


    public Uitgever getUitgeverById(Integer id) {
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Uitgever.class);
        var root = query.from(Uitgever.class);

        query.where(criteriaBuilder.equal(root.get("uitgeverID"), id));
        var result = entityManager.createQuery(query).getResultList();
        
        return result.get(0);
    }

    public Uitgever getUitgeverByName(String name){
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Uitgever.class);
        var root = query.from(Uitgever.class);

        query.where(criteriaBuilder.equal(root.get("naam"), name));
        var result = entityManager.createQuery(query).getResultList();
        
        return result.get(0);
    }

    public void postUitgever(Uitgever uitgever) throws Exception {
        entityManager.getTransaction().begin();
        try{
            entityManager.persist(uitgever);
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            throw new Exception("uitgever could not be created");
        }
    }

    public void deleteSelectedUitgever(Uitgever uitgever) throws Exception{
        
        entityManager.getTransaction().begin();
        try{
            entityManager.remove(uitgever);
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            throw new Exception("uitgever could not be deleted");
        }
    }
}
