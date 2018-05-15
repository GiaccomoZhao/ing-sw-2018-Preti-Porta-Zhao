package porprezhas.RMI.Server;

import porprezhas.RMI.RemoteObservable;
import porprezhas.RMI.RemoteObserver;

import java.util.Observable;
import java.util.Observer;

public  class ProxyObserver implements Observer
    {

        private RemoteObserver remoteObserver;

        public ProxyObserver(RemoteObserver remoteObserver)
        {
            this.remoteObserver = remoteObserver;
        }

        public void update(Observable modelObs, Object arg)
        {
            try
            {
                    remoteObserver.update((RemoteObservable) modelObs, arg);
            }
            catch(Exception re)
            {
                System.err.println("Remote Observer error: " + re);
                re.printStackTrace();
            }
        }



    }

