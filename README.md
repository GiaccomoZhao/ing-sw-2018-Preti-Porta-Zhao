## Prova Finale Ingegneria del Software A.A. 2017/2018 - Gianpaolo Cugola

### 1. Composizione del gruppo
  * * Cognome: Preti
    * Nome: Matteo
    * Matricola: 845884
    * Codice Persona: 10523717
    
  * * Cognome: Zhao
    * Nome: Xiang
    * Matricola: 848129
    * Codice Persona: 10454297
    
  * * Cognome: Porta
    * Davide: Davide Maria
    * Matricola: 846777
    * Codice Persona: 10494506
    
### [2. Coverage dei test](https://github.com/GiaccomoZhao/ing-sw-2018-Preti-Porta-Zhao/tree/master/test%20coverage)
### [3. Diagrammi UML delle classi](https://github.com/GiaccomoZhao/ing-sw-2018-Preti-Porta-Zhao/tree/master/UML%20finali)

### 4. Funzionalità implementate
 *  * Regole Complete
    * CLI
    * RMI
    * Socket
    * GUI
    * SinglePlayer
    * Partite Multiple
### 5.Note relative al codice

Rete.
    Abbiamo implementato sia RMI che Socket.
    Abbiamo cercato di uniformare il modo in cui le due diverse tipologie di connessione
    permettono al client e al server di comunicare tra di loro.
    E’ presente un singleton(Client Action Singleton) che crea e restituisce un istanza
    di una delle due classi che implementano ClientAction Singleton (Socket client action e Rmi client action).
    Tutte le chiamate che il client fa  verso il server passano da quell’interfaccia.
    Le rispettive classi si occupano di creare il command (nel caso di socket) o di
    gestire i parametri per la chiamata remota (nel caso di rmi).
    Allo stesso tempo la maggior parte delle notifiche che arrivano dal server verso
    il client sono inviate a viewUpdateHandlerInterface (interfaccia implementata da una classe per cli e da una classe per socket) che si occupa di gestirle.
    Nella cartella command sono presenti i command(Action e Answer) che utilizzano client
    e server in modalità socket.

    ProxyObserverRmi e proxyObserverSocket sono le due classi che implementano
    observer e si occupano di notificare il client quando lo richiede il model secondo il pattern observer.

