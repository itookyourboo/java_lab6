package common;

public class Config {
    public static final String IP = "127.0.0.1";
    public static final int PORT = 8080;
    public static final String DB_HOST = "pg";
    public static final int DB_PORT = 2908;
    public static final String ENV_VAR = "LAB5";
    public static final String CRYPTO = "MD2";
    public static final String TOP_SECRET_FILE = "top_secret";
    public static final String jdbcLocal = String.format("jdbc:postgresql://localhost:%d/studs", DB_PORT);

    public static String getJdbcUrl() {
        return String.format("jdbc:postgresql://%s:%d/studs", DB_HOST, PORT);
    }
}
