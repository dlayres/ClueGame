/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C13A-1 Clue Paths
 */
package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BadConfigFormatException extends Exception {
	BadConfigFormatException() {
		super("BadConfigFormatError");
	}
	
	BadConfigFormatException(String message) {
		super(message);
		try {
			PrintWriter log = new PrintWriter("errorLogFile.txt");
			log.println(message);
			log.close();
		} catch (FileNotFoundException e){
			System.out.println(e.getMessage());
		}
	}
}
