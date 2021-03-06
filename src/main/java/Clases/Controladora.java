package Clases;

import services.GestionDB;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Controladora implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Controladora controladora;
    private ArrayList<Usuario> misUsuarios;
    private ArrayList<Articulo> misArticulos;
    private ArrayList<Comentario> misComentarios;
    private ArrayList<Etiqueta> misEtiquetas;
    private long numeracionLike;
    private long numeracionDislike;

    public Controladora(){
        this.misArticulos = new ArrayList<>();
        this.misUsuarios = new ArrayList<>();
        this.misComentarios = new ArrayList<>();
        this.misEtiquetas = new ArrayList<>();
        this.numeracionLike = 0;
        this.numeracionDislike = 0;
    }

    public static Controladora getInstance() {
        if (controladora == null) {
            controladora = new Controladora();
        }
        return controladora;
    }

    public void setData(){
        EntityManagerFactory emf =  Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        EntityManager entityManager = emf.createEntityManager();
        this.misUsuarios = (ArrayList<Usuario>) entityManager.createQuery("select u from Usuario u", Usuario.class).getResultList();
        this.misArticulos = (ArrayList<Articulo>) entityManager.createQuery("select a from Articulo a", Articulo.class).getResultList();
        this.misComentarios = (ArrayList<Comentario>) entityManager.createQuery("select c from Comentario c", Comentario.class).getResultList();
        this.misEtiquetas = (ArrayList<Etiqueta>) entityManager.createQuery("select e from Etiqueta e", Etiqueta.class).getResultList();
        this.numeracionLike = (Long) entityManager.createQuery("select count(n.id) from Likes n").getSingleResult();
        this.numeracionDislike = (Long) entityManager.createQuery("select count(n.id) from Dislike n").getSingleResult();
        System.out.println("La cantidad de likes existentes es: " + numeracionLike);
        System.out.println("La cantidad de dislike no existentes es: " + numeracionDislike);
        System.out.println("La cantidad de dislikes en un articulo es: " + misArticulos.get(0).getDislikes().size());
    }

    public List<Articulo> reverseArticulos(int pageNumber, List<Articulo> articulos){
        List<Articulo> reverse = new ArrayList<>();
        int articleStartingPoint = articulos.size()-1;
//        if(pageNumber != 1){
//            articleStartingPoint -= ((pageNumber-1) * 5);
//        }
        int articleEndPoint = articulos.size()-(pageNumber*5);
        if(articleEndPoint < 0){
            articleEndPoint = 0;
        }
        for(int i = articleStartingPoint; i >= articleEndPoint; i--) {
            reverse.add(articulos.get(i));
        }
        return reverse;
    }

    public Articulo buscarArticulo(long id){
        for(Articulo articulo : Controladora.getInstance().getMisArticulos()){
            if(articulo.getId() == id){
                return articulo;
            }
        }
        return null;
    }

    public Usuario buscarAutor(String userName)
    {
        System.out.println(userName);
        Usuario usuario = null;
        for (Usuario usu: Controladora.getInstance().getMisUsuarios()
             ) {
            if (usu.getUsername().equalsIgnoreCase(userName))
            {
                usuario = usu;
            }
        }
        return usuario;
    }

    public Usuario buscarUsuarioPorID(String id){
        for(Usuario usuario : Controladora.getInstance().getMisUsuarios()){
            if(usuario.getId().equalsIgnoreCase(id)){
                return usuario;
            }
        }
        return null;
    }

    public Comentario buscarComentario(long id)
    {
        Comentario coment = null;

        for (Comentario com: Controladora.getInstance().getMisComentarios()
             ) {
            if (com.getId() == id)
            {
                coment = com;
            }
        }

        return coment;
    }

    public Etiqueta buscarEtiqueta(long id)
    {
        Etiqueta etiq = null;

        for (Etiqueta et: Controladora.getInstance().getMisEtiquetas()
        ) {
            if (et.getId() == id)
            {
                etiq = et;
            }
        }

        return etiq;
    }

    public Etiqueta buscarEtiquetaPorNombre(String nombre){
        for(Etiqueta etiqueta : getMisEtiquetas()){
            if(etiqueta.getEtiqueta().equalsIgnoreCase(nombre)){
                return etiqueta;
            }
        }
        return null;
    }

    public boolean tagExistence(Etiqueta etq)
    {
        boolean flag = false;

        for (Etiqueta etiqueta: Controladora.getInstance().getMisEtiquetas()
        ) {
            if(etiqueta.getEtiqueta().equalsIgnoreCase(etq.getEtiqueta()))
            {
                flag = true;
            }
        }

        return flag;
    }

    public boolean validateArticle(Articulo art)
    {
        boolean flag = true;

        if(getMisArticulos().size() > 0)
        {
            for (Articulo articulo: Controladora.getInstance().getMisArticulos()
            ) {
                if(art.getTitulo().equalsIgnoreCase(articulo.getTitulo()) || art.getCuerpo().equalsIgnoreCase(articulo.getCuerpo()))
                {
                    flag = false;
                }
            }
        }

        return flag;
    }

    public boolean validateUser(String username){
        for(Usuario usuario : Controladora.getInstance().getMisUsuarios()){
            if(usuario.getUsername().equalsIgnoreCase(username)){
                return false;
            }
        }
        return true;
    }

    public boolean validatePassword(String username, String password){
        for(Usuario usuario : Controladora.getInstance().getMisUsuarios()){
            if(usuario.getUsername().equalsIgnoreCase(username) && usuario.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Etiqueta> divideTags(String tagsinput)
    {
        ArrayList<Etiqueta> tags = new ArrayList<>();
        String[] ntags = tagsinput.split(" ");
        for (String tag: ntags) {
            Etiqueta etq = new Etiqueta(tag);
            Controladora.getInstance().getMisEtiquetas().add(etq);
            tags.add(etq);
        }
        return tags;
    }

    public Etiqueta buscarEtqPorContenido(String cont)
    {
        Etiqueta etq = null;

        for (Etiqueta e: Controladora.getInstance().getMisEtiquetas()
        ) {
            if(e.getEtiqueta().equalsIgnoreCase(cont))
            {
                etq = e;
                break;
            }
        }

        return etq;
    }

    public boolean buscarEtqDeArticulo(Articulo art, Etiqueta etiqueta)
    {
        boolean flag = false;

        for (Etiqueta etq: art.getListaEtiquetas()
        ) {
            if (etiqueta.getEtiqueta().equalsIgnoreCase(etq.getEtiqueta()))
            {
                flag = true;
                break;
            }
        }

        return flag;
    }

    public boolean validateLike(Usuario usuario, Articulo articulo, Likes like){
        for(Likes artLike : articulo.getLikes()){
            if(like.getUsu().equals(artLike.getUsu()) && like.getArt().equals(artLike.getArt())){
                System.out.println("I am about to give you a false value");
                return false;
            }
        }
        return true;
    }

    public Likes findLikes(Usuario usuario, Articulo articulo, Likes like){
        for(Likes artLike : articulo.getLikes()){
            if(like.getUsu().equals(artLike.getUsu()) && like.getArt().equals(artLike.getArt())){
                return artLike;
            }
        }
        return null;
    }

    public void toggleLike(Likes like, Articulo art){
        int index = art.getLikes().indexOf(like);
        if(art.getLikes().get(index).isActivo()){
            System.out.println("Estoy tratando de desactivar el like");
            art.getLikes().get(index).setActivo(false);
        }
        else{
            System.out.println("Estoy tratando de activar el like");
            art.getLikes().get(index).setActivo(true);
        }
        new GestionDB<Likes>().editar(like);
        //new GestionDB<Articulo>().editar(art);
    }

    public int getActiveLikes(Articulo articulo){
        int cant = 0;
        for(Likes like : articulo.getLikes()){
            if(like.isActivo()){
                cant++;
            }
        }
        return cant;
    }

    public void deactivateLikeIfDislike(Usuario usuario, Articulo articulo, Likes like){
        if(!validateLike(usuario, articulo, like)){
            Likes actualLike = findLikes(usuario, articulo, like);
            if(actualLike.isActivo()){
                toggleLike(actualLike, articulo);
            }
        }
    }

    public boolean validateDislike(Usuario usuario, Articulo articulo, Dislike dislike){
        for(Dislike artDislike : articulo.getDislikes()){
            if(dislike.getUsu().equals(artDislike.getUsu()) && dislike.getArt().equals(artDislike.getArt())){
                System.out.println("I am about to give you a false value");
                return false;
            }
        }
        return true;
    }

    public Dislike findDislikes(Usuario usuario, Articulo articulo, Dislike dislike){
        for(Dislike artDislike : articulo.getDislikes()){
            if(dislike.getUsu().equals(artDislike.getUsu()) && dislike.getArt().equals(artDislike.getArt())){
                return artDislike;
            }
        }
        return null;
    }

    public int getActiveDislikes(Articulo articulo){
        int cant = 0;
        for(Dislike dislike : articulo.getDislikes()){
            if(dislike.isActivo()){
                cant++;
            }
        }
        return cant;
    }

    public void toggleDislike(Dislike dislike, Articulo art){
        int index = art.getDislikes().indexOf(dislike);
        if(art.getDislikes().get(index).isActivo()){
            System.out.println("Estoy tratando de desactivar el dislike");
            art.getDislikes().get(index).setActivo(false);
        }
        else{
            System.out.println("Estoy tratando de activar el dislike");
            art.getDislikes().get(index).setActivo(true);
        }
        new GestionDB<Dislike>().editar(dislike);
        //new GestionDB<Articulo>().editar(art);
    }

    public void deactivateDislikeIfLike(Usuario usuario, Articulo articulo, Dislike dislike){
        if(!validateDislike(usuario, articulo, dislike)){
            System.out.println("I am deactivating the dislike");
            Dislike actualDislike = findDislikes(usuario, articulo, dislike);
            if(actualDislike.isActivo()){
                toggleDislike(actualDislike, articulo);
            }
        }
    }

    public List<Articulo> buscarArticuloByEtiqueta(String etiquetaNombre){
        //System.out.println("Nombre de la etiqueta: " + etiquetaNombre);
        List<Articulo> listaArticulos = new ArrayList<>();
        for(Articulo articulo : Controladora.getInstance().getMisArticulos()){
            for(Etiqueta etiqueta : articulo.getListaEtiquetas()){
                if(etiqueta.getEtiqueta().equalsIgnoreCase(etiquetaNombre)){
                    listaArticulos.add(articulo);
                    break;
                }
            }
        }
        return listaArticulos;
    }

    public List<Articulo> getArticuloPaginacion(int cantidad){
        EntityManagerFactory emf =  Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        EntityManager entityManager = emf.createEntityManager();
        Query query = entityManager.createQuery("select a from Articulo a order by a.id desc", Articulo.class);
        List<Articulo> list = query.getResultList();
        if(list.size() > 0){
            long id = list.get(0).getId();
            long valor = 0;
            System.out.println("el id del primer elemento es = " + id);
            query.setMaxResults(5);
            if(cantidad != 5)
            {
                valor = ((id+5)-cantidad);
                query.setMaxResults(Integer.parseInt(Long.toString(valor)));
            }
            if(cantidad == 5){
                query.setFirstResult(0);
            }
            else {
                int firstResult = (int)id - (int)valor;
                query.setFirstResult(firstResult);
            }
            return query.getResultList();
        }
        return list;
    }



    public ArrayList<Usuario> getMisUsuarios() {
        return misUsuarios;
    }

    public ArrayList<Articulo> getMisArticulos(){
        return this.misArticulos;
    }

    public ArrayList<Comentario> getMisComentarios() {
        return misComentarios;
    }

    public ArrayList<Etiqueta> getMisEtiquetas() {
        return misEtiquetas;
    }

    public long getNumeracionLike() {
        return numeracionLike;
    }

    public void setNumeracionLike(long numeracionLike) {
        this.numeracionLike = numeracionLike;
    }

    public long getNumeracionDislike() {
        return numeracionDislike;
    }

    public void setNumeracionDislike(long numeracionDislike) {
        this.numeracionDislike = numeracionDislike;
    }

}
