package clueGame;

public class BadConfigFormatException extends Exception {
	BadConfigFormatException() {
		super("BadConfigFormatError");
	}
	
	BadConfigFormatException(String message) {
		super(message);
	}
}
