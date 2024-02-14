package socketServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class socketServer extends IOException {
    public static void main(String[] args) throws IOException {
        final int PORT = 1250;

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting...");
            while (true) {
                Socket client = server.accept();
                System.out.println("Client from address: " + client.getInetAddress() + ", connected!");
                socketServerThread sst = new socketServerThread(client);
                sst.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
