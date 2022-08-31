package com.bahar.server;

import com.bahar.service.OrderServiceImpl;
import com.bahar.service.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderServer {

    private static final Logger logger = Logger.getLogger(UserServer.class.getName());

    private Server server;

    public void StartServer()  {

        int port =50053;
        try {
            server = ServerBuilder.forPort(port)
                    .addService(new OrderServiceImpl())
                    .build().start();
            logger.info(" server started on port 50053. ");

            // invoke the stopServer method was made in this class.
            Runtime.getRuntime().addShutdownHook(new Thread(){

                @Override
                public void run() {
                    logger.info("clean server showdown and JVM handle it... ");
                    try {
                        OrderServer.this.StopServer();
                    } catch (InterruptedException exception) {
                        logger.log(Level.SEVERE,"server shutdown is interrupted...",exception);
                    }
                }
            });
        } catch (IOException exception) {
            logger.log(Level.SEVERE,"server did not start...",exception);
        }
    }

    public void StopServer() throws InterruptedException {

        if(server!= null){
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void BlockUntilShowdown() throws InterruptedException{
        if(server!= null){
            server.awaitTermination(); // server keep running and never stop.
        }
    }

    public static void main(String[] args) throws InterruptedException {

        OrderServer orderServer = new OrderServer();
        orderServer.StartServer();
//        userServer.StopServer();
        orderServer.BlockUntilShowdown();
    }


}
