package Clases;
import static spark.Spark.*;

public class Filtros {
    public void aplicarFiltros(){
        before((request, response) -> {
            Usuario usuario = request.session().attribute("usuario");
            String username = request.cookie("usuario_id");
            if(username != null && usuario == null){
                Usuario userLog = Controladora.getInstance().buscarAutor(username);
                request.session(true).attribute("usuario", userLog);
            }
        });
        /**before("/menu/*", (request, response) -> {
            Usuario usuario=request.session().attribute("usuario");
            if(usuario==null){
                System.out.println("El usuario es null");
                response.redirect("/login");
            }
            else{
                response.header("Usuario", usuario.getUsername());
            }
        });**/

//        before("/menu/*", (request, response) -> {
//            Usuario usuario=request.session().attribute("usuario");
//            if(usuario==null){
//                response.redirect("/login");
//            }
//            else{
//                response.header("Usuario", usuario.getUsername());
//            }
//        });

        before("/login", (request, response) -> {
            Usuario usuario=request.session().attribute("usuario");
            if(usuario!=null){
                response.redirect("/menu");
            }
        });

        before("/register", (request, response) -> {
            Usuario usuario=request.session().attribute("usuario");
            if(usuario!=null){
                response.redirect("/menu");
            }
        });
    }

}
