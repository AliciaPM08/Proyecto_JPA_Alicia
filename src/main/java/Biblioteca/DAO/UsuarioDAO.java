package Biblioteca.DAO;

import Biblioteca.DTOS.Usuario;
import jakarta.persistence.EntityManager;

public class UsuarioDAO {
    private EntityManager em;

    public UsuarioDAO(EntityManager em) {
        this.em = em;
    }

    //Insertar Usuario
    public void insert(Usuario usuario) {
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
    }

    //Encontrar Usuario mediante el ID
    public Usuario findById(int id) {
        return em.find(Usuario.class, id);
    }

    //Encontrar Usuario pr DNI
    public Usuario findByDni(String dni) {
        return em.createQuery("SELECT u FROM Usuario u WHERE u.dni= :dni", Usuario.class)
                .setParameter("dni", dni).getSingleResult();
    }
}
