package porprezhas.model;

import porprezhas.Network.rmi.common.RemoteObserver;
import porprezhas.Network.rmi.common.ServerRMIInterface;
import porprezhas.control.ServerController;
import porprezhas.control.ServerControllerInterface;
import porprezhas.model.SerializableGameInterface;

import java.net.InetAddress;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public  class ProxyObserverRMI extends TimerTask implements Observer
    {

        private RemoteObserver remoteObserver;
        private Registry registry;
        private ServerControllerInterface serverControllerInterface;
        private String username;
        private  Timer timer;
        private String ipClient;
        private int portClient;
        public ProxyObserverRMI(String username, ServerControllerInterface serverControllerInterface) throws RemoteException, NotBoundException {
            List<String> listParam= serverControllerInterface.getClientRmiAddress(username);
            this.ipClient=listParam.get(0);
            this.portClient=Integer.parseInt(listParam.get(1));
            registry= LocateRegistry.getRegistry(ipClient, portClient);
            this.username=username;
            this.serverControllerInterface=serverControllerInterface;
            this.remoteObserver = (RemoteObserver) registry.lookup(username);
            timer = new Timer();
            timer.schedule(this, 0, 5000);

        }

        public void update(Observable modelObs, Object arg)
        {
            try
            {

                remoteObserver.update((SerializableGameInterface) arg);
            }
            catch(Exception re)
            {
                if(re instanceof ConnectException)
                    serverControllerInterface.closedConnection(username);
                else
                    re.printStackTrace();
            }
        }


        @Override
        public void run() {
            try {

                remoteObserver.checkState();
            } catch (RemoteException e) {
                if(e instanceof ConnectException) {
                    serverControllerInterface.closedConnection(username);
                    this.timer.cancel();
                }
                else
                    e.printStackTrace();
            }
        }
    }

