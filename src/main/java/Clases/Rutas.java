package Clases;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import services.GestionDB;
import spark.Session;
import spark.Spark;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rutas {
    public void manejoRutas()
    {
        final Configuration configuration = new Configuration(new Version(2, 3, 0));
        //configuration.setClassForTemplateLoading(this.getClass(), "/");
        try {
            configuration.setDirectoryForTemplateLoading(new File(
                    "src/main/java/resources/spark/template/freemarker"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Spark.get("/menu/:pageNumber", (request, response) -> {
            int pageNumber = Integer.parseInt(request.params("pageNumber"));
            List<Articulo> reverseList = Controladora.getInstance().reverseArticulos(pageNumber);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("listaArticulos", reverseList);
            attributes.put("loggedUser", request.session(true).attribute("usuario"));
            attributes.put("pageNumber", pageNumber);
            attributes.put("sizeArticulos", reverseList.size());
            attributes.put("sizeAllArticulos", Controladora.getInstance().getMisArticulos().size());
            return getPlantilla(configuration, attributes, "index.ftl");
        });

        Spark.get("/autores", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("listaUsuarios", Controladora.getInstance().getMisUsuarios());
            return getPlantilla(configuration, attributes, "autor.ftl");
        });

        Spark.post("/deleteComment/:idPost/:idComment", (request, response) -> {
            //InterArticleServices articleServices = new InterArticleServices();
            long idPost = Long.parseLong(request.params("idPost"));
            Articulo art = Controladora.getInstance().buscarArticulo(idPost);
            long idComment = Long.parseLong(request.params("idComment"));
            //Comentario comentario = Controladora.getInstance().buscarComentario(idComment);
            Comentario comentario = Controladora.getInstance().buscarComentario(idComment);
            //articleServices.borrarComentarioDeArticulo(art, comentario);
            art.getListaComentarios().remove(comentario);
            new GestionDB<Articulo>(Articulo.class).editar(art);
            new GestionDB<Comentario>(Comentario.class).eliminar(comentario.getId());
            response.redirect("/menu/articulo/" + idPost);
            return "";
        });

        Spark.get("/deletePost/:idPost", (request, response) -> {
            long id = Long.parseLong(request.params("idPost"));
            Articulo art = Controladora.getInstance().buscarArticulo(id);
            /*new InterArticleServices().borrarTodaEtiquetaDeArticulo(art);
            new InterArticleServices().borrarTodoComentarioArticulo(art);
            new ArticleServices().borrarArticulo(id);*/
            new GestionDB<Articulo>(Articulo.class).eliminar(art.getId());
            Controladora.getInstance().getMisArticulos().remove(art);
            response.redirect("/menu/1");
            return "";
        });

        Spark.get("/menu/articulo/:id", (request, response) -> {
            long id = Long.parseLong(request.params("id"));
            Articulo articulo = Controladora.getInstance().buscarArticulo(id);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("articulo", articulo);
            attributes.put("listaComentarios", articulo.getListaComentarios());
            attributes.put("loggedUser", request.session(true).attribute("usuario"));
            attributes.put("likes", Controladora.getInstance().getActiveLikes(articulo));
            attributes.put("dislikes", Controladora.getInstance().getActiveDislikes(articulo));
           return getPlantilla(configuration, attributes, "post.ftl");
        });

        Spark.post("/makeAutor/:username", (request, response) -> {
            String username = request.params("username");
            Usuario usu = Controladora.getInstance().buscarAutor(username);
            boolean valor;
            if(request.queryParams("checkAutor") != null)
            {
                valor = true;
            }
            else{
                valor=false;
            }
            usu.setAutor(valor);
            new GestionDB<Usuario>(Usuario.class).editar(usu);
            //new UserServices().actualizarUsuario(usu);
            response.redirect("/autores");
            return "";
        });

        Spark.get("/saveComment/:id", (request, response) -> {
            String id = request.params("id");
            String comentario = request.queryParams("commentContent");
            Articulo articulo = Controladora.getInstance().buscarArticulo(Long.parseLong(id));
            int indexArt = Controladora.getInstance().getMisArticulos().indexOf(articulo);
            Session session=request.session(true);
            Usuario usuario = session.attribute("usuario");
            Comentario newComentario = new Comentario(comentario, usuario, articulo);

            articulo.getListaComentarios().add(newComentario);
            new GestionDB<Comentario>(Comentario.class).crear(newComentario);
            new GestionDB<Articulo>(Articulo.class).editar(articulo);

            //new ComentServices().crearComentario(newComentario);
            //new InterArticleServices().nuevoComentarioAlArticulo(articulo, newComentario);
            response.redirect("/menu/articulo/"+id);
            return "";
        });

        Spark.post("/createPost", (request, response) -> {
            String title = request.queryParams("postTitle");
            String body = request.queryParams("postContent");
            ArrayList<Etiqueta> tags = Controladora.getInstance().divideTags(request.queryParams("tags"));
            Articulo art = new Articulo(title, body, request.session(true).attribute("usuario"));

            System.out.println("La cantidad de tags es: " + Controladora.getInstance().getMisEtiquetas().size());
            for (Etiqueta etq: tags) {
                if (Controladora.getInstance().buscarEtiqueta(etq.getId()) != null)
                {
                   // System.out.println()
                    System.out.println(etq.getId());
                    new GestionDB<Etiqueta>(Etiqueta.class).crear(etq);
                }
            }
            art.setListaEtiquetas(tags);
            new GestionDB<Articulo>(Articulo.class).crear(art);
            Controladora.getInstance().getMisArticulos().add(art);
            response.redirect("/menu/1");
            return "";
        });

        Spark.post("/updatePost/:idArticle", (request, response) -> {
            String title = request.queryParams("postTitle");
            String body = request.queryParams("postContent");
            ArrayList<Etiqueta> tags = Controladora.getInstance().divideTags(request.queryParams("tags"));
            long id = Long.parseLong(request.params("idArticle"));
            Articulo art = Controladora.getInstance().buscarArticulo(id);
            art.setTitulo(title);
            art.setCuerpo(body);
            art.setFecha(Date.valueOf(LocalDate.now().toString()));
            //new ArticleServices().actualizarArticulo(art);
            //new GestionDB<Articulo>().editar(art);
            /*for (Etiqueta e: art.getListaEtiquetas()
            ) {
                //new InterArticleServices().borrarEtiquetaDeArticulo(art, e);
            }*/
            art.getListaEtiquetas().clear();
            new GestionDB<Articulo>().editar(art);
            art.setListaEtiquetas(tags);
            for (Etiqueta etq: tags
            ) {
                Etiqueta tag;
                if(!Controladora.getInstance().tagExistence(etq))
                {
                    tag = new Etiqueta(etq.getEtiqueta());
                    long idEtq = Controladora.getInstance().getMisEtiquetas().get(Controladora.getInstance().getMisEtiquetas().size()-1).getId()+1;
                    tag.setId(idEtq);
                    Controladora.getInstance().getMisEtiquetas().add(tag);
                    //new TagServices().crearEtiqueta(tag);
                    new GestionDB<Etiqueta>(Etiqueta.class).crear(tag);
                }
                else
                {
                    tag = Controladora.getInstance().buscarEtqPorContenido(etq.getEtiqueta());
                }

                //new InterArticleServices().nuevaEtiquetaAlArticulo(art, tag);
                new GestionDB<Articulo>(Articulo.class).editar(art);
            }
            response.redirect("/menu/" + id);
            return " ";
        });

        Spark.get("/addLike/:idArticle/:usuario", (request, response) -> {
            long id = Long.parseLong(request.params("idArticle"));
            Articulo art = Controladora.getInstance().buscarArticulo(id);

            String username = request.params("usuario");
            Usuario usu = Controladora.getInstance().buscarAutor(username);

            Controladora.getInstance().setNumeracionLike(Controladora.getInstance().getNumeracionLike()+1);

            Likes actualLike = null;
            Dislike actualDislike = null;
            Likes like = new Likes(usu, art);
            Dislike dislike = new Dislike(usu, art);
            if(Controladora.getInstance().validateLike(usu, art, like)){
                Controladora.getInstance().deactivateDislikeIfLike(usu, art, dislike);
                art.getLikes().add(like);
                new GestionDB<Likes>(Likes.class).crear(like);
                new GestionDB<Articulo>(Articulo.class).editar(art);
            }
            else{
                Controladora.getInstance().deactivateDislikeIfLike(usu, art, dislike);
                actualLike = Controladora.getInstance().findLikes(usu, art, like);
                Controladora.getInstance().toggleLike(actualLike, art);
            }
        response.redirect("/menu/articulo/" + art.getId());
        return "";
        });

        Spark.get("/addDislike/:idArticle/:usuario", (request, response) -> {
            long id = Long.parseLong(request.params("idArticle"));
            Articulo art = Controladora.getInstance().buscarArticulo(id);

            String username = request.params("usuario");
            Usuario usu = Controladora.getInstance().buscarAutor(username);

            Controladora.getInstance().setNumeracionLike(Controladora.getInstance().getNumeracionLike()+1);

            Likes actualLike = null;
            Dislike actualDislike = null;
            Likes like = new Likes(usu, art);
            Dislike dislike = new Dislike(usu, art);
            if(Controladora.getInstance().validateDislike(usu, art, dislike)){
                Controladora.getInstance().deactivateLikeIfDislike(usu, art, like);
                art.getDislikes().add(dislike);
                new GestionDB<Dislike>(Dislike.class).crear(dislike);
                new GestionDB<Articulo>(Articulo.class).editar(art);
            }
            else{
                Controladora.getInstance().deactivateLikeIfDislike(usu, art, like);
                actualDislike = Controladora.getInstance().findDislikes(usu, art, dislike);
                Controladora.getInstance().toggleDislike(actualDislike, art);
            }
            response.redirect("/menu/articulo/" + art.getId());
            return "";
        });



        /**
         * Metodos Get y Post para logearse.
         */
        Spark.get("/login", (request, response) -> {
            String warningText = "";
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("warningText", warningText);
            return getPlantilla(configuration, attributes, "login.ftl");
        });

        Spark.post("/login", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            if(!Controladora.getInstance().validatePassword(username, password)){
                String warningText = "Usuario o contrasena incorrectos.";
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("warningText", warningText);
                return getPlantilla(configuration, attributes, "login.ftl");
            }

            Session session=request.session(true);
            Usuario usuario = Controladora.getInstance().buscarAutor(username);
            session.attribute("usuario", usuario);
            System.out.println("Hola");
            String remember = request.queryParams("remember");

            if(remember != null){

                System.out.println("waaaaaasaaaa");
                response.cookie("usuario_id", usuario.getUsername(), 604800000);
            }
            response.redirect("/menu/1");
            return "";
        });

        /**
         *Metodos Get y Post para registrarse.
         */
        Spark.get("/register", (request, response) -> {
            String warningText = "";
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("warningText", warningText);
            return getPlantilla(configuration, attributes, "register.ftl");
        });

        Spark.post("/register", (request, response) -> {
            String nombre = request.queryParams("first_name") + " " + request.queryParams("last_name");
            String username = request.queryParams("username");
            if(!Controladora.getInstance().validateUser(username)){
                String warningText = "Usuario ya existe";
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("warningText", warningText);
                return getPlantilla(configuration, attributes, "register.ftl");
            }
            String password = request.queryParams("password");
            String confirmPassword = request.queryParams("confirm_password");
            if(!password.equals(confirmPassword)){
                String warningText = "Las contraseñas no coinciden";
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("warningText", warningText);
                return getPlantilla(configuration, attributes, "register.ftl");
            }
            Usuario usuario = new Usuario(username, nombre, password, false);
            new GestionDB<Usuario>(Usuario.class).crear(usuario);
            Controladora.getInstance().getMisUsuarios().add(usuario);
            Session session=request.session(true);
            session.attribute("usuario", usuario);
            response.redirect("/menu/1");
            return "";
        });

        /**
         * Metodo get para terminar la sesion.
         */
        Spark.get("/disconnect", (request, response) -> {
            Session session=request.session(true);
            session.invalidate();
            response.removeCookie("usuario_id");
            response.redirect("/login");
            return "";
        });

    }

    public StringWriter getPlantilla(Configuration configuration, Map<String, Object> model, String templatePath) throws IOException, TemplateException {
        Template plantillaPrincipal = configuration.getTemplate(templatePath);
        StringWriter writer = new StringWriter();
        plantillaPrincipal.process(model, writer);
        return writer;
    }
}
