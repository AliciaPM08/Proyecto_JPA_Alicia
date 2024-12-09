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
    public Ejemplar findById(int id) {
        return em.find(Ejemplar.class, id);
    }

    //Encontrar ejmeplar mediante el libro
    public List<Ejemplar> findbyLibro(Libro libro) {
        return  em.createQuery("SELECT e FROM Ejemplar e WHERE e.libro = :libro", Ejemplar.class)
                .setParameter("libro", libro).getResultList();
    }
}
