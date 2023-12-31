package be.kuleuven.dbproject.model.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;

import be.kuleuven.dbproject.interfaces.VerkoopbaarApiInterface;
import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.enums.Type;
import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;

public class ExtraApi implements VerkoopbaarApiInterface {

    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;
    
    private Type searchType;

    private Winkel searchWinkel;

    private User user;
    
    public ExtraApi(DbConnection dbConnection, User user){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
        this.user = user;
    }

    public Type getSearchType() {
        return this.searchType;
    }

    public Winkel getSearchWinkel() {
        return this.searchWinkel;
    }
    
    public List<VerkoopbaarInterface> getVerkoopbaar(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);
    
        if(user.getBevoegdheid() == 1){ //testen met ergens user = null
            query.select(root);
        }
        else if(user.getBevoegdheid() == 0){
            query.where(criteriaBuilder.greaterThan(root.get("stock"), 0));
        }

        List<Extra> extraList = entityManager.createQuery(query).getResultList();
        List<VerkoopbaarInterface> verkoopbaarList = new ArrayList<>();
     
        for(Extra extra : extraList){
            verkoopbaarList.add((VerkoopbaarInterface) extra);
        }
        
        return verkoopbaarList;
    }
    
    public VerkoopbaarInterface getVerkoopbaarById(String ID) throws Exception{
        System.out.println(ID);
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);

        query.where(criteriaBuilder.equal(root.get("extraID"), ID));
        var result = entityManager.createQuery(query).getResultList();
        
        if(result.size() > 0){
            return (VerkoopbaarInterface) result.get(0);
        }
        else{
            throw new Exception("no extra found with id: "+ ID);
        }
    }
    
    public void postVerkoopbaar(VerkoopbaarInterface extra){
        entityManager.getTransaction().begin();
        entityManager.persist(extra);
        entityManager.getTransaction().commit();
    }

    public void deleteVerkoopbaar (List<VerkoopbaarInterface> extras){ //try rond gooien om mislopende transactie te rollbacken4
        try{ 
            if(extras.size() > 0){
                entityManager.getTransaction().begin();
                for(VerkoopbaarInterface extra: extras){
                    var delete = entityManager.find(Extra.class, extra.getID());
                    delete.setStock(0);
                    entityManager.merge(delete);
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

    public List<VerkoopbaarInterface> searchVerkoopbaarByFilters(String naam){

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

        if(user.getBevoegdheid() == 0 ){
            querryFilterList.add(criteriaBuilder.greaterThan(root.get("stock"), 0));
        }

        Predicate predicate = criteriaBuilder.and(querryFilterList.toArray(new Predicate[querryFilterList.size()]));

        var result = entityManager.createQuery(query.where(predicate)).getResultList();
        if(result.size() > 0){
            List<VerkoopbaarInterface> verkoopbaarList = new ArrayList<>();
     
            for(Extra extra : result){
                verkoopbaarList.add((VerkoopbaarInterface) extra);
            }
            return verkoopbaarList;
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
