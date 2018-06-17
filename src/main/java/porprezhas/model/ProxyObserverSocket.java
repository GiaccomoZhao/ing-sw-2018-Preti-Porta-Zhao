package porprezhas.model;


import porprezhas.Network.command.UpdateAnswer;

import java.io.ObjectOutputStream;

import java.util.Observable;
import java.util.Observer;

import static porprezhas.Network.socket.SocketServerClientHandler.lock;


public class ProxyObserverSocket implements Observer {

    private ObjectOutputStream out ;


    public ProxyObserverSocket(ObjectOutputStream objectOutputStream) {
        this.out=objectOutputStream;

    }
int i=0;
    public void update(Observable modelObs, Object arg)
    {

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
            System.err.println("Observer error: " + re);
            re.printStackTrace();
        }
    }
}
