package porprezhas.Network;

import porprezhas.Network.Command.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

public class SocketServerClientHandler extends Observable implements Runnable {
    private ActionHandler serverController;
    private Socket socket;
    private int state = 0;
    private ProxyObserverSocket proxyObserverSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private Boolean first=true;

    public int getState() {
        return state;
    }

    public SocketServerClientHandler(Socket socket, ActionHandler serverController) throws IOException {
        this.socket = socket;
        this.serverController= serverController;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }


    public void run() {
        while(true) {
            try {
                Action action = ((Action) in.readObject());
                if (first)
                    ((LoginAction) action).setObjectOutputStream(out);
                Answer answer = action.handle(serverController);
                if (first)
                    if (((LoginActionAnswer) answer).answer.equals(true))
                        first = false;
                out.writeObject(answer);
                out.flush();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        /*try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}