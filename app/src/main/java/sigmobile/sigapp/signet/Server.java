package sigmobile.sigapp.signet;

/**
 * Created by amaddah on 29/04/16.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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

    public String createMsg(String nom, String societe, String mdp, Boolean withEmail, String email) {
        if (withEmail)
            return nom + this.getSep() + societe + this.getSep() + mdp + this.getSep() + email;
        else
            return nom + this.getSep() + societe + this.getSep() + mdp;
    }

    public String createMsg(String token, String balise) {
        return token + this.getSep() + balise;
    }
}
