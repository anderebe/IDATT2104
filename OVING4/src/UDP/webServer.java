package UDP;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class webServer {
    public static void main(String[] args) throws IOException {
        final int PORT = 80;
        final int BUFFER_SIZE = 1024;

        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            System.out.println("Server started and awaits connections...");

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String clientMSG = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received request: " + clientMSG + ", from: " 
                                                    + packet.getAddress() + ":" + packet.getPort() + ", at: " 
                                                    + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

                if (!clientMSG.isEmpty() && clientMSG.matches(".*[0-9+\\-*/].*")) {
                    String response = String.valueOf(CalculatorService.solve(clientMSG));
                    byte[] responseData = response.getBytes();

                    DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, packet.getAddress(), packet.getPort());
                    socket.send(responsePacket);

                    System.out.println("Sent response: " + response + "\n");
                } else {
                    String response = "Invalid expression!\n";
                    byte[] responseData = response.getBytes();

                    DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, packet.getAddress(), packet.getPort());
                    socket.send(responsePacket);

                    System.out.println("Sent response: " + response);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
