package Game;
public class PalworldException extends Exception {
    public PalworldException(String message) {
        super(message);
    }

    public PalworldException(String message, Throwable cause) {
        super(message, cause);
    }
}