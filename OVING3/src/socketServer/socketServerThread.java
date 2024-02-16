package socketServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class socketServerThread extends Thread{
    private final Socket client;
    private boolean running = true;

    public socketServerThread(Socket newClient) {
        this.client = newClient;
    }
    
    public void run() {
        
        try {
            InputStreamReader in = new InputStreamReader(client.getInputStream());
            BufferedReader bf = new BufferedReader(in);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            out.println("\nWelcome to the server!\n");

            while(running){
            try {
                out.println("Write the first number: ");
                int num1 = Integer.parseInt(bf.readLine());

                out.println("Write the second number: ");
                int num2 = Integer.parseInt(bf.readLine());

                out.println("Write either '+' or '-' to add or subtract the numbers: ");
                String operation = bf.readLine();

                if(operation.equals("exit")){
                    out.println("Goodbye!");
                    break;
                }

                int result = operation(num1, num2, operation);
                out.println("The result is: " + result + "\n");
                System.out.println("Client from address: " + client.getInetAddress() + ", calculation completed!");
            } catch (NumberFormatException e) {
                out.println("Invalid number format. Please enter valid integers.\n");
            } catch (IllegalArgumentException e) {
                out.println("Invalid operation. Please enter '+' or '-'.\n");
            }

            }

            in.close();
            out.close();
            bf.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client from address: " + client.getInetAddress() + ", disconnected...");
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

