package porprezhas.Network;


import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.util.Observable;
import java.util.Observer;


public class ProxyObserverSocket implements Observer {

    private Socket socket;
    private ObjectOutputStream out ;


    public ProxyObserverSocket(Socket socket) throws IOException {
        this.socket=socket;
        this.out= (ObjectOutputStream) socket.getOutputStream();
    }

    public void update(Observable modelObs, Object arg)
    {
        try
        {
            out.writeObject(arg);
            out.flush();

        }
        catch(Exception re)
        {
            System.err.println("Observer error: " + re);
            re.printStackTrace();
        }
    }
}
