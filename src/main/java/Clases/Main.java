package Clases;

import services.BootStrapServices;
import services.DataBaseServices;
import services.GestionDB;

import java.sql.SQLException;

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
            Usuario usuario = new Usuario("Admin", "Admin", "admin", true);
            new GestionDB<Usuario>().crear(usuario);
            Controladora.getInstance().getMisUsuarios().add(usuario);
        }
    }
}
