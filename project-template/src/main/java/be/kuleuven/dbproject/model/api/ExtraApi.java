package be.kuleuven.dbproject.model.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import be.kuleuven.dbproject.model.Extra;

public class ExtraApi {

    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;
    
    public ExtraApi(DbConnection dbConnection){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
    }
    
    public List<Extra> getExtras(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);
        var select = query.select(root);
        
        return entityManager.createQuery(select).getResultList();
    }
    
    public Extra getExtraById(String ID) throws Exception{
        System.out.println(ID);
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);

        query.where(criteriaBuilder.equal(root.get("extraID"), ID));
        var result = entityManager.createQuery(query).getResultList();
        
        if(result.size() > 0){
            return result.get(0);
        }
        else{
            throw new Exception("no extra found with id: "+ ID);
        }
    
    }
    
    public List<Extra> SearchExtraByName(String naam){

        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);

        query.where(criteriaBuilder.equal(root.get("naam"), naam));

        var result = entityManager.createQuery(query).getResultList();

        return result;
    }
    
    public void postExtra(Extra extra){
        entityManager.getTransaction().begin();
        entityManager.persist(extra);
        entityManager.getTransaction().commit();
    }

    public void deleteExtra(List<Extra> extras){ //try rond gooien om mislopende transactie te rollbacken4
        try{ 
            if(extras.size() > 0){
                entityManager.getTransaction().begin();
                for(Extra extra: extras){
                    var delete = entityManager.find(Extra.class, extra.getExtraID());
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
