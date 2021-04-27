package common;

public class Config {
    public static final String IP = "localhost";
    public static final int PORT = 8082;
    public static final String DB_HOST = "pg";
    public static final int DB_PORT = 2908;
    public static final String ENV_VAR = "LAB5";
    public static final String CRYPTO = "MD2";
    public static final String TOP_SECRET_FILE = "top_secret";
    public static final boolean HELIOS = true;
    public static final String jdbcLocal = String.format("jdbc:postgresql://%s:%d/studs", HELIOS? DB_HOST: IP, HELIOS? 5432: DB_PORT);
}
