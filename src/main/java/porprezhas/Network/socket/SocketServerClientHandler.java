package porprezhas.Network.socket;

import porprezhas.Network.command.*;
import porprezhas.model.ProxyObserverSocket;

import java.io.EOFException;
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
    private Boolean first = true;
    public static Object lock;
    private Boolean test = true;

    public int getState() {
        return state;
    }

    public SocketServerClientHandler(Socket socket, ActionHandler serverController) throws IOException {
        this.socket = socket;
        this.serverController = serverController;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        lock = new Object();
    }


    public void run() {

        try {
            while (test) {
                Action action = ((Action) in.readObject());
                if (first)
                    ((LoginAction) action).setObjectOutputStream(out);
                Answer answer = action.handle(serverController);
                if (first)
                    if (((LoginActionAnswer) answer).answer.equals(true))
                        first = false;
                synchronized (lock) {
                    out.reset();
                    out.writeObject(answer);
                    out.flush();
                }

            }
        } catch (IOException e) {
            if( e instanceof EOFException)
                System.out.println("Invalid username");
            else
                e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
