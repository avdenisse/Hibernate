package org.example.practicahibernate.dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.practicahibernate.database.JPAUtil;
import org.example.practicahibernate.model.Libro;
import static org.example.practicahibernate.database.JPAUtil.getEntityManagerFactory;

import java.util.List;
public class LibroDAO {
    public void guardar(Libro libro){
        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            //Iniciar la transaccion en PostgreSQL
            tx.begin();
            //Decirle a Hibernate que prepare la persistencia del objeto libro
            em.persist(libro);
            //Confirmar los cambios en la base de datos
            tx.commit();
        }catch(Exception e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }


    public List<Libro> listar(){
        EntityManager em = getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();
        }finally {
            em.close();
        }
    }
}
