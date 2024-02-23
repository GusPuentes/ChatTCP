import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket servidor;
    private ExecutorService ejecutor;
    private int cont;
    private List<PrintWriter> listaClientes;
    public String conver;

    public static void main(String[] args) {
        Server server = new Server();
    }

    public Server() {
        cont = 0;
        listaClientes = new ArrayList<>();
        ejecutor = Executors.newCachedThreadPool();
        try {
            servidor = new ServerSocket(12345);
            while(true){
                Socket socket = servidor.accept();
                PrintWriter salida = new PrintWriter(socket.getOutputStream(),true);
                listaClientes.add(salida);

                ejecutor.execute(new Controlador(socket,this));
                if(conver!=null){
                    enviarMensaje(cont,conver);
                }
                cont++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            ejecutor.shutdown();
        }
    }
    public void enviarMensaje(int pos, String msg){
        listaClientes.get(pos).println(msg);
    }
    public void enviarMensaje(String msg){
        for(PrintWriter cliente : listaClientes){
            cliente.println(msg);
        }
    }
}
