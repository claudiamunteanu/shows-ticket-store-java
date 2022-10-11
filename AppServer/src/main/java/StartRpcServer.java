import app.network.utils.AbstractServer;
import app.network.utils.AppRpcConcurrentServer;
import app.network.utils.ServerException;
import app.persistence.ShowsDBRepository;
import app.persistence.TicketsDBRepository;
import app.persistence.UsersDBRepository;
import app.persistence.interfaces.ShowRepository;
import app.persistence.interfaces.TicketRepository;
import app.persistence.interfaces.UserRepository;
import app.server.AppService;
import app.server.validators.TicketValidator;
import app.server.validators.UserValidator;
import app.services.IAppServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/appserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }
        ShowRepository showRepository = new ShowsDBRepository(serverProps);
        TicketRepository ticketRepository = new TicketsDBRepository(serverProps);
        UserRepository userRepository = new UsersDBRepository(serverProps);
        UserValidator userValidator = new UserValidator();
        TicketValidator ticketValidator = new TicketValidator();

        IAppServices appServer = new AppService(showRepository, ticketRepository, userRepository, userValidator, ticketValidator);
        
        int appServerPort=defaultPort;
        try {
            appServerPort = Integer.parseInt(serverProps.getProperty("app.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+appServerPort);
        AbstractServer server = new AppRpcConcurrentServer(appServerPort, appServer);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}

