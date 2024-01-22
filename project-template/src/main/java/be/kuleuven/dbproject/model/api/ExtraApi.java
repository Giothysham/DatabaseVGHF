package be.kuleuven.dbproject.model.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;
import be.kuleuven.dbproject.interfaces.VerkoopbaarApiInterface;
import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Uitgever;
import be.kuleuven.dbproject.model.User;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.enums.Type;
import be.kuleuven.dbproject.interfaces.VerkoopbaarInterface;

public class ExtraApi implements VerkoopbaarApiInterface {

    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;
    
    private Type searchType;

    private Winkel searchWinkel;

    private Uitgever searchUitgever;

    private User user;
    
    public ExtraApi(DbConnection dbConnection,User user){
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

    public Uitgever getSearchUitgever() {
        return this.searchUitgever;
    }

    public void clearSearchQuerry(){
        searchType = null;
        searchWinkel = null;
    }
    
    public List<VerkoopbaarInterface> getVerkoopbaarVoorUser(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);
        List<Extra> extraList = new ArrayList<>();

        if(user.getBevoegdheid() == 1){
            var select = query.select(root); 
            extraList = entityManager.createQuery(select).getResultList();
        }
        else{    
            query.where(criteriaBuilder.greaterThan(root.get("stock"), 0));
            extraList = entityManager.createQuery(query).getResultList();
        }

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
    
    public void postVerkoopbaar(VerkoopbaarInterface extra) throws Exception{
        entityManager.getTransaction().begin();
        try{
            entityManager.persist(extra);
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            throw new Exception("kan extra niet aanmaken");
        }
    }

    public void deleteVerkoopbaar (List<VerkoopbaarInterface> extras){
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
        else if(filterValue.getClass() == Uitgever.class){
            searchUitgever = (Uitgever) filterValue;
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

        if(searchUitgever != null){
            querryFilterList.add(criteriaBuilder.equal(root.get("uitgever"), searchUitgever));
        }

        if(naam != null){
            querryFilterList.add( criteriaBuilder.equal(root.get("naam"), naam));
        }

        if(user.getBevoegdheid() == 0 ){
            querryFilterList.add(criteriaBuilder.greaterThan(root.get("stock"), 0));
        }

        Predicate predicate = criteriaBuilder.and(querryFilterList.toArray(new Predicate[querryFilterList.size()]));

        var result = entityManager.createQuery(query.where(predicate)).getResultList();
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
        else if(filterValue.getClass() == Uitgever.class){
            searchUitgever = null;
        }
    }

    public boolean gebruiktUitgever(Uitgever uitgever){
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);

        query.where(criteriaBuilder.equal(root.get("uitgever"), uitgever));
        var result = entityManager.createQuery(query).getResultList();

        if(result.isEmpty()){
            return false;
        } else{
            return true;
        }
    }

    public boolean gebruiktWinkel(Winkel winkel) {
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Extra.class);
        var root = query.from(Extra.class);

        query.where(criteriaBuilder.equal(root.get("winkel"), winkel));
        var result = entityManager.createQuery(query).getResultList();

        if(result.isEmpty()){
            return false;
        } else{
            return true;
        }
        
    }

    public void updateVerkoopbaar(VerkoopbaarInterface verkoopbaar) throws Exception {
        entityManager.getTransaction().begin();
        try{
            entityManager.merge(verkoopbaar);
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            throw new Exception("extra kan niet geupdate worden");
        }
    }
}
