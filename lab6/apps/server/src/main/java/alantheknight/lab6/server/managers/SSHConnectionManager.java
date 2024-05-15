package alantheknight.lab6.server.managers;

import alantheknight.lab6.common.utils.Config;
import alantheknight.lab6.common.utils.ConfigReader;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import static alantheknight.lab6.server.Main.logger;

public class SSHConnectionManager {
    private static Session session;

    public static void createSSHTunnel() throws SSHConnectionException {
        JSch jsch = new JSch();
        Config config = ConfigReader.getConfig();

        try {
            session = jsch.getSession(config.sshConfig().user(), config.sshConfig().host(), config.sshConfig().port());
            session.setPassword(config.sshConfig().password());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            session.setPortForwardingL(
                    config.postgresConfig().port(),
                    config.postgresConfig().host(),
                    config.postgresConfig().port());

            logger.info("SSH tunnel created: {}:{} -> {}:{}", config.sshConfig().host(), config.sshConfig().port(), config.postgresConfig().host(), config.postgresConfig().port());
        } catch (JSchException e) {
            throw new SSHConnectionException("Failed to create SSH tunnel: " + e.getMessage());
        }
    }

    public static void disconnectSSHTunnel() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            logger.info("SSH tunnel disconnected");
        }
    }

    public static class SSHConnectionException extends Exception {
        public SSHConnectionException(String message) {
            super(message);
        }
    }
}
