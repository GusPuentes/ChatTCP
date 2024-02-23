import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class InterfazUsuario extends JFrame {
    private Socket cliente;
    private BufferedReader entrada;
    private PrintWriter salida;
    private JTextArea labelHistorialMensajes;
    private JTextField tvMensaje;
    private JTextField tvNombre;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                InterfazUsuario user = new InterfazUsuario();
            }
        });
    }

    public InterfazUsuario() {
            //Identificación del cliente
       String name = JOptionPane.showInputDialog("Inserte su nick");  //Pediremos identificación para entrar en la app
       setTitle("Conversación de : " + name);
            //Interfaz del cliente
       tvNombre = new JTextField(name);
       tvNombre.setEditable(false);
       tvMensaje = new JTextField();

       tvMensaje.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               envio(name + ": " + e.getActionCommand());
               tvMensaje.setText("");
           }
       });
       labelHistorialMensajes = new JTextArea();
       labelHistorialMensajes.setEditable(false);

       add(tvNombre,"North");
       add(tvMensaje,"South");
       add(new JScrollPane(labelHistorialMensajes),"Center");

       setSize(350,200);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setLocationRelativeTo(null);
       setVisible(true);
            //Conexión al server
       try {
           cliente = new Socket("localhost",12345);

           InputStreamReader entradaMensajes = new InputStreamReader(cliente.getInputStream());
           entrada = new BufferedReader(entradaMensajes);
           salida = new PrintWriter(cliente.getOutputStream(),true);
               //Recepción de mensajes desde el servidor
           new Thread(new Runnable(){
               public void  run(){recibirMEnsaje();}
           }).start();
       } catch (IOException e) {
           System.out.println(e.getMessage());
       }
    }
    private void envio(String mensaje){
        salida.println(mensaje);
    }

    private void recibirMEnsaje(){
        String msg;
        try {
            while ((msg = entrada.readLine()) != null) {
                labelHistorialMensajes.append(msg + "\n");
            }
        }catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
    }
}
