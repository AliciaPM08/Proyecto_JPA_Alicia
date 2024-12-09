package Biblioteca.DTOS;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

    @Entity
    @Table(name = "usuario")
    public class Usuario {
        //Variables
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Integer id;

        @Column(name = "dni", nullable = false, length = 15)
        private String dni;

        @Column(name = "nombre", nullable = false, length = 100)
        private String nombre;

        @Column(name = "email", nullable = false, length = 100)
        private String email;

        @Column(name = "password", nullable = false)
        private String password;

        //Tipo => Enum
        @Enumerated(EnumType.STRING)
        @Column(name = "tipo", nullable = false)
        private TipoUsuario tipo;

        @Column(name = "penalizacionHasta")
        private Date penalizacionHasta;

        //Vinculo con la tabla Prestamos
        @OneToMany(mappedBy = "usuario")
        private Set<Prestamo> prestamos = new LinkedHashSet<>();

        //Getters y setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDni() {
            return dni;
        }

        public void setDni(String dni) {
            this.dni = dni;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public TipoUsuario getTipo() {
            return tipo;
        }

        public void setTipo(TipoUsuario tipo) {
            this.tipo = tipo;
        }

        public Date getPenalizacionHasta() {
            return penalizacionHasta;
        }

        public void setPenalizacionHasta(Date penalizacionHasta) {
            this.penalizacionHasta = penalizacionHasta;
        }

        public Set<Prestamo> getPrestamos() {
            return prestamos;
        }

        public void setPrestamos(Set<Prestamo> prestamos) {
            this.prestamos = prestamos;
        }

        //Creo un enum para el tipo de Usuario
        public enum TipoUsuario {
            NORMAL, ADMINISTRADOR
        }
    }