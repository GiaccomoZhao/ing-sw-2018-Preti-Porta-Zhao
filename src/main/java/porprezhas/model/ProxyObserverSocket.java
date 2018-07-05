package porprezhas.model;


import porprezhas.Network.command.CardEffectAnswer;
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
               // re.printStackTrace();
            }

    }

    public void handleCardUpdate(Object object){
        try
        {

            CardEffectAnswer cardEffectAnswer = new CardEffectAnswer(object);
            synchronized (lock){

                out.reset();
                out.writeObject(cardEffectAnswer);
                out.flush();
            }

        }
        catch(Exception re)
        {
            re.printStackTrace();
        }
    }

}
