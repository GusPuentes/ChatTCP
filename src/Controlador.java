import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
public class Controlador implements Runnable{
    private Socket socket;
    private BufferedReader entrada;
    private Server server;
    public Controlador(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void run() {
        String msg;
            try {
                while((msg = entrada.readLine())!=null){
                    server.conver += msg+ "\n";
                    server.enviarMensaje(msg);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
    }
}
