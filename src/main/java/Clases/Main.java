package Clases;

import org.apache.commons.codec.digest.DigestUtils;
import services.BootStrapServices;
import services.DataBaseServices;
import services.GestionDB;

import java.sql.SQLException;
import java.util.UUID;

public class Main {

    public static void main(String[] args) throws SQLException {
        //staticFileLocation("/publico");
        new Rutas().manejoRutas();

        //Aplicando Filtros
        new Filtros().aplicarFiltros();

        //Iniciando el servicio
        BootStrapServices.startDb();

        //Prueba de Conexión.
        DataBaseServices.getInstancia().testConexion();

        //BootStrapServices.crearTablas();

        Controladora.getInstance().setData();

        if(Controladora.getInstance().getMisUsuarios().size() == 0){
            System.out.println("Registrando al usuario admin");
            String adminPassword = "admin";
            Usuario usuario = new Usuario("Admin", "Admin", DigestUtils.md5Hex(adminPassword), true);
            usuario.setId(UUID.randomUUID().toString());
            new GestionDB<Usuario>(Usuario.class).crear(usuario);
            Controladora.getInstance().getMisUsuarios().add(usuario);
        }
    }
}
