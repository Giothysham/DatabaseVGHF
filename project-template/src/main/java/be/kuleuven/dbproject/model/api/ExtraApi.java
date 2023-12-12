package be.kuleuven.dbproject.model.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;

import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.enums.Type;

public class ExtraApi {

    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;
    
    private Type searchType;

    private Winkel searchWinkel;
    
    public ExtraApi(DbConnection dbConnection){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
    }

    public Type getSearchType() {
        return this.searchType;
    }

    public Winkel getSearchWinkel() {
        return this.searchWinkel;
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

    public <T> void creatSearchQuerry(T filterValue){
        if(filterValue.getClass() == Type.class){
            searchType = (Type) filterValue;
        }
        else if(filterValue.getClass() == Winkel.class){
            searchWinkel = (Winkel) filterValue;
        }
    }

    public List<Extra> searchExtraByFilters(String naam){

        List<Predicate> querryFilterList = new ArrayList<Predicate>();

        var criteriaBuilder = sessionFactory.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);

        if(searchType != null){
           querryFilterList.add(criteriaBuilder.equal(root.get("type"), searchType));
        }
        
        if(searchWinkel != null){
            querryFilterList.add(criteriaBuilder.equal(root.get("winkel"), searchWinkel));
        }

        if(naam != null){
            querryFilterList.add( criteriaBuilder.equal(root.get("naam"), naam));
        }

        Predicate predicate = criteriaBuilder.and(querryFilterList.toArray(new Predicate[querryFilterList.size()]));

        var result = entityManager.createQuery(query.where(predicate)).getResultList();
        if(result.size() > 0){
            return result;
        }
        else{
            throw new IllegalArgumentException("no extra found with filters and name: "+ naam);
        }

    }
    
    public <T> void removeFilterByClass(T filterValue) {
        if(filterValue.getClass() == Type.class){
            searchType = null;
        }
        else if(filterValue.getClass() == Winkel.class){
            searchWinkel = null;
        }
    }
}
