package alantheknight.lab6.common.utils;

import org.ini4j.Wini;

import java.io.IOException;

public class ConfigReader {
    private static Config config;

    public static synchronized Config getConfig() {
        if (config == null) {
            config = readConfig();
        }
        return config;
    }

    public static Config readConfig() {
        try {
            Wini ini = new Wini(ConfigReader.class.getResource("/config.ini"));
            System.out.println("Reading config file");

            return new Config(
                    ini.get("main", "data_path"),
                    ini.get("main", "port", Integer.class),
                    ini.get("main", "address"),
                    ini.get("main", "buffer_size", Integer.class),
                    new Config.ClientConfig(
                            ini.get("client", "max_recursion_level", Integer.class),
                            ini.get("client", "response_timeout", Integer.class)),
                    new Config.SSHConfig(
                            ini.get("ssh", "port", Integer.class),
                            ini.get("ssh", "host"),
                            ini.get("ssh", "password"),
                            ini.get("ssh", "user")
                    ),
                    new Config.PostgresConfig(
                            ini.get("postgres", "port", Integer.class),
                            ini.get("postgres", "host"),
                            ini.get("postgres", "password"),
                            ini.get("postgres", "user"),
                            ini.get("postgres", "database")
                    ),
                    new Config.ServerConfig(
                            ini.get("server", "remote_db", Boolean.class)
                    ));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config file: " + e.getMessage());
        }
    }
}
