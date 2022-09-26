package Client;
/* UDP CLIENT IN JAVA */

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientConnection extends Thread {

    /* create socket */
    private DatagramSocket socket;

    /* create address */
    private InetAddress address;

    /* create port */
    private int port;

    /* isRunning */
    public boolean isRunning = false;

    public ClientConnection(String ip, int port) throws IOException {
        isRunning = true;
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException | SocketException e) {
            throw new IOException("Could not connect to server");
        }
        /* initialize port */
        this.port = port;

        System.out.println("Client started on port " + socket.getLocalPort());
    }

    /* connect to server */
    public void connect(String name) throws IOException {
        /* create packet */
        String msg = name + " connected" + getTimeStamp();
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length,
                address,
                port);
        /* send packet */
        socket.send(packet);
    }

    /* send message */
    public void send(String msg) {
        msg = msg + getTimeStamp();
        /* create packet */
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, port);
        try {
            /* send packet */
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* create a receive method */
    public String receive() {
        /* create buffer */
        byte[] buf = new byte[256];
        /* create packet */
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            /* receive packet */
            socket.receive(packet);
            /* print packet */
            System.out.println("Received packet from " + packet.getAddress().getHostAddress() + " : "
                    + new String(packet.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(packet.getData());
    }

    /* method to get current timestamp as string */
    public String getTimeStamp() {
        /* format : (HH:mm:ss) */
        return " (" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + ") ";
    }

}