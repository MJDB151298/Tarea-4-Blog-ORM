package Clases;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Articulo implements Serializable {
    @Id
    private long id;
    private String titulo;
    @Column(length = 2000)
    private String cuerpo;
    private Usuario autor;
    private Date fecha;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comentario> listaComentarios;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Etiqueta> listaEtiquetas;
    @Transient
    private String cuerpoResumido;
    @ManyToMany(mappedBy = "art", cascade = CascadeType.ALL)
    private List<Likes> likes;
    @ManyToMany(mappedBy = "art", cascade = CascadeType.ALL)
    private List<Dislike> dislikes;


    public Articulo(){

    }

    public List<Likes> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Likes> likes) {
        this.likes = likes;
    }

    public List<Dislike> getDislikes() {
        return dislikes;
    }

    public void setDislikes(ArrayList<Dislike> dislikes) {
        this.dislikes = dislikes;
    }

    public Articulo(String titulo, String cuerpo, Usuario autor){
        this.id = Controladora.getInstance().getMisArticulos().get(Controladora.getInstance().getMisArticulos().size()-1).getId()+1;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = Date.valueOf(LocalDate.now());
        this.listaComentarios = new ArrayList<>();
        this.listaEtiquetas = new ArrayList<>();
        this.cuerpoResumido = "";
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
    }

    public long getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getCuerpo() {
        return cuerpo;
    }
    public Usuario getAutor() {
        return autor;
    }
    public Date getFecha() {
        return fecha;
    }
    public List<Etiqueta> getListaEtiquetas() { return listaEtiquetas; }
    public List<Comentario> getListaComentarios() {
        return listaComentarios;
    }
    public String getCuerpoResumido(){
        cuerpoResumido = "";
        if(getCuerpo().length() > 70){
            int i = 0;
            while(i < 70){
                cuerpoResumido += getCuerpo().charAt(i);
                i++;
            }
            cuerpoResumido += "...";
        }
        else{
            cuerpoResumido = getCuerpo();
        }
        return cuerpoResumido;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public void setAutor(Usuario autor) {
        this.autor = autor;
    }
    public void setListaComentarios(List<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }
    public void setListaEtiquetas(List<Etiqueta> listaEtiquetas) {
        this.listaEtiquetas = listaEtiquetas;
    }



}
