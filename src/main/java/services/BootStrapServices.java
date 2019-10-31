package services;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BootStrapServices {
    /**
     *
     * @throws SQLException
     */
    public static void startDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    /**
     *
     * @throws SQLException
     */
    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }


    /**
     * Metodo para recrear las tablas necesarios
     * @throws SQLException
     */
    public static void crearTablas() throws  SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS USUARIOS\n" +
                "(\n" +
                "  USERNAME VARCHAR(100) PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  PASSWORD VARCHAR(100) NOT NULL,\n" +
                "  ADMINISTRADOR BOOLEAN NOT NULL,\n" +
                "  AUTOR BOOLEAN NOT NULL\n" +
                ");";
        Connection con = DataBaseServices.getInstancia().getConexion();
        Statement statement = con.createStatement();
        statement.execute(sql);
        statement.close();
        con.close();

        String sql2 = "CREATE TABLE IF NOT EXISTS ARTICULOS\n" +
                "(\n" +
                "   ID BIGINT PRIMARY KEY NOT NULL,\n" +
                "  TITULO VARCHAR(100) NOT NULL,\n" +
                "  CUERPO VARCHAR(2000) NOT NULL,\n" +
                "  AUTOR VARCHAR(255),\n" +
                "  FECHA DATE NOT NULL,\n" +
                "  FOREIGN KEY (AUTOR) references USUARIOS(USERNAME)" +
                ");";
        Connection con2 = DataBaseServices.getInstancia().getConexion();
        Statement statement2 = con2.createStatement();
        statement2.execute(sql2);
        statement2.close();
        con2.close();

        String sql5 = "CREATE TABLE IF NOT EXISTS COMENTARIOS\n" +
                "(\n" +
                "   ID BIGINT PRIMARY KEY NOT NULL,\n" +
                "  COMENTARIO VARCHAR(2000) NOT NULL,\n" +
                "  AUTOR VARCHAR(100) NOT NULL,\n" +
                "  ARTICULO INTEGER NOT NULL,\n" +
                "  FOREIGN KEY (AUTOR) references USUARIOS(USERNAME),\n" +
                "  FOREIGN KEY (ARTICULO) references ARTICULOS(ID)" +
                ");";
        Connection con5 = DataBaseServices.getInstancia().getConexion();
        Statement statement5 = con5.createStatement();
        statement5.execute(sql5);
        statement5.close();
        con5.close();

        String sql6 = "CREATE TABLE IF NOT EXISTS ETIQUETAS\n" +
                "(\n" +
                "   ID BIGINT PRIMARY KEY NOT NULL,\n" +
                "  ETIQUETA VARCHAR(100) NOT NULL" +
                ");";
        Connection con6 = DataBaseServices.getInstancia().getConexion();
        Statement statement6 = con6.createStatement();
        statement6.execute(sql6);
        statement6.close();
        con6.close();

        String sql3 = "CREATE TABLE IF NOT EXISTS ARTICULOSCOMENTARIOS\n" +
                "(\n" +
                "   ID BIGINT PRIMARY KEY NOT NULL,\n" +
                "  IDARTICULO INTEGER NOT NULL,\n" +
                "  IDCOMENTARIO INTEGER NOT NULL,\n" +
                "  FOREIGN KEY (IDARTICULO) references ARTICULOS(ID),\n" +
                "  FOREIGN KEY (IDCOMENTARIO) references COMENTARIOS(ID)" +
                ");";
        Connection con3 = DataBaseServices.getInstancia().getConexion();
        Statement statement3 = con3.createStatement();
        statement3.execute(sql3);
        statement3.close();
        con3.close();

        String sql4 = "CREATE TABLE IF NOT EXISTS ARTICULOSETIQUETAS\n" +
                "(\n" +
                "   ID BIGINT PRIMARY KEY NOT NULL,\n" +
                "  IDARTICULO INTEGER NOT NULL,\n" +
                "  IDETIQUETA INTEGER NOT NULL,\n" +
                "  FOREIGN KEY (IDARTICULO) references ARTICULOS(ID),\n" +
                "  FOREIGN KEY (IDETIQUETA) references ETIQUETAS(ID)" +
                ");";
        Connection con4 = DataBaseServices.getInstancia().getConexion();
        Statement statement4 = con4.createStatement();
        statement4.execute(sql4);
        statement4.close();
        con4.close();
    }
}
