package Biblioteca.DAO;

import Biblioteca.DTOS.Prestamo;
import Biblioteca.DTOS.Usuario;
import jakarta.persistence.EntityManager;

import java.sql.Date;
import java.util.List;

public class PrestamoDAO {
    private EntityManager em;

    public PrestamoDAO(EntityManager em) {
        this.em = em;
    }

    //Insertar Prestamos
    public void insert(Prestamo prestamo) {
        em.getTransaction().begin();
        em.persist(prestamo);
        em.getTransaction().commit();
    }

    //Encontrar prestamos mediante el ID
    public Prestamo findById(Long id) {
        return em.find(Prestamo.class, id);
    }

    //Encontrar prestamos mediante Usuarios
    public List<Prestamo> findByUsuario(Usuario usuario) {
        return em.createQuery("SELECT p FROM Prestamo p WHERE p.usuario = :usuario", Prestamo.class)
                .setParameter("usuario", usuario).getResultList();
    }

    //Cuenta las devoluciones tardias
    public long countLateReturns(Usuario usuario, Date fechaLimite) {
        return em.createQuery("SELECT COUNT(p) FROM Prestamo p WHERE p.usuario = :usuario AND p.fechaDevolucion > :fechaLimite", Long.class)
                .setParameter("usuario", usuario)
                .setParameter("fechaLimite", fechaLimite).getSingleResult();
    }
}
