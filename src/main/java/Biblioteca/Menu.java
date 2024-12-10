package Biblioteca;

import Biblioteca.DTOS.Usuario;
import Biblioteca.SERVICIOS.Biblioteca;
import Biblioteca.DTOS.Libro;
import Biblioteca.DTOS.Ejemplar;
import Biblioteca.DTOS.Prestamo;

import java.util.List;
import java.util.Scanner;

public class Menu {
        public static void main(String[] args) {
            Biblioteca service = new Biblioteca();
            Scanner scanner = new Scanner(System.in);

            Usuario usuarioActual = null;
            System.out.print("Ingrese su DNI: ");
            String dniActual = scanner.nextLine();
            usuarioActual = service.buscarUsuarioPorDni(dniActual);

            if (usuarioActual == null) {
                System.out.println("Usuario no encontrado. Saliendo del sistema");
                return;
            }

            // Menú
            int opcion = -1;
            while (opcion != 0) {
                System.out.println("Menú: ");
                if (usuarioActual.getTipo() == Usuario.TipoUsuario.administrador) {
                    System.out.println("1. Registrar Usuario");
                    System.out.println("2. Registrar Libro");
                    System.out.println("3. Registrar Ejemplar");
                    System.out.println("4. Registrar Préstamo");
                    System.out.println("5. Registrar Devolución");
                }
                System.out.println("6. Ver Préstamos");
                System.out.println("0. Salir");
                System.out.print("Opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        if (usuarioActual.getTipo() == Usuario.TipoUsuario.administrador) {
                            // Registrar Usuario
                            System.out.print("DNI: ");
                            String dni = scanner.nextLine();
                            System.out.print("Nombre: ");
                            String nombre = scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Password: ");
                            String password = scanner.nextLine();
                            System.out.print("Tipo (normal/administrador): ");
                            Usuario.TipoUsuario tipo = Usuario.TipoUsuario.valueOf(scanner.nextLine());

                            Usuario usuario = new Usuario();
                            usuario.setDni(dni);
                            usuario.setNombre(nombre);
                            usuario.setEmail(email);
                            usuario.setPassword(password);
                            usuario.setTipo(tipo);

                            service.registrarUsuario(usuario);
                        } else {
                            System.out.println("Opción no válida.");
                        }
                        break;
                    case 2:
                        if (usuarioActual.getTipo() == Usuario.TipoUsuario.administrador) {
                            // Registrar Libro
                            System.out.print("ISBN: ");
                            String isbn = scanner.nextLine();
                            System.out.print("Título: ");
                            String titulo = scanner.nextLine();
                            System.out.print("Autor: ");
                            String autor = scanner.nextLine();

                            Libro libro = new Libro();
                            libro.setIsbn(isbn);
                            libro.setTitulo(titulo);
                            libro.setAutor(autor);
                            service.registrarLibro(libro);
                        } else {
                            System.out.println("Opción no válida.");
                        }
                        break;
                    case 3:
                        if (usuarioActual.getTipo() == Usuario.TipoUsuario.administrador) {
                            // Registrar Ejemplar
                            System.out.print("ISBN del libro: ");
                            String isbnEjemplar = scanner.next();
                            System.out.print("Estado (Disponible/Prestado/Dañado): ");
                            String estadoInput = scanner.nextLine();

                            Ejemplar.Estado estado;
                            try{
                                estado= Ejemplar.Estado.valueOf(estadoInput);
                            }catch (IllegalArgumentException e){
                                System.out.println("Estado invalido.");
                                break;
                            }

                            Ejemplar ejemplar= new Ejemplar();
                            Libro libro = service.buscarLibroPorIsbn(isbnEjemplar);
                            if(libro != null){
                                ejemplar.setLibro(libro);
                                ejemplar.setEstado(estado);

                                service.registrarEjemplar(ejemplar);
                                System.out.println("Ejemplar registrada con exito.");
                            } else {
                                System.out.println("Libro no encontrado.");
                            }
                            } else {
                                System.out.println("Opcion.");
                            }

                        break;
                    case 4:
                        if (usuarioActual.getTipo() == Usuario.TipoUsuario.administrador) {
                            // Registrar Préstamo
                            System.out.print("DNI del usuario: ");
                            String dniUsuario = scanner.nextLine();
                            System.out.print("ID del ejemplar: ");
                            Long idEjemplar = scanner.nextLong();
                            scanner.nextLine(); // Consumir newline

                            Usuario user = service.buscarUsuarioPorDni(dniUsuario);
                            Ejemplar ejem = service.buscarEjemplarPorID(idEjemplar);

                            Prestamo prestamo = new Prestamo();
                            prestamo.setUsuario(user);
                            prestamo.setEjemplar(ejem);

                            service.registrarPrestamo(prestamo);
                        } else {
                            System.out.println("Opción no válida.");
                        }
                        break;
                    case 5:
                        if (usuarioActual.getTipo() == Usuario.TipoUsuario.administrador) {
                            // Registrar Devolución
                            System.out.print("ID del préstamo: ");
                            int idPrestamo = scanner.nextInt();
                            scanner.nextLine(); // Consumir newline

                            service.registrarDevoluciones(idPrestamo);
                        } else {
                            System.out.println("Opción no válida.");
                        }
                        break;
                    case 6:
                        // Ver Préstamos
                        List<Prestamo> prestamos = service.verPrestamos(usuarioActual);
                        for (Prestamo p : prestamos) {
                            System.out.println("ID Préstamo: " + p.getId());
                            System.out.println("Usuario: " + p.getUsuario().getNombre());
                            System.out.println("Libro: " + p.getEjemplar().getLibro());
                            System.out.println("Fecha Inicio: " + p.getFechaInicio());
                            System.out.println("Fecha Devolución: " + p.getFechaDevolucion());
                            System.out.println("-----------");
                        }
                        break;
                    case 0:
                        service.close();
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            }
        }
    }


