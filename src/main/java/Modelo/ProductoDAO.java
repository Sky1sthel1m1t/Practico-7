package Modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductoDAO extends AbstractDao<Producto> {

    @Override
    public Producto get(int id) {
        Conexion conexion = Conexion.getInstance();
        Producto producto = null;

        String comando = "SELECT * FROM producto WHERE id = " + id;

        try {
            conexion.conectar();
            Statement statement = conexion.getConexion().createStatement();
            ResultSet rs = conexion.consulta(statement, comando);

            if (rs.next()) {
                producto = new Producto(rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("codigo"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getString("fechaVencimiento"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        conexion.desconectar();

        return producto;
    }

    public ArrayList<Producto> getProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        Conexion conexion = Conexion.getInstance();

        String comando = "SELECT * FROM producto";

        try {
            conexion.conectar();
            Statement statement = conexion.getConexion().createStatement();
            ResultSet rs = conexion.consulta(statement, comando);

            while (rs.next()) {
                Producto producto = new Producto(rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("codigo"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getString("fechaVencimiento"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        conexion.desconectar();

        return productos;
    }

    public void a??adirProducto(Producto producto) throws SQLException {
        Conexion conexion = Conexion.getInstance();

        String comando = "INSERT INTO producto VALUES (" +
                producto.id() + "," +
                "'" + producto.nombre() + "'" + "," +
                "'" + producto.codigo() + "'" + "," +
                producto.precio() + "," +
                producto.cantidad() + "," +
                "'" + producto.fechaVencimiento() + "'" + ")";

        conexion.conectar();
        Statement statement = conexion.getConexion().createStatement();
        conexion.ejecutar(statement, comando);

        conexion.desconectar();

    }

    public void eliminarProducto(int id) throws SQLException {
        Conexion conexion = Conexion.getInstance();
        Producto producto = null;

        String comando = "DELETE FROM producto WHERE id = " + id;

        conexion.conectar();
        Statement statement = conexion.getConexion().createStatement();
        conexion.ejecutar(statement, comando);

        conexion.desconectar();
    }

    public void actualizarProducto(Producto producto) throws SQLException {
        Conexion conexion = Conexion.getInstance();

        String comando = "UPDATE producto SET " +
                "nombre = " + "'" +  producto.nombre() + "'" + ", "+
                "codigo = " + "'" + producto.codigo() + "'" + ", "+
                "precio = " + producto.precio() + ", "+
                "cantidad = " + producto.cantidad() + ", "+
                "fechaVencimiento = " + "'" + producto.fechaVencimiento() + "'" + " " +
                "WHERE id = " + producto.id();

        conexion.conectar();
        Statement statement = conexion.getConexion().createStatement();
        conexion.ejecutar(statement, comando);

        conexion.desconectar();
    }

    public ArrayList<String> columnas(){
        ArrayList<String> columnas = new ArrayList<>();

        Conexion conexion = Conexion.getInstance();
        String comando = "DESC producto";

        try {
            conexion.conectar();
            Statement statement = conexion.getConexion().createStatement();
            ResultSet rs = conexion.consulta(statement,comando);
            while (rs.next()){
                String aux = rs.getString("Field");
                columnas.add(aux);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        conexion.desconectar();

        return columnas;
    }


}
