package Vista;

import javax.swing.*;

public class Ventana extends JFrame {

    private Panel panel;

    public Ventana(){
        init1();
    }

    private void init1() {
        setSize(1000,1000);
        setPreferredSize(this.getSize());
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new Panel(this.getSize());

        this.add(panel);
        this.pack();
    }

}
