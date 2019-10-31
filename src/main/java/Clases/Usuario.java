package Clases;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Usuario implements Serializable {
    @Id
    private String username;
    private String nombre;
    private String password;
    private boolean administrador;
    private boolean autor;

    public Usuario(){

    }
    public Usuario(String username, String nombre, String password, boolean administrador){
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.administrador = administrador;
        this.autor = false;
    }

    public String getUsername() {
        return username;
    }
    public String getNombre() {
        return nombre;
    }
    public String getPassword() {
        return password;
    }
    public boolean isAdministrador() {
        return administrador;
    }
    public boolean isAutor() {
        return autor;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAutor(boolean autor) {
        this.autor = autor;
    }
}
