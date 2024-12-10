package Biblioteca.DAO;

import Biblioteca.DTOS.Ejemplar;
import Biblioteca.DTOS.Libro;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EjemplarDAO {
    private EntityManager em;

    public EjemplarDAO(EntityManager em) {
        this.em = em;
    }

    //Insertar ejemplar
    public void insert(Ejemplar ejemplar) {
        em.getTransaction().begin();
        em.persist(ejemplar);
        em.getTransaction().commit();
    }

    //Encontrar ejemplar mediante id
    public Ejemplar findById(Long id) {
        return em.find(Ejemplar.class, id);
    }

    //Muestra los libros que estan disponibles
    public long countDisponibles(String isbn){
        return em.createQuery("SELECT COUNT(e) FROM Ejemplar e WHERE e.libro.isbn = :isbn AND e.estado= :estado", Long.class)
                .setParameter("isbn", isbn).setParameter("estado", Ejemplar.Estado.Disponible).getSingleResult();
    }
}
