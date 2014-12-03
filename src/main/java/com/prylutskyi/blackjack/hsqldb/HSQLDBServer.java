package com.prylutskyi.blackjack.hsqldb;

import org.apache.log4j.Logger;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;
import org.springframework.context.SmartLifecycle;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Patap on 02.12.2014.
 */
public class HSQLDBServer implements SmartLifecycle {
    private static final Logger LOGGER = Logger.getLogger(HSQLDBServer.class);
    private HsqlProperties properties;
    private Server server;
    private boolean running = false;

    public HSQLDBServer(Properties props) {
        properties = new HsqlProperties(props);
        start();
    }

    @Override
    public boolean isRunning() {
        if (server != null)
            server.checkRunning(running);
        return running;
    }

    @Override
    public void start() {
        if (server == null) {
            LOGGER.info("Starting HSQL server...");
            server = new Server();
            try {
                server.setProperties(properties);
                server.start();
                running = true;
            } catch (ServerAcl.AclFormatException | IOException afe) {
                LOGGER.error("Error starting HSQL server.", afe);
            }
        }
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping HSQL server...");
        if (server != null) {
            server.stop();
            running = false;
        }
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public boolean isAutoStartup() {
        return false;
    }

    @Override
    public void stop(Runnable runnable) {
        stop();
        runnable.run();
    }
}
