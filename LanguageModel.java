import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        String window = "";
        char c;
        In in = new In(fileName);
        // Reads just enough characters to form the first window
        for (int i = 0; i < this.windowLength; i++) {
            window += in.readChar();
        }
        // Processes the entire text, one character at a time
        while (!in.isEmpty()) {
            // Gets the next character
            c = in.readChar();
            // Checks if the window is already in the map
            if(!CharDataMap.containsKey(window)){
                CharDataMap.put(window, new List());
            }
            CharDataMap.get(window).update(c);
        
            // Advances the window: adds c to the window’s end, and deletes the
            // window's first character.
            window = window.substring(1) + c;
        }
        // The entire file has been processed, and all the characters have been counted.
        // Proceeds to compute and set the p and cp fields of all the CharData objects
        // in each linked list in the map.
        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {				
		ListIterator itr = probs.listIterator(0);
        int totalChars = 0;
        while (itr.hasNext()) {
            CharData current = itr.next();
            totalChars += current.count;
        }

        double prevCp = 0;
		itr = probs.listIterator(0);
        while (itr.hasNext()) {
            CharData current = itr.next();
            current.p = current.count * 1.0 / totalChars;
            current.cp = prevCp + current.p;
            prevCp = current.cp;
        }        
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
		double r = randomGenerator.nextDouble();
		ListIterator itr = probs.listIterator(0);
        while (itr.hasNext()) {
            CharData current = itr.next();
            if (r < current.cp){
                return current.chr;
            }
        }
        throw new IndexOutOfBoundsException("not suppose to get here");
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		// Your code goes here
        return "";
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        // in this test we run the get random char a lot of times and cheking if we ger the expected results.
        List testList = new List();
        String testString = "committee_";
        for (int i = 0; i < testString.length(); i++){
            testList.update(testString.charAt(i));
        }
        LanguageModel newMode = new LanguageModel(0);
        newMode.calculateProbabilities(testList);
        int c = 0;
        int o = 0;
        int m = 0;
        int i_ = 0;
        int t = 0;
        int e = 0;
        int a_ = 0;
        int total = 0;
        for (int i = 0; i < 10000; i++) {
            total++;
            switch (newMode.getRandomChar(testList)) {
                case 'c':
                    c++;
                    break;
                case 'o':
                    o++;
                    break;
                case 'm':
                    m++;
                    break;
                case 'i':
                    i_++;
                    break;
                case 't':
                    t++;
                    break;
                case 'e':
                    e++;
                    break;
                case '_':
                    a_++;
                    break;           
            }
        }
        System.out.println("c: " + (double) c/total);
        System.out.println("o: " + (double) o/total);
        System.out.println("m: " + (double) m/total);
        System.out.println("i: " + (double) i_/total);
        System.out.println("t: " + (double) t/total);
        System.out.println("e: " + (double) e/total);
        System.out.println("_: " + (double) a_/total);

    }
}
