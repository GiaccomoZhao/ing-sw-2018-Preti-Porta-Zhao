package porprezhas.Network;

public class ClientActionSingleton {
    private volatile static ClientActionInterface clientActionInstance;     // volatile guarantee the sub-atomic action of new are done in order
                                                                    // (new() => 1. alloc, 2. constructor initialize, 3. give instance alloc-ed memory)
    private ClientActionSingleton(){}       // default constructor

    public static ClientActionInterface getClientAction() { // not synchronize here for multi-thread performance
        if (clientActionInstance == null) {     // double check for thread safe
            synchronized (ClientActionSingleton.class) {
                if (clientActionInstance == null) {
                    clientActionInstance = new RMIClientAction();    // default value
                }
            }
        }
        return clientActionInstance;
    }

    public static void setClientActionInstance(ClientActionInterface clientActionInstance) {
        ClientActionSingleton.clientActionInstance = clientActionInstance;
    }
}
