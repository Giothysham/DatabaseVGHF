package be.kuleuven.dbproject.model.api;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DbConnection {
    
    private EntityManagerFactory sessionFactory;

    private EntityManager entityManager;


    public DbConnection() {

        //make different class for the game and so on conection
        if(sessionFactory == null || entityManager == null || !sessionFactory.isOpen()){
            sessionFactory = Persistence.createEntityManagerFactory("be.kuleuven.dbproject.model");
            entityManager = sessionFactory.createEntityManager();

            //createWinkel();
            //createGames();
            //createGenre(); TODO: DELETE

        }
    }  
    
    public EntityManager getEntityManager(){
        return entityManager;
    }

    public EntityManagerFactory getsessionFactory(){
        return sessionFactory;
    }
}
