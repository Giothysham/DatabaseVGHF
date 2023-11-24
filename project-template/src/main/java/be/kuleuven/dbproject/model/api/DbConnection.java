package be.kuleuven.dbproject.model.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import be.kuleuven.dbproject.model.Genre;
import be.kuleuven.dbproject.model.Winkel;

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
            //createGenre();

        }
    }  
    
    public EntityManager getEntityManager(){
        return entityManager;
    }

    public EntityManagerFactory getsessionFactory(){
        return sessionFactory;
    }
}
