package Clases;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
class Likes implements Serializable {
    @Id
    private long id;
    @OneToOne
    private Usuario usu;
    @OneToOne
    private Articulo art;

    public Likes()
    {

    }

    public Likes(Usuario usu, Articulo art) {
        this.usu = usu;
        this.art = art;
        this.id = Controladora.getInstance().getNumeracionLike();
    }

    public Usuario getUsu() {
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }

    public Articulo getArt() {
        return art;
    }

    public void setArt(Articulo art) {
        this.art = art;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
