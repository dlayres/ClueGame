/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C13A-1 Clue Paths
 */
package clueGame;

public class BadConfigFormatException extends Exception {
	BadConfigFormatException() {
		super("BadConfigFormatError");
	}
	
	BadConfigFormatException(String message) {
		super(message);
	}
}
