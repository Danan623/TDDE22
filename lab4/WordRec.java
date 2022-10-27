package lab4;



/** 
 * WordRec represents a word and their parent in the graph, and can be
 * (is) used to represent word chains.
 */ 
class WordRec {
    private String word;
    private WordRec parent; // adress till parentnoden
    public WordRec(String word_, WordRec parent_) {
	word = word_;
	parent = parent_;
    }

    /**
     * Calculates and returns the amount of words in this particular
     * chain of words. 
     *
     * @return - the word count in the chain beginning with this word
     * (int)
     */
    public int chainLength() {
	int i = 0;
	for (WordRec x = this; x != null; x = x.parent) {
	    i++;
	}
	return i;
    }

    /**
     * Helper method for formating the print of a word chain.
     */ 
    private void printChainHelp() {
	if (parent != null) {
	    parent.printChainHelp();
	}
	printWord();

	System.out.print(" -> ");
    }

    /**
     * Print the chain of words recursively. 
     */
    public void printChain() {
   
	if (parent != null) {
	    parent.printChainHelp();
	}

	printWord();
	System.out.println();
    }

    /** 
     * Print the word in this particular WordRec object.
     */ 
    public void printWord() {
	System.out.print(word);
    }

    /**
     * Helper method for formating the print of a word chain in reverse order.
     */ 
    public void printChainHelpRev() {
	if (parent != null) {
		printWord();
		System.out.print(" -> ");
		parent.printChainHelpRev();
		
	}else {
		printWord();
		System.out.println();
	}
	
		
    }

    /**
     * Get the word in this particular WordRec object.
     *
     * @return - the word contained in this WordRec object (String)
     */ 
    public String getWord() {
	return word;
    }

    /**
     * Get the parent, when this WordRec was created during BFS, of
     * the word in this particular WordRec object.
     *
     * @return - the parent of this WordRec from the BFS (WordRec)
     */ 
    public WordRec getParent() {
	return parent;
    }
	
}
