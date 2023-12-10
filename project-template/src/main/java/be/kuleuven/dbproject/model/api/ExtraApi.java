package be.kuleuven.dbproject.model.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;

import be.kuleuven.dbproject.controller.VerkoopbaarController;
import be.kuleuven.dbproject.interfaces.VerkoopbaarApiInterface;
import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.enums.Type;
import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;

public class ExtraApi implements VerkoopbaarApiInterface{

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
    
    public List<VerkoopbaarInterface> getVerkoopbaar(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);
        var select = query.select(root);

        List<Extra> extraList = entityManager.createQuery(select).getResultList();
        List<VerkoopbaarInterface> verkoopbaarList = new ArrayList<>();
     
        for(Extra extra : extraList){
            verkoopbaarList.add((VerkoopbaarInterface) extra);
        }
        
        return verkoopbaarList;
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
    
    public void postVerkoopbaar(VerkoopbaarInterface extra){
        entityManager.getTransaction().begin();
        entityManager.persist(extra);
        entityManager.getTransaction().commit();
    }

    public void deleteVerkoopbaar (List<VerkoopbaarInterface> extras){ //try rond gooien om mislopende transactie te rollbacken4
        try{ 
            if(extras.size() > 0){
                entityManager.getTransaction().begin();
                for(VerkoopbaarInterface extra: extras){ //TODO: naageminging misschine fixen, zie ook GameApi
                    var delete = entityManager.find(Extra.class, extra.getID());
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
            System.out.println(searchType.name());
        }
        else if(filterValue.getClass() == Winkel.class){
            searchWinkel = (Winkel) filterValue;
            System.out.println(((Winkel) filterValue).getSmallAdress());
        }
    }

    public List<VerkoopbaarInterface> searchVerkoopbaarByFilters(String naam){

        List<Predicate> querryFilterList = new ArrayList<Predicate>();

        var criteriaBuilder = sessionFactory.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);

        if(searchType != null){
            System.out.println("searching for console: "+searchType);
           querryFilterList.add(criteriaBuilder.equal(root.get("type"), searchType));
        }
        
        if(searchWinkel != null){
            System.out.println("searching for winkel: "+searchWinkel);
            querryFilterList.add(criteriaBuilder.equal(root.get("winkel"), searchWinkel));
        }

        if(naam != null){
            System.out.println("searching for name: "+naam);
            querryFilterList.add( criteriaBuilder.equal(root.get("naam"), naam));
        }

        Predicate predicate = criteriaBuilder.and(querryFilterList.toArray(new Predicate[querryFilterList.size()]));

        var result = entityManager.createQuery(query.where(predicate)).getResultList();

        System.out.println(result);

        List<VerkoopbaarInterface> verkoopbaarList = new ArrayList<>();
     
        for(Extra extra : result){
            verkoopbaarList.add((VerkoopbaarInterface) extra);
        }

        return verkoopbaarList;
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
