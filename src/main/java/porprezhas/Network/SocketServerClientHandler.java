package porprezhas.Network;

import porprezhas.Network.ProxyObserverSocket;
import porprezhas.control.ServerControllerInterface;
import porprezhas.model.Game;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Observable;

public class SocketServerClientHandler extends Observable implements Runnable {
    private ServerControllerInterface serverController;
    private Socket socket;
    private int state = 0;
    private ProxyObserverSocket proxyObserverSocket;
    public int getState() {
        return state;
    }

    public SocketServerClientHandler(Socket socket, ServerControllerInterface serverController) {
        this.socket = socket;
        this.serverController= serverController;
    }

    public SocketServerClientHandler(Socket socket) {
        this.socket = socket;

    }

    public void run() {

        try {
            this.addObserver(new ProxyObserverSocket(socket));
        } catch (IOException e) {
            e.printStackTrace();
        }




// chiudo gli stream e il socket
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}