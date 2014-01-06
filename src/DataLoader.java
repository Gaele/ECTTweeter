import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Load data from files
 * 
 * @author vvanhec
 * 
 */
public class DataLoader {

	/**
	 * Number of classes to consider
	 */
	private final int NB_CLASSES = 4;
	/**
	 * % of the file to take to learn
	 */
	private final int NB_FILE_TO_TAKE_IN_LEARNING = 80;

	/**
	 * contains number of word in the corpus.
	 */
	private final HashMap<String, Integer> dictionary;

	/**
	 * learning data
	 */
	final ArrayList<ArrayList<Tweet>> learning = new ArrayList<ArrayList<Tweet>>();
	/**
	 * evaluation data
	 */
	final ArrayList<ArrayList<Tweet>> evaluation = new ArrayList<ArrayList<Tweet>>();

	// for the calculus
	// regarder dans le corpus combien on a de classes

	private Integer nextId = 0;

	public DataLoader() {
		this.dictionary = new HashMap<String, Integer>();
		for (int i = 0; i < this.NB_CLASSES; i++) {
			this.evaluation.add(new ArrayList<Tweet>());
			this.learning.add(new ArrayList<Tweet>());
		}
	}

	public void loadWordsFromFile(final File f) {
		BufferedReader br = null;
		String line;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					f.getAbsolutePath())));

			int proba;
			Random r;
			// get tweets
			while ((line = br.readLine()) != null) {
				// cut line
				final Tweet tweet = this.getTweet(line);
				// draw a random number between 0 and 99
				r = new Random();
				proba = r.nextInt(99);
				// 80% of the tweets are used for the learning
				if (proba < this.NB_FILE_TO_TAKE_IN_LEARNING) {
					this.learning.get(tweet.getPolarite()).add(tweet);
				} else {
					this.evaluation.get(tweet.getPolarite()).add(tweet);
				}
			}

			// debug
			// this.print(learning);
			int learningSize = 0;
			for (final ArrayList<Tweet> alt : this.learning) {
				learningSize += alt.size();
			}
			int evaluationSize = 0;
			for (final ArrayList<Tweet> alt : this.evaluation) {
				evaluationSize += alt.size();
			}
			System.out.println("Learning: " + learningSize + ", soit: "
					+ learningSize * 100 / (evaluationSize + learningSize)
					+ "%");
			System.out.println("Evaluation: " + evaluationSize + ", soit: "
					+ evaluationSize * 100 / (evaluationSize + learningSize)
					+ "%");
			System.out.println();

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Fill the dictionary <String, Integer> and return the number of each word
	 * in the string
	 * 
	 * @param t
	 *            Tokenizer (the list of words)
	 * @return tab with the number of the words in the dictionary
	 */
	public Integer[] getNumbersFromWords(final String[] t) {
		final int numberOfWords = t.length;
		final Integer[] words = new Integer[numberOfWords];
		int cpt = 0;
		for (final String token : t) {
			final Integer word = this.dictionary.get(token);
			if (word == null) {
				this.dictionary.put(token, this.nextId);
				words[cpt] = this.nextId;
				this.nextId++;
			} else {
				words[cpt] = word;
			}
			cpt++;
		}
		return words;
	}

	/**
	 * get the object Tweet from a line
	 * 
	 * @param line
	 * @return a Tweet
	 */
	public Tweet getTweet(final String line) {
		final Integer middle = line.indexOf(")");
		String texte = line.substring(middle + 2, line.length());
		texte = this.filter(texte); // /////////////////// ??
		final String polarite = line.substring(1, line.indexOf(','));
		int polariteCode;
		// A revoir !
		switch (polarite) {
		case "positive":
			polariteCode = 0;
			break;
		case "negative":
			polariteCode = 1;
			break;
		case "neutral":
			polariteCode = 2;
			break;
		case "irrelevant":
			polariteCode = 3;
			break;
		default:
			polariteCode = -1;
			break;
		}
		final String marque = line.substring(line.indexOf(',') + 1, middle);

		// treat tokens
		final String[] tokens = texte.split(" ");
		final Integer[] words = this.getNumbersFromWords(tokens);

		// create tweet
		return new Tweet(polariteCode, marque, words);
	}

	public String filter(final String words) {
		// words = "";
		// final String[] tokens = words.split(" ");
		return words;
	}

	public void print(final ArrayList<Tweet> tweets) {
		for (final Tweet t : tweets) {
			System.out.println(t);
		}
	}

	public ArrayList<ArrayList<Tweet>> getLearning() {
		return this.learning;
	}

	public ArrayList<ArrayList<Tweet>> getEvaluation() {
		return this.evaluation;
	}

	public HashMap<String, Integer> getDictionary() {
		return this.dictionary;
	}

}
