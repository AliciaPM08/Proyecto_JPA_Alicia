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
import java.util.Calendar;
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

    //Registrar Usuarios
    public void registrarUsuario(Usuario usuario) {
        usuarioDAO.insert(usuario);
    }

    //Registrar libros
    public void registrarLibro(Libro libro) {
        libroDAO.insert(libro);
    }

    //Registrar Ejemplares
    public void registrarEjemplar(Ejemplar ejemplar) {
        ejemplarDAO.insert(ejemplar);
    }

    //Contar ejemplares disponibles
    public long contarDisponibles(String isbn){
        return ejemplarDAO.countDisponibles(isbn);
    }

    //Registro de prestamos
    public void registrarPrestamo(Prestamo prestamo) {
        Usuario usuario= prestamo.getUsuario();

        //Validacion para usuario => si tiene tres prestamso no puede hacer mas
        long prestamsoActivos = prestamoDAO.findByUsuario(usuario).stream().filter(p -> p.getFechaDevolucion()== null).count();
        if(prestamsoActivos >= 3){
            throw  new RuntimeException("El usuario no puede realizar mas prestamos");
        }

        //Validacion para Ejemplar => Solo puede registrar prestamso si el ejemplar esta disponibles
        Ejemplar ejemplar = prestamo.getEjemplar();
        if(ejemplar.getEstado() != Ejemplar.Estado.Disponible){
            throw new RuntimeException("El ejemplar no esta disponible");
        }

        //Validacion para usuario => si tiene una penalizacion no puede registrar prestamos
        if(usuario.getPenalizacionHasta() != null && usuario.getPenalizacionHasta().after(new Date(System.currentTimeMillis()))) {
            throw new RuntimeException("El usuario tiene una penalizacion actualmente");
        }

        prestamo.setFechaInicio(new Date(System.currentTimeMillis()));
        ejemplar.setEstado(Ejemplar.Estado.Prestado);
        ejemplarDAO.insert(ejemplar);
        prestamoDAO.insert(prestamo);
    }

    //Registra las devoluciones
    public void registrarDevoluciones(long id){
        Prestamo prestamo= prestamoDAO.findById(id);
        Ejemplar ejemplar= prestamo.getEjemplar();
        ejemplar.setEstado(Ejemplar.Estado.Disponible);
        ejemplarDAO.insert(ejemplar);

        prestamo.setFechaDevolucion(new Date(System.currentTimeMillis()));

        if(prestamo.getFechaDevolucion().after(getFechaLimite(prestamo.getFechaInicio()))){
            Usuario usuario = prestamo.getUsuario();
            Date fechalimite= getFechaLimite(prestamo.getFechaInicio());
            long penalizaciones= prestamoDAO.countLateReturns(usuario, fechalimite);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(prestamo.getFechaDevolucion());
            calendar.add(Calendar.DAY_OF_MONTH, (int) penalizaciones * 15);
            usuario.setPenalizacionHasta(new java.util.Date(calendar.getTimeInMillis()));
            usuarioDAO.insert(usuario);
        }
        prestamoDAO.insert(prestamo);
    }

    //Metodo que calcula la fecha limite
    private Date getFechaLimite(Date fechaInicio){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaInicio);
        calendar.add(Calendar.DAY_OF_MONTH, 15);
        return new Date(calendar.getTimeInMillis());
    }

    //Busca Usuario por el dni
    public Usuario buscarUsuarioPorDni(String dni) {
        return usuarioDAO.findByDni(dni);
    }

    //Busca el ejemplar por ID
    public Ejemplar buscarEjemplarPorID(Long id){
        return ejemplarDAO.findById(id);
    }

    //Busca libro por el isbn
    public Libro buscarLibroPorIsbn(String isbn){
        return libroDAO.findById(isbn);
    }

    //Lista lso prestamos de un usuario
    public List<Prestamo> verPrestamos(Usuario usuario){
        if(usuario.getTipo() == Usuario.TipoUsuario.administrador){
            return em.createQuery("SELECT p FROM Prestamo p", Prestamo.class).getResultList();
        }else{
            return prestamoDAO.findByUsuario(usuario);
        }
    }

    //Cierre de flujos
    public void close() {
        em.close(); emf.close();
    }
}
