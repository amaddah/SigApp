package sigmobile.sigapp.signet;

/**
 * Created by amaddah on 29/04/16.
 */

import java.io.IOException;
import java.net.DatagramPacket;

import sigmobile.sigapp.Inscription;

/**
 * Created by amaddah on 17/04/16.
 */
public class Server {

    private String IP;
    private int port;

    public Server(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    public String getSep() {
        return Inscription.sepc;
    }

    public int getPort() {
        return this.port;
    }

    public String getIP() {
        return this.IP;
    }

    public static String communicate(Client cl, String msg, int type) throws IOException {
        byte[] sendData = new byte[1024];
        byte[] r = new byte[1024];
        sendData = cl.msgConstruct(msg,type).getBytes();
        String response;

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, cl.IPAddress, cl.port);
        cl.getSocket().send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(r, r.length);
        cl.getSocket().setSoTimeout(2000);
        try {
            cl.getSocket().receive(receivePacket);
        } catch (Exception e) {
            response = "Nothing";
        }
        response = new String(receivePacket.getData());
        return response.trim();
    }

    public String createMsg(String t, String p) {
            return t + this.getSep() + p;
    }
}
