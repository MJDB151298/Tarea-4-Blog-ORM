package Clases;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class Comentario implements Serializable {
    @Id
    private long id;
    private String comentario;
    @OneToOne
    private Usuario autor;
    @OneToOne
    private Articulo articulo;

    public Comentario(){

    }

    public Comentario(String comentario, Usuario autor, Articulo articulo){
        this.id = Controladora.getInstance().getMisComentarios().get(Controladora.getInstance().getMisComentarios().size()-1).getId()-1;
        this.comentario = comentario;
        this.autor = autor;
        this.articulo = articulo;
    }

    public long getId() {
        return id;
    }
    public String getComentario() {
        return comentario;
    }
    public Usuario getAutor() {
        return autor;
    }
    public Articulo getArticulo() {
        return articulo;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public void setAutor(Usuario autor) {
        this.autor = autor;
    }
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}
