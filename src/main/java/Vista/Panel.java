package Vista;

import Modelo.Producto;
import Modelo.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Panel extends JPanel {

    private JLabel lbID = new JLabel("ID: ");
    private JLabel lbNombre = new JLabel("Nombre: ");
    private JLabel lbCodigo = new JLabel("Codigo: ");
    private JLabel lbPrecio = new JLabel("Precio: ");
    private JLabel lbCantidad = new JLabel("Cantidad: ");
    private JLabel lbFechaVencimiento = new JLabel("Fecha de vencimiento: ");
    private JTextField txtID = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JTextField txtCodigo = new JTextField();
    private JTextField txtPrecio = new JTextField();
    private JTextField txtCantidad = new JTextField();
    private JTextField txtFechaVencimiento = new JTextField();
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnEliminar = new JButton("Eliminar");

    private ProductoDAO productoDAO = new ProductoDAO();

    private DefaultTableModel defaultTableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable jTable;
    private JScrollPane scrollPane;

    public Panel(Dimension dimension) {
        setSize(dimension);
        init1();
    }

    private void init1() {
        setLayout(null);

        int x = this.getWidth() / 2 - 200;
        int y = 20;

        lbID.setBounds(x, y, 200, 30);
        x += 200;
        txtID.setBounds(x, y, 200, 30);
        y += 40;
        x -= 200;
        lbNombre.setBounds(x, y, 200, 30);
        x += 200;
        txtNombre.setBounds(x, y, 200, 30);
        y += 40;
        x -= 200;
        lbCodigo.setBounds(x, y, 200, 30);
        x += 200;
        txtCodigo.setBounds(x, y, 200, 30);
        y += 40;
        x -= 200;
        lbPrecio.setBounds(x, y, 200, 30);
        x += 200;
        txtPrecio.setBounds(x, y, 200, 30);
        y += 40;
        x -= 200;
        lbCantidad.setBounds(x, y, 200, 30);
        x += 200;
        txtCantidad.setBounds(x, y, 200, 30);
        y += 40;
        x -= 200;
        lbFechaVencimiento.setBounds(x, y, 200, 30);
        x += 200;
        txtFechaVencimiento.setBounds(x, y, 200, 30);
        y += 40;
        btnGuardar.setBounds(this.getWidth() / 2 - 75, y, 150, 30);
        y += 40;
        btnEliminar.setBounds(this.getWidth() / 2 - 75, y, 150, 30);
        y+=40;

        iniciarJtable();

        scrollPane.setBounds(0, y, this.getWidth() - 15, this.getHeight() - y - 30);

        btnGuardar.addActionListener(e -> {
            String aux = txtID.getText().trim();
            Producto producto;

            int id;

            try {
                id = Integer.parseInt(aux);
                producto = productoDAO.get(id);
            } catch (Exception error){
                JOptionPane.showMessageDialog(null,"El ID debe ser un numero entero!");
                return;
            }

            if (producto == null){

                if (validarProducto()){
                    String nombre = txtNombre.getText().trim();
                    String codigo = txtCodigo.getText().trim();
                    double precio = Double.parseDouble(txtPrecio.getText().trim());
                    int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                    String fecha = txtFechaVencimiento.getText();
                    producto = new Producto(id,nombre,codigo,precio,cantidad,fecha);
                } else {
                    return;
                }

                try {
                    productoDAO.añadirProducto(producto);
                    JOptionPane.showMessageDialog(null, "Se añadió el producto correctamente");
                    leerDatos();
                    limpiarDatos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo añadir el producto");
                    throw new RuntimeException(ex);
                }

            } else {

                if (validarProducto()){
                    String nombre = txtNombre.getText().trim();
                    String codigo = txtCodigo.getText().trim();
                    double precio = Double.parseDouble(txtPrecio.getText().trim());
                    int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                    String fecha = txtFechaVencimiento.getText();
                    producto = new Producto(id,nombre,codigo,precio,cantidad,fecha);
                } else {
                    return;
                }

                try {
                    productoDAO.actualizarProducto(producto);
                    JOptionPane.showMessageDialog(null, "Se actualizó el producto con exito");
                    leerDatos();
                    limpiarDatos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo el producto");
                    throw new RuntimeException(ex);
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            String aux = txtID.getText();
            try {
                int id = Integer.parseInt(aux);
                productoDAO.eliminarProducto(id);
                JOptionPane.showMessageDialog(null, "Se eliminó el producto correctamente");
                leerDatos();
                limpiarDatos();
            } catch (Exception error){
                JOptionPane.showMessageDialog(null, "No se encontró el producto que desea eliminar");
            }
        });

        this.add(lbID);
        this.add(lbNombre);
        this.add(lbCodigo);
        this.add(lbPrecio);
        this.add(lbCantidad);
        this.add(lbFechaVencimiento);
        this.add(txtID);
        this.add(txtNombre);
        this.add(txtCodigo);
        this.add(txtPrecio);
        this.add(txtCantidad);
        this.add(txtFechaVencimiento);
        this.add(btnGuardar);
        this.add(btnEliminar);
        this.add(scrollPane);
    }

    private boolean validarProducto(){
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()){
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacio");
            return false;
        }

        String codigo = txtCodigo.getText().trim();

        if (codigo.length() > 10){
            JOptionPane.showMessageDialog(null, "El codigo no puede pasar de 10"
            + " caracteres. Actualmente tiene " + codigo.length());
            return false;
        }

        String auxPrecio = txtPrecio.getText().trim();
        auxPrecio = auxPrecio.replace(",", ".");
        double precio;

        try {
            precio = Double.parseDouble(auxPrecio);
            if (precio < 0){
                JOptionPane.showMessageDialog(null, "El precio no puede ser negativo");
                return false;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "El precio debe tener solo números!");
            return false;
        }

        String auxCantidad = txtCantidad.getText().trim();
        int cantidad;

        try {
            cantidad = Integer.parseInt(auxCantidad);
            if (cantidad < 0){
                JOptionPane.showMessageDialog(null, "La cantidad no puede ser negativa");
                return false;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "La cantidad debe ser un número entero menor a "
            + Integer.MAX_VALUE);
            return false;
        }

        String fecha = txtFechaVencimiento.getText().trim();
        fecha = fecha.replace("/", "-");

        Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        Matcher matcher = pattern.matcher(fecha);

        if (!matcher.find()){
            JOptionPane.showMessageDialog(null, "El formato de fecha debe ser " +
                    " YYYY/MM/DD");
            return false;
        }

        return true;
    }

    private void iniciarJtable() {
        for (String s : productoDAO.columnas()) {
            defaultTableModel.addColumn(s);
        }

        leerDatos();

        jTable = new JTable(defaultTableModel);
        scrollPane = new JScrollPane(jTable);

        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = jTable.getSelectedRow();

                    int id = Integer.parseInt((String) defaultTableModel.getValueAt(fila, 0));
                    Producto producto = productoDAO.get(id);

                    txtID.setText(producto.id() + "");
                    txtNombre.setText(producto.nombre());
                    txtCodigo.setText(producto.codigo());
                    txtPrecio.setText(producto.precio() + "");
                    txtCantidad.setText(producto.cantidad() + "");
                    txtFechaVencimiento.setText(producto.fechaVencimiento());
                }
            }
        });
    }

    public void leerDatos() {
        defaultTableModel.setRowCount(0);
        for (Producto p : productoDAO.getProductos()) {
            defaultTableModel.addRow(p.getDatos());
        }
    }

    private void limpiarDatos(){
        txtID.setText("");
        txtNombre.setText("");
        txtCodigo.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtFechaVencimiento.setText("");
    }


}
