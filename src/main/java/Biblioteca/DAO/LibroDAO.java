package Biblioteca.DAO;

import Biblioteca.DTOS.Libro;
import jakarta.persistence.EntityManager;

public class LibroDAO {
    private EntityManager em;

    public LibroDAO(EntityManager em) {
        this.em = em;
    }

    //Insertar Libros
    public void insert(Libro libro) {
        em.getTransaction().begin();
        em.persist(libro);
        em.getTransaction().commit();
    }

    //Encontrar Libros mediante el ISBN
    public Libro findById(String isbn) {
        return em.find(Libro.class, isbn);
    }
}
