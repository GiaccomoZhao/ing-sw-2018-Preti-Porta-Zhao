package porprezhas.Network;

import porprezhas.RMI.RemoteObservable;
import porprezhas.RMI.RemoteObserver;
import porprezhas.model.SerializableGameInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Observable;
import java.util.Observer;

public  class ProxyObserverRMI implements Observer
    {

        private RemoteObserver remoteObserver;
        private Registry registry;

        public ProxyObserverRMI(String username) throws RemoteException, NotBoundException {
            registry= LocateRegistry.getRegistry();
            this.remoteObserver = (RemoteObserver) registry.lookup(username);  ;

        }

        public void update(Observable modelObs, Object arg)
        {
            try
            {

                remoteObserver.update((SerializableGameInterface) arg);
            }
            catch(Exception re)
            {
                System.err.println("Remote Observer error: " + re);
                re.printStackTrace();
            }
        }



    }

