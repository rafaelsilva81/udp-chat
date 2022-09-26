package Server;
/* UDP SERVER IN JAVA */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerConnection extends Thread {

    /* create socket */
    private DatagramSocket socket;

    /* create buffer */
    private byte[] buf = new byte[256];

    /* create arraylist of connected sockets */
    private ArrayList<SocketAddress> connectedSockets = new ArrayList<SocketAddress>();

    public ServerConnection() throws SocketException {
        /* initialize socket on port 4445 */
        socket = new DatagramSocket(4445);
        System.out.println("Server started on port 4445");
    }

    @Override
    public void run() {
        while (true) {
            /* create packet */
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                /* receive packet */
                socket.receive(packet);
                /* System.out.println(connectedSockets); */
                /* print packet */
                System.out.println("Received packet from " + packet.getAddress().getHostAddress() + " : "
                        + new String(packet.getData()));
                /* add socket to list of connected sockets if its not there yet */
                if (!connectedSockets.contains(packet.getSocketAddress())) {
                    connectedSockets.add(packet.getSocketAddress());
                }
                /* send back packet to all connected sockets */
                for (SocketAddress socketAddress : connectedSockets) {
                    packet.setSocketAddress(socketAddress);
                    socket.send(packet);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /* close socket */
        // socket.close();
    }

    public static void main(String[] args) throws SocketException {
        new ServerConnection().start();
    }

}