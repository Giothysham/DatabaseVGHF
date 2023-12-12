package be.kuleuven.dbproject;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import be.kuleuven.dbproject.model.Extra;
import be.kuleuven.dbproject.model.Winkel;
import be.kuleuven.dbproject.model.api.DbConnection;
import be.kuleuven.dbproject.model.api.ExtraApi;


public class ExtraApiTest {
    
    @Test
    public void testGetExtras() {
        // Create a mock EntityManager
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        
        // Create a mock CriteriaBuilder
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        
        // Create a mock Query
        CriteriaQuery<Extra> query = Mockito.mock(CriteriaQuery.class);
        
        // Create a mock Root
        Root<Extra> root = Mockito.mock(Root.class);
        
        // Create a mock TypedQuery
        TypedQuery<Extra> typedQuery = Mockito.mock(TypedQuery.class);
        
        // Create a mock List
        List<Extra> expectedExtras = new ArrayList<Extra>();
        expectedExtras.add(new Extra());
        
        // Mock the necessary method calls
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Extra.class)).thenReturn(query);
        Mockito.when(query.from(Extra.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(entityManager.createQuery(query)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedExtras);
        
        // Create an instance of ExtraApi and set the entityManager
        DbConnection dbConnection = Mockito.mock(DbConnection.class);

        ExtraApi extraApi = new ExtraApi(dbConnection);
        
        // Call the getExtras method
        List<Extra> actualExtras = extraApi.getExtras();
        
        // Assert that the actualExtras is equal to the expectedExtras
        Assertions.assertEquals(expectedExtras, actualExtras);
    }

    @Test
    public void testGetExtraById() throws Exception {
        // Create a mock EntityManager
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        
        // Create a mock CriteriaBuilder
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        
        // Create a mock Query
        CriteriaQuery<Extra> query = Mockito.mock(CriteriaQuery.class);
        
        // Create a mock Root
        Root<Extra> root = Mockito.mock(Root.class);
        
        // Create a mock TypedQuery
        TypedQuery<Extra> typedQuery = Mockito.mock(TypedQuery.class);
        
        // Create a mock List
        List<Extra> expectedExtras = new ArrayList<Extra>();
        expectedExtras.add(new Extra());

        // Mock the necessary method calls
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Extra.class)).thenReturn(query);
        Mockito.when(query.from(Extra.class)).thenReturn(root);
        Mockito.when(query.where(Mockito.<Predicate>any())).thenReturn(query); // Specify the type of the expression
        Mockito.when(entityManager.createQuery(query)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedExtras);

        // Create an instance of ExtraApi and set the entityManager
        DbConnection dbConnection = Mockito.mock(DbConnection.class);
        ExtraApi extraApi = new ExtraApi(dbConnection);
        
        // Call the getExtraById method
        String ID = "123";
        Extra actualExtra = extraApi.getExtraById(ID);
        
        // Assert that the actualExtra is equal to the expectedExtra
        Assertions.assertEquals(expectedExtras.get(0), actualExtra);
    }

    @Test
    public void testGetExtraById_ThrowsException() {
        // Create a mock EntityManager
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        
        // Create a mock CriteriaBuilder
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        
        // Create a mock Query
        CriteriaQuery<Extra> query = Mockito.mock(CriteriaQuery.class);
        
        // Create a mock Root
        Root<Extra> root = Mockito.mock(Root.class);
        
        // Create a mock TypedQuery
        TypedQuery<Extra> typedQuery = Mockito.mock(TypedQuery.class);
        
        // Create an empty List
        List<Extra> expectedExtras = new ArrayList<Extra>();
        
        // Mock the necessary method calls
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Extra.class)).thenReturn(query);
        Mockito.when(query.from(Extra.class)).thenReturn(root);
        Mockito.when(query.where(Mockito.<Predicate>any())).thenReturn(query); // Specify the type of the expression
        Mockito.when(entityManager.createQuery(query)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedExtras);
        
        // Create an instance of ExtraApi and set the entityManager
        DbConnection dbConnection = Mockito.mock(DbConnection.class);
        ExtraApi extraApi = new ExtraApi(dbConnection);
        
        // Call the getExtraById method with an invalid ID
        String invalidID = "456";
        Assertions.assertThrows(Exception.class, () -> {
            extraApi.getExtraById(invalidID);
        });
    }

    @Test
    public void testDeleteExtra() {
        // Create a mock EntityManager
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        
        // Create a mock Transaction
        EntityTransaction transaction = Mockito.mock(EntityTransaction.class);
        
        // Create a mock List of Extras
        List<Extra> extras = new ArrayList<>();
        extras.add(new Extra());
        
        // Mock the necessary method calls
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        
        // Create an instance of ExtraApi and set the entityManager
        DbConnection dbConnection = Mockito.mock(DbConnection.class);
        ExtraApi extraApi = new ExtraApi(dbConnection);
        
        // Call the deleteExtra method
        extraApi.deleteExtra(extras);
        
        // Verify that the transaction was started and committed
        Mockito.verify(transaction, Mockito.times(1)).begin();
        Mockito.verify(transaction, Mockito.times(1)).commit();
    }

    @Test
    public void testDeleteExtra_EmptyList() {
        // Create a mock EntityManager
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        
        // Create a mock Transaction
        EntityTransaction transaction = Mockito.mock(EntityTransaction.class);
        
        // Create an empty List of Extras
        List<Extra> extras = new ArrayList<>();
        
        // Mock the necessary method calls
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        
        // Create an instance of ExtraApi and set the entityManager
        DbConnection dbConnection = Mockito.mock(DbConnection.class);
        ExtraApi extraApi = new ExtraApi(dbConnection);
        
        // Call the deleteExtra method
        extraApi.deleteExtra(extras);
        
        // Verify that the transaction was not started or committed
        Mockito.verify(transaction, Mockito.never()).begin();
        Mockito.verify(transaction, Mockito.never()).commit();
    }

    @Test
    public void testDeleteExtra_Exception() {
        // Create a mock EntityManager
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        
        // Create a mock Transaction
        EntityTransaction transaction = Mockito.mock(EntityTransaction.class);
        
        // Create a mock List of Extras
        List<Extra> extras = new ArrayList<>();
        extras.add(new Extra());
        
        // Mock the necessary method calls
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.doThrow(new RuntimeException()).when(entityManager).remove(Mockito.any());
        
        // Create an instance of ExtraApi and set the entityManager
        DbConnection dbConnection = Mockito.mock(DbConnection.class);
        ExtraApi extraApi = new ExtraApi(dbConnection);
        
        // Call the deleteExtra method
        extraApi.deleteExtra(extras);
        
        // Verify that the transaction was rolled back
        Mockito.verify(transaction, Mockito.times(1)).rollback();
        Mockito.verify(transaction, Mockito.never()).commit();
    }

    @Test
    public void testCreatSearchQuerryWithType() {
        // Create an instance of ExtraApi
        DbConnection dbConnection = Mockito.mock(DbConnection.class);
        ExtraApi extraApi = new ExtraApi(dbConnection);
        
        var winkel = Mockito.mock(Winkel.class);
        
        // Call the creatSearchQuerry method with the Type object
        extraApi.creatSearchQuerry(winkel);
        
        // Assert that the searchType is equal to the Type object
        Assertions.assertEquals(winkel, extraApi.getSearchWinkel());
    }

    @Test
    public void testCreatSearchQuerryWithWinkel() {
        // Create an instance of ExtraApi
        DbConnection dbConnection = Mockito.mock(DbConnection.class);
        ExtraApi extraApi = new ExtraApi(dbConnection);
        
        // Create a Winkel object
        Winkel winkel = new Winkel();
        
        // Call the creatSearchQuerry method with the Winkel object
        extraApi.creatSearchQuerry(winkel);
        
        // Assert that the searchWinkel is equal to the Winkel object
        Assertions.assertEquals(winkel, extraApi.getSearchWinkel());
    }

    @Test
    public void testCreatSearchQuerryWithInvalidType() {
        // Create an instance of ExtraApi
        DbConnection dbConnection = Mockito.mock(DbConnection.class);
        ExtraApi extraApi = new ExtraApi(dbConnection);
        
        // Create an object of an invalid type
        Object invalidObject = new Object();
        
        // Call the creatSearchQuerry method with the invalid object
        extraApi.creatSearchQuerry(invalidObject);
        
        // Assert that the searchType and searchWinkel are null
        Assertions.assertNull(extraApi.getSearchType());
        Assertions.assertNull(extraApi.getSearchWinkel());
    }

    @Test
    public void testSearchExtraByFilters_WithFiltersAndName() {
        // Create a mock CriteriaBuilder
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        
        // Create a mock CriteriaQuery
        CriteriaQuery<Extra> query = Mockito.mock(CriteriaQuery.class);
        
        // Create a mock Root
        Root<Extra> root = Mockito.mock(Root.class);
        
        // Create a mock TypedQuery
        TypedQuery<Extra> typedQuery = Mockito.mock(TypedQuery.class);
        
        // Create a mock List
        List<Extra> expectedExtras = new ArrayList<Extra>();
        expectedExtras.add(new Extra());

        // Mock the necessary method calls
        Mockito.when(sessionFactory.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Extra.class)).thenReturn(query);
        Mockito.when(query.from(Extra.class)).thenReturn(root);
        Mockito.when(criteriaBuilder.equal(Mockito.any(), Mockito.any())).thenReturn(Mockito.mock(Predicate.class));
        Mockito.when(criteriaBuilder.and(Mockito.<Predicate>anyVararg())).thenReturn(Mockito.mock(Predicate.class));
        Mockito.when(entityManager.createQuery(query)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedExtras);

        // Set the searchType and searchWinkel
        ExtraApi extraApi = new ExtraApi(dbConnection);
        extraApi.setSearchType("type");
        extraApi.setSearchWinkel(Mockito.mock(Winkel.class));
        
        // Call the searchExtraByFilters method
        String naam = "extraName";
        List<Extra> actualExtras = extraApi.searchExtraByFilters(naam);
        
        // Assert that the actualExtras is equal to the expectedExtras
        Assertions.assertEquals(expectedExtras, actualExtras);
    }

    @Test
    public void testSearchExtraByFilters_WithFiltersAndName_NoResult() {
        // Create a mock CriteriaBuilder
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        
        // Create a mock CriteriaQuery
        CriteriaQuery<Extra> query = Mockito.mock(CriteriaQuery.class);
        
        // Create a mock Root
        Root<Extra> root = Mockito.mock(Root.class);
        
        // Create a mock TypedQuery
        TypedQuery<Extra> typedQuery = Mockito.mock(TypedQuery.class);
        
        // Create an empty List
        List<Extra> expectedExtras = new ArrayList<Extra>();
        
        // Mock the necessary method calls
        Mockito.when(sessionFactory.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Extra.class)).thenReturn(query);
        Mockito.when(query.from(Extra.class)).thenReturn(root);
        Mockito.when(criteriaBuilder.equal(Mockito.any(), Mockito.any())).thenReturn(Mockito.mock(Predicate.class));
        Mockito.when(criteriaBuilder.and(Mockito.<Predicate>anyVararg())).thenReturn(Mockito.mock(Predicate.class));
        Mockito.when(entityManager.createQuery(query)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedExtras);

        // Set the searchType and searchWinkel
        ExtraApi extraApi = new ExtraApi(dbConnection);
        extraApi.setSearchType("type");
        extraApi.setSearchWinkel(Mockito.mock(Winkel.class));
        
        // Call the searchExtraByFilters method
        String naam = "extraName";
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            extraApi.searchExtraByFilters(naam);
        });
    }

    @Test
    public void testSearchExtraByFilters_WithoutFiltersAndName() {
        // Create a mock CriteriaBuilder
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        
        // Create a mock CriteriaQuery
        CriteriaQuery<Extra> query = Mockito.mock(CriteriaQuery.class);
        
        // Create a mock Root
        Root<Extra> root = Mockito.mock(Root.class);
        
        // Create a mock TypedQuery
        TypedQuery<Extra> typedQuery = Mockito.mock(TypedQuery.class);
        
        // Create a mock List
        List<Extra> expectedExtras = new ArrayList<Extra>();
        expectedExtras.add(new Extra());

        // Mock the necessary method calls
        Mockito.when(sessionFactory.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Extra.class)).thenReturn(query);
        Mockito.when(query.from(Extra.class)).thenReturn(root);
        Mockito.when(entityManager.createQuery(query)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(expectedExtras);

        // Call the searchExtraByFilters method without setting any filters
        String naam = "extraName";
        List<Extra> actualExtras = extraApi.searchExtraByFilters(naam);
        
        // Assert that the actualExtras is equal to the expectedExtras
        Assertions.assertEquals(expectedExtras, actualExtras);
    }

}