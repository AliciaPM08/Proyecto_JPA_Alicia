package Biblioteca.DAO;

import Biblioteca.DTOS.Prestamo;
import Biblioteca.DTOS.Usuario;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PrestamoDAO {
    private EntityManager em;

    public PrestamoDAO(EntityManager em) {
        this.em = em;
    }

    //Insertar Prestamos
    public void insert(Prestamo prestamo){
        em.getTransaction().begin();
        em.persist(prestamo);
        em.getTransaction().commit();
    }

    //Encontrar prestamos mediante el ID
    public Prestamo findById(int id){
        return em.find(Prestamo.class, id);
    }

    //Encontrar prestamos mediante Usuarios
    public List<Prestamo> findByUsuario(Usuario usuario){
        return em.createQuery("SELECT p FROM Prestamo p WHERE p.usuario = :usuario", Prestamo.class)
                .setParameter("usuario", usuario).getResultList();
    }
}
