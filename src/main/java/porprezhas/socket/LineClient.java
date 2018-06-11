package porprezhas.socket;

import porprezhas.Network.CLISocketClient;

import java.io.*;
import java.net.InetAddress;

public class LineClient {

    public static void main(String[] args) throws IOException {
        CLISocketClient CLISocketClient = new CLISocketClient(InetAddress.getLocalHost(), 1457);
        CLISocketClient.run();
    }



}