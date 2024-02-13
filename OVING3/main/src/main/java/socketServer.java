import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class socketServer extends Thread {
    private Socket client;
    public socketServer(Socket client) {
        this.client = client;
    }

    public static void main(String[] args) throws IOException {
        final int PORT = 1250;

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting..."); 
             while (true) {
                Socket client = server.accept();
                System.out.println("Client connected");
                Thread clientThread = new Thread(new socketServer(client));
                clientThread.start();
            }
        }
    }

    public void run() {
        try {
            InputStreamReader in = new InputStreamReader(client.getInputStream());
            BufferedReader bf = new BufferedReader(in);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            out.println("\nWelcome to the server!\n");

            try {
                out.println("Write the first number: ");
                int num1 = Integer.parseInt(bf.readLine());

                out.println("Write the second number: ");
                int num2 = Integer.parseInt(bf.readLine());

                out.println("Write either '+' or '-' to add or subtract the numbers: ");
                String operation = bf.readLine();

                int result = operation(num1, num2, operation);
                out.println("The result is: " + result + "\n");
            } catch (NumberFormatException e) {
                out.println("Invalid number format. Please enter valid integers.\n");
            } catch (IllegalArgumentException e) {
                out.println("Invalid operation. Please enter '+' or '-'.\n");
            }

            in.close();
            out.close();
            bf.close();
            client.close();
            System.out.println("Server closing....\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client disconnected");
    }

    public static int operation(int num1, int num2, String operation) {
        if (operation.equals("+")) {
            return num1 + num2;
        } else if (operation.equals("-")) {
            return num1 - num2;
        } else {
            return 0;
        }
    }
}
