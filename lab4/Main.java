package lab4;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Main class for the WordChains lab.
 */
public class Main {
	final static private int WordLength = 4;

	public static void main(String args[]) throws IOException {
		// Create a buffered reader for the user input.
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		// Build the word list.
		WordList.read(WordLength, stdin);
		LongestChain lc = new LongestChain(WordLength);
		while (true) {
			// Get a start word and an end word, or just an end word.
			String line = stdin.readLine();
			if (line == null) {
				break;
			}
			String tokens[] = line.split(" ");
			// Run BFS, which version depends on whether we have one
			// or two words.
			if (tokens.length == 1) {
				lc.checkAllStartWords(tokens[0]); 
			} else if (tokens.length == 2) {
				WordRec wr = lc.breadthFirst(tokens[0], tokens[1]);
				if (wr == null) {
					System.out.println(tokens[0] + " " + tokens[1] + ": ingen lösning");
				} else {
					System.out.println(tokens[0] + " " + tokens[1] + ": " + wr.chainLength() + " ord");
					wr.printChain();
				}
			} else {
				// Erroneous input.
				System.out.println("felaktig fråga: '" + line + "'");
				System.out.println("syntax följer frågor: slutord");
				System.out.println("eller:             startord slutord");
			}
		}
	}
}
