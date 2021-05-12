package server;

import objectprotocol.proxy.ClientObjectWorker;
import service.IService;

import java.net.Socket;

public class ObjectConcurrentServer extends AbstractConcurrentServer {
    private IService chatServer;
    public ObjectConcurrentServer(int port, IService chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker=new ClientObjectWorker(chatServer, client);
        chatServer.addObserver(worker);
        Thread tw=new Thread(worker);
        return tw;
    }


}