package be.kuleuven.dbproject.model.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import be.kuleuven.dbproject.model.Genre;

public class GenreApi {

    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;
    
    public GenreApi(DbConnection dbConnection){
        sessionFactory = dbConnection.getsessionFactory();
        entityManager = dbConnection.getEntityManager();
    }

    // private void createGenre(){
    //     Genre genre1 = new Genre(0, "Real-time strategy", "test niet goed");
    //     Genre genre2 = new Genre(0, "Fixed shooter", "test niet goed");
    //     Genre genre3 = new Genre(0, "FPS", "test niet goed");
    //     Genre genre4 = new Genre(0, "Action", "test niet goed");

    //     entityManager.getTransaction().begin();
    //     entityManager.persist(genre1);
    //     entityManager.persist(genre2);
    //     entityManager.persist(genre3);
    //     entityManager.persist(genre4);
    //     entityManager.getTransaction().commit();
    // }

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
            //TODO: throw exception
            return null;
        }
        else{
            return result.get(0);
        }

    }

    public void postGenre(Genre genre){
        entityManager.getTransaction().begin();
        entityManager.persist(genre);
        entityManager.getTransaction().commit();
    }
}
