import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class Classifier {

	private final double k = 0.1;

	/**
	 * Number of classes to consider
	 */
	protected int NB_CLASSES = 4;

	/**
	 * % of the file to take to learn
	 */
	private final int NB_FILE_TO_TAKE_IN_LEARNING = 80;

	/**
	 * ??? contains number of word in the corpus.
	 */
	private final HashMap<String, Integer> dictionary;
	/**
	 * learning data
	 */
	private final ArrayList<ArrayList<Tweet>> learning = new ArrayList<ArrayList<Tweet>>();
	/**
	 * evaluation data
	 */
	private final ArrayList<ArrayList<Tweet>> evaluation = new ArrayList<ArrayList<Tweet>>();

	/**
	 * To give a unique identifier to all words
	 */
	private Integer nextId = 0;
	/**
	 * for the calculus
	 */
	double[] py;
	double[][] byw;
	double[] alpha;

	/**
	 * init dico and lists of tweets
	 */
	public Classifier() {
		dictionary = new HashMap<String, Integer>();
		for (int i = 0; i < NB_CLASSES; i++) {
			evaluation.add(new ArrayList<Tweet>());
			learning.add(new ArrayList<Tweet>());
		}
	}

	public void calculate() {
		py = new double[Main.NB_CLASSES];
		alpha = new double[Main.NB_CLASSES];
		byw = new double[Main.NB_CLASSES][dictionary.size()];

		final Long startCalculus = System.nanoTime();
		calculatePy();
		final Long endPy = System.nanoTime();
		System.out.println(">>> Time for Py: " + (endPy - startCalculus) / 1000000000 + " sec");
		calculateByw();
		final Long endByw = System.nanoTime();
		System.out.println(">>> Time for Byw: " + (endByw - endPy) / 1000000000 + " sec");
		calculateA();
		System.out.println(">>> Time for A: " + (System.nanoTime() - endByw) / 1000000000 + " sec");

	}

	/**
	 * Print stats about the performance (performance per class + confusion matrix)
	 */
	public void printResults() {
		calculateClass(evaluation);
		calculateAndDisplayConfusionMatrix(evaluation);
	}

	/**
	 * Load the datas
	 * @param f file do load
	 */
	public void load(final File f) {
		BufferedReader br = null;
		String line;

		try {
			br = new BufferedReader(new FileReader(f));

			Random r; // to split learning and evaluation parts
			int learningSize = 0;
			int evaluationSize = 0;
			// get tweets
			while ((line = br.readLine()) != null) {
				// cut line
				final Tweet tweet = getTweet(line);
				// draw a random number between 0 and 99
				r = new Random();
				// 80% of the tweets are used for the learning
				if (r.nextInt(99) < NB_FILE_TO_TAKE_IN_LEARNING) {
					learning.get(tweet.getPolarite()).add(tweet);
					learningSize++;
				} else {
					evaluation.get(tweet.getPolarite()).add(tweet);
					evaluationSize++;
				}
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
	 * get the object Tweet from a line
	 * 
	 * @param line
	 * @return a Tweet
	 */
	public Tweet getTweet(final String line) {
		final Integer middle = line.indexOf(")");
		String texte = line.substring(middle + 2, line.length());
		texte = filter(texte);
		final String polarite = line.substring(1, line.indexOf(','));
		int polariteCode;

		polariteCode = nti(polarite);

		final String marque = line.substring(line.indexOf(',') + 1, middle);

		// treat tokens
		final String[] tokens = texte.split(" ");
		final Integer[] words = getNumbersFromWords(tokens);

		// create tweet
		return new Tweet(polariteCode, marque, words);
	}

	public abstract String filter(String text);

	public abstract Integer nti(final String polarite);

	public abstract String itn(final Integer polarite);



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
			final Integer word = dictionary.get(token);
			if (word == null) {
				dictionary.put(token, nextId);
				words[cpt] = nextId;
				nextId++;
			} else {
				words[cpt] = word;
			}
			cpt++;
		}
		return words;
	}
	public void print(final ArrayList<Tweet> tweets) {
		for (final Tweet t : tweets) {
			System.out.println(t);
		}
	}

	public ArrayList<ArrayList<Tweet>> getLearning() {
		return learning;
	}

	public ArrayList<ArrayList<Tweet>> getEvaluation() {
		return evaluation;
	}

	public HashMap<String, Integer> getDictionary() {
		return dictionary;
	}

	/////////////// **** CALCULUS ****

	public void calculatePy() {
		int total = 0;
		int cpt = 0;
		// for all class
		for (final ArrayList<Tweet> alt : learning) {
			py[cpt] = alt.size();
			total += py[cpt];
			cpt++;
		}
		for (int i = 0; i < NB_CLASSES; i++) {
			py[i] = Math.log(py[i] / total);
		}
	}

	/**
	 * calculate Beta
	 */
	public void calculateByw() {
		// nb tweets in class i
		final double[] dy = new double[NB_CLASSES];
		for (int i = 0; i < NB_CLASSES; i++) {
			dy[i] = learning.get(i).size();
		}

		// content of hash table : (class number, (word number in dictionary, nb of occurrences of this word))
		final HashMap<Integer, HashMap<Integer, Integer>> occurrences = new HashMap<Integer, HashMap<Integer, Integer>>();
		// map initialization
		int classNb;
		for (classNb = 0; classNb < NB_CLASSES; classNb++) {
			occurrences.put(classNb, new HashMap<Integer, Integer>());
		}
		classNb = 0; // must start from the same number as in map initialization above

		// for all classes
		for (final ArrayList<Tweet> classe : learning) {
			// for all tweets in the class
			for (final Tweet tweet : classe) {
				// for all words in the tweet
				for (final Integer wordNb : tweet.getWords()) {
					final Integer wordOcc = occurrences.get(classNb).get(wordNb);

					if (wordOcc == null) { // first time to see the word in this class
						occurrences.get(classNb).put(wordNb, 1);
					} else {
						occurrences.get(classNb).put(wordNb, wordOcc + 1);
					}
				}
			}
			classNb++;
		}
		// filling of the beta table
		for (classNb = 0; classNb < NB_CLASSES; classNb++) {
			for (final int wordNb : dictionary.values()) {
				final Integer wordOcc = occurrences.get(classNb).get(wordNb);
				if (wordOcc == null) {
					byw[classNb][wordNb] = k / dy[classNb];
				} else {
					byw[classNb][wordNb] = Math.max(k, Math.min(wordOcc, dy[classNb] - k)) / dy[classNb];
				}
			}
		}
	}

	/**
	 * Calculate Alpha
	 */
	public void calculateA() {
		for (int i = 0; i < NB_CLASSES; i++) {
			// for all type we calculate a different alpha
			alpha[i] = 0;
			for (final Integer w : dictionary.values()) {
				alpha[i] += Math.log(1 - byw[i][w]);
			}
		}
	}

	/**
	 * Pre-calculated result
	 * 
	 * @param t
	 * @return
	 */
	public int calculatePxy2(final Tweet t) {
		final Integer[] tweetWords = t.getWords();
		final List<Integer> tweetWordsSet = new LinkedList<Integer>();
		for (final Integer tweetWord : tweetWords) {
			tweetWordsSet.add(tweetWord);
		}
		double logPXY;
		int classe = -1;
		double maxLogPXY = -Double.MAX_VALUE;
		for (int i = 0; i <= NB_CLASSES - 1; i++) {
			// for all class
			logPXY = alpha[i];
			for(final int word : t.getWords()) {
				logPXY += Math.log(byw[i][word])
						- Math.log(1 - byw[i][word]);
			}

			final double nouvelleValeur = logPXY + py[i];
			if (maxLogPXY < nouvelleValeur) {
				classe = i;
				maxLogPXY = nouvelleValeur;
			}
		}
		return classe;
	}

	/**
	 * 
	 * @param evaluationTweets
	 */
	public void calculateClass(
			final ArrayList<ArrayList<Tweet>> evaluationTweets) {
		double total = 0, ok = 0;
		final int okStats[] = new int[NB_CLASSES];
		final int totStats[] = new int[NB_CLASSES];
		for (int i = 0; i < NB_CLASSES; i++) {
			okStats[i] = 0;
			totStats[i] = 0;
		}
		for (final ArrayList<Tweet> alt : evaluationTweets) {
			for (final Tweet t : alt) {
				final int classe = calculatePxy2(t);
				if (classe != t.getPolarite()) {
				} else {
					okStats[t.getPolarite()]++;
					ok++;
				}
				total++;
				totStats[t.getPolarite()]++;
			}
		}
		System.out.println("taux erreur : " + (total - ok) * 100 / total + "%");
		for (int i = 0; i < NB_CLASSES; i++) {
			System.out.println("err Classe " + itn(i) + ": "
					+ (totStats[i] - okStats[i]) + "/" + totStats[i] + " => "
					+ (totStats[i] - okStats[i]) * 100 / totStats[i] + "%");
		}
	}

	////////////// *** DISPLAY ***
	public void displayPy() {
		System.out.println("PY :");
		for (int i = 0; i < NB_CLASSES; i++) {
			System.out.print(py[i] + " ");
		}
		System.out.println();
	}

	/**
	 * Display the Beta
	 */
	public void displayByw() {
		System.out.println("BYW :");
		for (int i = 0; i < dictionary.size(); i++) {
			System.out.print("\t" + i);
		}
		System.out.println();
		for (int i = 0; i < NB_CLASSES; i++) {
			// display class
			System.out.println(itn(i));

			for (int j = 0; j < dictionary.size(); j++) {
				System.out.print("\t" + byw[i][j]);
			}
			System.out.println();
		}
	}

	/**
	 * Display the confusion matrix
	 * @param evaluationTweets the tweets to evaluate
	 */
	public void calculateAndDisplayConfusionMatrix(
			final ArrayList<ArrayList<Tweet>> evaluationTweets) {
		final Integer[][] confusionMatrix = new Integer[NB_CLASSES][NB_CLASSES];
		for (int i = 0; i < NB_CLASSES; i++) {
			for (int j = 0; j < NB_CLASSES; j++) {
				confusionMatrix[i][j] = 0;
			}
		}
		for (final ArrayList<Tweet> classe : evaluationTweets) {
			for (final Tweet tweet : classe) {
				final int real = tweet.getPolarite();
				final int calculated = calculatePxy2(tweet);
				confusionMatrix[calculated][real] += 1;
			}
		}
		for(int i=0; i < NB_CLASSES; i++) {
			System.out.print("\t"+itn(i));
		}
		System.out.println();
		for (int i = 0; i < NB_CLASSES; i++) {
			System.out.print(itn(i));
			for (int j = 0; j < NB_CLASSES; j++) {
				System.out.print("\t" + confusionMatrix[i][j]);
			}
			System.out.println();
		}
	}

}