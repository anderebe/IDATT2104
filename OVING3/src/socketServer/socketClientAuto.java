package socketServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class socketClientAuto {
    
    public static void main(String[] args) throws Exception {
        final int PORT = 1250;
        final String HOST = "localhost";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of clients: ");
        int numClients = scanner.nextInt();

        for (int i = 1; i <= numClients; i++) {
            final int clientNumber = i;
            Thread clientThread = new Thread(() -> {
                try {
                    int rounds = 0;
                    System.out.println("\nClient " + clientNumber + " started");
                    Socket socket = new Socket(HOST, PORT);
                    if (!socket.isConnected()) {
                        System.out.println("Client " + clientNumber + ": Server is not connected");
                        socket.close();
                        return;
                    }
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    while(rounds < 11){
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                        if (serverResponse.contains("Write the first number: ")) {
                            out.println(1);
                        } else if (serverResponse.contains("Write the second number: ")) {
                            out.println(2);
                        } else if (serverResponse.contains("Write either '+' or '-' to add or subtract the numbers: ")) {
                            if(rounds == 10){
                                out.println("exit");
                                break;
                            }
                            out.println('+');
                        } 
                    }
                    rounds++;
                    }

                    socket.close();
                    System.out.println("Client " + clientNumber + " finished\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            clientThread.start();
        }

        scanner.close();
    }
}