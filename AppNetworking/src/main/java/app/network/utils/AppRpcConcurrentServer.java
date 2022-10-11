package app.network.utils;

import app.network.rpcprotocol.AppClientRpcReflectionWorker;
import app.services.IAppServices;

import java.net.Socket;


public class AppRpcConcurrentServer extends app.network.utils.AbsConcurrentServer {
    private IAppServices appServer;
    public AppRpcConcurrentServer(int port, IAppServices appServer) {
        super(port);
        this.appServer = appServer;
        System.out.println("App - AppRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AppClientRpcReflectionWorker worker=new AppClientRpcReflectionWorker(appServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
