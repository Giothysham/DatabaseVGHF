package be.kuleuven.dbproject.model.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jvnet.fastinfoset.EncodingAlgorithmException;

import be.kuleuven.dbproject.model.Genre;

public class GenreApi {

    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;
    
    public GenreApi(DbConnection dbConnection){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
    }

    public List<Genre> getGenres() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Genre.class);
        var root = query.from(Genre.class);
        var select = query.select(root);
        
        return entityManager.createQuery(select).getResultList();
    }

    public Genre getGenreByName(String naam){
        var criteriaBuilder = sessionFactory.getCriteriaBuilder();

        var query = criteriaBuilder.createQuery(Genre.class);
        var root = query.from(Genre.class);

        query.where(criteriaBuilder.equal(root.get("naam"), naam));

        var result = entityManager.createQuery(query).getResultList();

        if(result.size() > 1){
            return null;
        }
        else{
            return result.get(0);
        }

    }

    public void postGenre(Genre genre) throws Exception{
        entityManager.getTransaction().begin();
        try{
            entityManager.persist(genre);
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            throw new Exception("genre kan niet aangemaakt worden");
        }
    }

    public void deleteSelectedGenre(Genre genre) throws Exception{
        entityManager.getTransaction().begin();
        try{
            entityManager.remove(genre);
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            throw new Exception("genre kan niet verwijderd worden");
        }
    }
}
