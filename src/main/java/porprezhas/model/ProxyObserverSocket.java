package porprezhas.model;


import porprezhas.Network.command.UpdateAnswer;
import porprezhas.control.ServerController;
import porprezhas.control.ServerControllerInterface;

import java.io.ObjectOutputStream;

import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;

import static porprezhas.Network.socket.SocketServerClientHandler.lock;


public class ProxyObserverSocket implements Observer {

    private String username;
    private ObjectOutputStream out ;
    private ServerControllerInterface serverControllerInterface;


    public ProxyObserverSocket(String username, ObjectOutputStream objectOutputStream, ServerControllerInterface serverControllerInterface) {
        this.out=objectOutputStream;
        this.serverControllerInterface=serverControllerInterface;
        this.username=username;

    }
int i=0;
    public void update(Observable modelObs, Object arg)
    {

        if(true){
            try
            {
                SerializableGameInterface serializableGameInterface= (SerializableGame) arg;

                UpdateAnswer updateAnswer= new UpdateAnswer(serializableGameInterface);
                synchronized (lock){

                    out.reset();
                    out.writeObject(updateAnswer);
                    out.flush();
                }

            }
            catch(Exception re)
            {
                if(re instanceof SocketException)
                    serverControllerInterface.closedConnection(username);

            }
        }
    }


}
