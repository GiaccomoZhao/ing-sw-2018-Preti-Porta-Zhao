package porprezhas.Network.socket;

import porprezhas.Network.command.*;
import porprezhas.control.ServerController;
import porprezhas.control.ServerControllerInterface;
import porprezhas.model.ProxyObserverSocket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

import static porprezhas.control.ServerController.USERNAME_ALREADY_TAKEN;

public class SocketServerClientHandler extends Observable implements Runnable {
    private ActionHandler serverController;
    private Socket socket;
    private String username;
    private ProxyObserverSocket proxyObserverSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private Boolean first = true;
    public static Object lock;
    private Boolean test = true;



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
                    if (!(((LoginActionAnswer) answer).answer==USERNAME_ALREADY_TAKEN)) {
                        first = false;
                        this.username=((LoginActionAnswer) answer).username;
                }
                synchronized (lock) {
                    out.reset();
                    out.writeObject(answer);
                    out.flush();
                }

            }
        } catch (IOException e) {
            if( e instanceof EOFException)
                System.out.println("Invalid username");
            else if(e instanceof SocketException) {
                System.out.println("Client closed the connection");
                ((ServerController) serverController).closedConnection(username);
            }
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
              //  ((ServerControllerInterface) serverController).closedConnection(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
