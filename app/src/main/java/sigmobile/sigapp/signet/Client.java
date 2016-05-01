package sigmobile.sigapp.signet;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by amaddah on 29/04/16.
 */

/**
 * Created by amaddah on 11/04/16.
 */
public class Client {
    int port;
    DatagramSocket socket;
    InetAddress IPAddress;

    public Client(String host, int port) throws IOException {
        this.port = port;
        this.IPAddress = InetAddress.getByName(host);
        this.socket = new DatagramSocket();
    }

    public String msgConstruct(String msg, int type){
        // Construit le message grace à la classe paquet
        Paquet p = new Paquet(type, msg);
        return p.prepare();
    }

    public DatagramSocket getSocket(){
        return this.socket;
    }

    public boolean closeSocket() throws IOException {
        this.getSocket().close();
        return this.getSocket().isClosed();
    }
}
