package Biblioteca.SERVICIOS;

import Biblioteca.DAO.EjemplarDAO;
import Biblioteca.DAO.LibroDAO;
import Biblioteca.DAO.PrestamoDAO;
import Biblioteca.DAO.UsuarioDAO;
import Biblioteca.DTOS.Ejemplar;
import Biblioteca.DTOS.Libro;
import Biblioteca.DTOS.Prestamo;
import Biblioteca.DTOS.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.Date;
import java.util.List;

public class Biblioteca {
    private EntityManager em;
    private EntityManagerFactory emf;
    private UsuarioDAO usuarioDAO;
    private LibroDAO libroDAO;
    private EjemplarDAO ejemplarDAO;
    private PrestamoDAO prestamoDAO;

    public Biblioteca() {
        emf = Persistence.createEntityManagerFactory("Biblioteca");
        em = emf.createEntityManager();
        usuarioDAO = new UsuarioDAO(em);
        libroDAO = new LibroDAO(em);
        ejemplarDAO = new EjemplarDAO(em);
        prestamoDAO = new PrestamoDAO(em);
    }

    public void registrarUsuario(Usuario usuario) {
        usuarioDAO.insert(usuario);
    }

    public void registrarLibro(Libro libro) {
        libroDAO.insert(libro);
    }

    public void registrarEjemplar(Ejemplar ejemplar) {
        ejemplarDAO.insert(ejemplar);
    }

    public void registrarPrestamo(Prestamo prestamo) {
        Usuario usuario= prestamo.getUsuario();
        List<Prestamo> prestamosActivos= prestamoDAO.findByUsuario(usuario);

        int prestamsoSinDevolver= 0;
        for (Prestamo p: prestamosActivos) {
            if(p.getFechaDevolucion() == null){
                prestamsoSinDevolver++;
            }
        }

        if (prestamsoSinDevolver >= 3 ||
                (usuario.getPenalizacionHasta() != null && usuario.getPenalizacionHasta().after(new Date(System.currentTimeMillis())))) {
            throw new RuntimeException("El usuario no puede realizar mas prestamos");
        }

        Ejemplar ejemplar= prestamo.getEjemplar();
        if(!"Disponible".equals(ejemplar.getEstado())){
            throw new RuntimeException("Elejemplar no esta disponible");
        }

        prestamo.setFechaInicio(new Date(System.currentTimeMillis()));
        ejemplar.setEstado("Prestado");
        ejemplarDAO.insert(ejemplar);
        prestamoDAO.insert(prestamo);
    }

    public void registrarDevoluciones(long id){
        Prestamo prestamo= prestamoDAO.findById(id);
        Ejemplar ejemplar= prestamo.getEjemplar();
        ejemplar.setEstado("Disponible");
        ejemplarDAO.insert(ejemplar);

        prestamo.setFechaDevolucion(new Date(System.currentTimeMillis()));
        prestamoDAO.insert(prestamo);
        }
        public List<Prestamo> verPrestamos(Usuario usuario) {
        if (usuario.getTipo() == Usuario.TipoUsuario.administrador) {
            return em.createQuery("SELECT p FROM Prestamo p", Prestamo.class).getResultList();
        } else { return prestamoDAO.findByUsuario(usuario);
        }
    }
    public Usuario buscarUsuarioPorDni(String dni) {
        return usuarioDAO.findByDni(dni);
    }
    public void close() {
        em.close(); emf.close();
    }

    public Ejemplar buscarEjemplarPorID(Long id){
        return ejemplarDAO.findById(id);
    }

    public List<Ejemplar> buscarEjemplaresPorIsbn(String isbn) {
        return ejemplarDAO.findByIsbn(isbn);
    }
}
