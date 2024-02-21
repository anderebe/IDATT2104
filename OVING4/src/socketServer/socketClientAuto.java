package socketServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class socketClientAuto {
    
    public static void main(String[] args) throws Exception {
        final String HOST = "localhost";
        final int PORT = 80;
        final int BUFFER_SIZE = 1024;
        final int TIMEOUT = 5000; // 5 seconds
        final int THREAD_TIMEOUT = 10000; // 10 seconds
        boolean running = true;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of clients: ");
        int numClients = scanner.nextInt();

        for (int i = 1; i <= numClients; i++) {
            final int clientNumber = i;
            Thread clientThread = new Thread(() -> {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    socket.setSoTimeout(TIMEOUT);
                    InetAddress address = InetAddress.getByName(HOST);
                    byte[] buffer = new byte[BUFFER_SIZE];

                    long startTime = System.currentTimeMillis();

                    while (running && System.currentTimeMillis() - startTime < THREAD_TIMEOUT) {
                        System.out.println("\nClient " + clientNumber + " started");
                        String clientMSG = "2 + 2"; // Modify this line to mimic the desired math calculation
                        byte[] requestData = clientMSG.getBytes();

                        DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, address, PORT);
                        socket.send(requestPacket);
                        System.out.println("Client " + clientNumber + " sent request: " + clientMSG);

                        DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                        
                        socket.receive(responsePacket);

                        String serverMSG = new String(responsePacket.getData(), 0, responsePacket.getLength());
                        System.out.println("Client " + clientNumber + " received response: " + serverMSG);

                        Thread.sleep(1000); // Add a delay of 1 second before sending the next request
                    }
                    socket.close();
                    System.out.println("Client " + clientNumber + " finished\n");
                } catch (IOException e) {
                    System.out.println("Server is not responding. Please try again later.");
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted.");
                }
            });

            clientThread.start();
        }

        scanner.close();
    }
}