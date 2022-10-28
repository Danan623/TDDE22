package lab4;

import java.util.HashSet;

/**
 * The LongestChain class contains the necessary data to perform breadth first
 * searches in accordance with the laboration, and determine the longest chain.
 *
 * @author Magnus Nielsen, Tommy FÃ¤rnqvist
 */
class LongestChain {
	private Queue q; // queue used in the BFS
	private String goalWord; // goal word of the BFS
	private int wordLength;
	private final char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'å', 'ä', 'ö', 'é' };
	private int alphabetLength = alphabet.length;
	private WordRec tmpWr;

	public LongestChain(int wordLength) {
		this.wordLength = wordLength;
		q = new Queue();

	}

	/**
	 * IsGoal checks if w is the goal word.
	 *
	 * @param w - the word to compare with the goal word (String)
	 * @return - true if w is the goal word, false otherwise (boolean)
	 */
	private boolean isGoal(String w) {
		return w.equals(goalWord);
	}

	/**
	 * makeChildren creates all words that differ in one letter from the given word
	 * x. If the word is encountered for the first time it will be added to the
	 * queue "q" for later processing.
	 *
	 * @param x - the word of origin (WordRec)
	 * @return the goal word if it's found, null otherwise (WordRec)
	 */
	private WordRec makeChildren(WordRec x) {
		char[] tmpWord;
		char tmpC;
		String res;

		for (int i = 0; i < wordLength; i++) {
			tmpWord = x.getWord().toCharArray();

				tmpC = tmpWord[i];

			for (char c : alphabet) {

				if (c != tmpC) {
					tmpWord[i] = c;
					res = WordList.contains(new String(tmpWord));

					if (res != null && WordList.markAsUsedIfUnused(res)) {
						WordRec wr = new WordRec(res, x);

						if (isGoal(res)) {
							return wr;
						}
						tmpWr = wr;
						q.enqueue(wr);

					}
				}
			}

		}
		return null;
	}

	/**
	 * BreadthFirst perform a BFS from startWord to find the shortest path to
	 * endWord. The shortest path will be returned as a chain of word posts
	 * (WordRec). If there is no path between the two words, null will be returned.
	 *
	 * @param startWord - the word from which we wish to begin the search (String)
	 * @param endWord   - the goal word, the end of the search (String)
	 * @return - a chain of word posts that can be traversed if the goal word is
	 *         found, null otherwise (WordRec)
	 */
	public WordRec breadthFirst(String startWord, String endWord) {

		WordList.eraseUsed();
		WordRec start = new WordRec(startWord, null);
		WordList.markAsUsedIfUnused(startWord);
	
			goalWord = endWord;
	
		q.empty();
		q.enqueue(start);

		try {
			while (true) {
				WordRec wr = makeChildren((WordRec) q.dequeue());
				if (wr != null) {
					return wr;
				}
			}
		} catch (Exception e) {

			return null;
		}

	}

	/**
	 * CheckAllStartWords finds the shortest path from any word to endWord. The
	 * longest of these shortest paths will be printed.
	 *
	 * @param endWord - the goal word, where we wish for all the paths to end
	 */
	public void checkAllStartWords(String endWord) {
	/**
	* If endword does not exist: quit
	*/
		if (WordList.contains(endWord) == null) {
			System.out.println(endWord + ": ingen lösning");
			return;
		}
		tmpWr = null; // "clear" prev. adress
		breadthFirst(endWord, null);
		if (tmpWr != null) {
			System.out.println(endWord + ": " + tmpWr.chainLength() + " ord");
			tmpWr.printChainHelpRev();// END -> START
		} else {
			System.out.println(endWord + ": ingen lösning");
		}

	}
}
