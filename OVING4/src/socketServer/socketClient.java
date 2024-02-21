package socketServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class socketClient {
    
    public static void main(String[] args) throws Exception {
        final String HOST = "localhost";
        final int PORT = 80;
        final int BUFFER_SIZE = 1024;
        final int TIMEOUT = 5000; // 5 seconds
        boolean running = true;

        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);
            InetAddress address = InetAddress.getByName(HOST);
            byte[] buffer = new byte[BUFFER_SIZE];

            while (running) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter a math calculation: ");
                String clientMSG = scanner.nextLine();
                byte[] requestData = clientMSG.getBytes();

                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, address, PORT);
                socket.send(requestPacket);
                System.out.println("Sent request: " + clientMSG);

                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                
                socket.receive(responsePacket);

                String serverMSG = new String(responsePacket.getData(), 0, responsePacket.getLength());
                System.out.println("Received response: " + serverMSG);

                System.out.print("Do you want to continue? (y/n): ");
                String answer = scanner.nextLine();
                if (answer.equals("n")) {
                    System.out.println("Goodbye!");
                    running = false;
                }
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("Server is not responding. Please try again later.");
        }
    }
}