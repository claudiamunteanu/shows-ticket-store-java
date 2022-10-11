package app.network.utils;

import app.network.protobuffprotocol.ProtoAppWorker;
import app.services.IAppServices;

import java.net.Socket;

public class AppProtobuffConcurrentServer extends AbsConcurrentServer {
    private IAppServices chatServer;

    public AppProtobuffConcurrentServer(int port, IAppServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("App - AppProtobuffConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoAppWorker worker = new ProtoAppWorker(chatServer, client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
