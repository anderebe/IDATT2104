package socketServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class socketClient {
    public static void main(String[] args) throws Exception {
        final int PORT = 1250;
        final String HOST = "localhost";

        Socket socket = new Socket(HOST, PORT);
        if(!socket.isConnected()){
            System.out.println("\nServer is not connected");
        } 
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Scanner scanner = new Scanner(System.in);

        String serverResponse;
        while ((serverResponse = in.readLine()) != null) {
            System.out.println(serverResponse);
            if (serverResponse.contains("Write the first number: ")) {
                int num1 = scanner.nextInt();
                out.println(num1);
            } else if (serverResponse.contains("Write the second number: ")) {
                int num2 = scanner.nextInt();
                out.println(num2);
            } else if (serverResponse.contains("Write either '+' or '-' to add or subtract the numbers: ")) {
                String operation = scanner.next();
                out.println(operation);
            }
        }
        scanner.close();
        socket.close();
    }
}