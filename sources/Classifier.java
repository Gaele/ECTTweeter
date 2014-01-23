package sources;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class Classifier {

	/**
	 * Number of classes to consider
	 */
	protected int NB_CLASSES = 4;

	/**
	 * ??? contains number of word in the corpus.
	 */
	private final HashMap<String, Integer> dictionary;
	/**
	 * learning data
	 */
	//	private final ArrayList<ArrayList<Tweet>> learning = new ArrayList<ArrayList<Tweet>>();
	/**
	 * evaluation data
	 */
	//	private final ArrayList<ArrayList<Tweet>> evaluation = new ArrayList<ArrayList<Tweet>>();

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
	}

	public double crossValidation(final File f, final double k2) {

		final ArrayList<ArrayList<ArrayList<Tweet>>> datas = fileToArrayList(f, 10);
		final int size = datas.size();
		final double[] results = new double[size];
		// for all test
		for(int i=0; i<size; i++) {
			final ArrayList<ArrayList<Tweet>> learning = new ArrayList<ArrayList<Tweet>>();
			final ArrayList<ArrayList<Tweet>> test = new ArrayList<ArrayList<Tweet>>();
			for(int classe=0; classe<NB_CLASSES; classe++) {
				learning.add(classe, new ArrayList<Tweet>());
				test.add(classe, new ArrayList<Tweet>());
			}
			for(int j=0; j<size; j++) {
				if(i == j) {
					// test.addAll(datas.get(j));
					for(int classe=0; classe<NB_CLASSES; classe++) {
						test.get(classe).addAll(datas.get(j).get(classe));
					}
				} else {
					// learning.addAll(datas.get(j));
					for(int classe=0; classe<NB_CLASSES; classe++) {
						learning.get(classe).addAll(datas.get(j).get(classe));
					}
				}
			}
			// make calculus
			calculate(learning, k2, false);
			results[i] = calculateClass(test, false);
		}
		// print results
		int sum = 0;
		for(int i=0; i<size; i++) {
			sum += results[i];
		}
		//		System.out.println("Moyenne = " + (double)sum / size);

		return (double)sum / size;
	}

	public void calculate(final ArrayList<ArrayList<Tweet>> datas, final double k2, final boolean verbose) {
		py = new double[NB_CLASSES];
		alpha = new double[NB_CLASSES];
		byw = new double[NB_CLASSES][dictionary.size()];

		final Long startCalculus = System.nanoTime();
		calculatePy(datas);
		final Long endPy = System.nanoTime();
		if(verbose) {
			System.out.println(">>> Time for Py: " + (endPy - startCalculus) / 1000000000 + " sec");
		}
		calculateByw(datas, k2);
		final Long endByw = System.nanoTime();
		if(verbose) {
			System.out.println(">>> Time for Byw: " + (endByw - endPy) / 1000000000 + " sec");
		}
		calculateA();
		if(verbose) {
			System.out.println(">>> Time for A: " + (System.nanoTime() - endByw) / 1000000000 + " sec");
		}

	}

	/**
	 * Print stats about the performance (performance per class + confusion matrix)
	 */
	public void printResults(final ArrayList<ArrayList<Tweet>> evaluation) {
		calculateClass(evaluation, true);
		calculateAndDisplayConfusionMatrix(evaluation);
	}

	/**
	 * Load the datas
	 * @param f file do load
	 */
	public ArrayList<ArrayList<Tweet>> fileToArrayList(final File f) {
		BufferedReader br = null;
		String line;
		final ArrayList<ArrayList<Tweet>> datas = new ArrayList<ArrayList<Tweet>>();
		for (int i = 0; i < NB_CLASSES; i++) {
			datas.add(new ArrayList<Tweet>());
		}
		try {
			br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null) {
				final Tweet tweet = getTweet(line);
				datas.get(tweet.getPolarite()).add(tweet);
			}
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
		return datas;
	}

	/**
	 * 
	 * @param f
	 * @return
	 */
	public ArrayList<ArrayList<ArrayList<Tweet>>> fileToArrayList(final File f, final int nbArrayToCreate) {
		BufferedReader br = null;
		String line;
		ArrayList<ArrayList<Tweet>> datas = new ArrayList<ArrayList<Tweet>>();
		final ArrayList<ArrayList<ArrayList<Tweet>>> res = new ArrayList<ArrayList<ArrayList<Tweet>>>();


		try {
			int nbLines = 0;
			br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null) {
				nbLines++;
			}
			final int nbDataPerArray = nbLines / nbArrayToCreate;

			br = new BufferedReader(new FileReader(f));
			int cpt = 0;
			while ((line = br.readLine()) != null) {
				if(cpt%nbDataPerArray == 0) {
					if(cpt != 0) {
						res.add(datas);
					}
					datas = new ArrayList<ArrayList<Tweet>>();
					for (int i = 0; i < NB_CLASSES; i++) {
						datas.add(new ArrayList<Tweet>());
					}
				}
				final Tweet tweet = getTweet(line);
				datas.get(tweet.getPolarite()).add(tweet);
				cpt++;
			}
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
		return res;
	}

	/**
	 * get the object Tweet from a line
	 * 
	 * @param line
	 * @return a Tweet
	 */
	private Tweet getTweet(final String line) {
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

	public HashMap<String, Integer> getDictionary() {
		return dictionary;
	}

	/////////////// **** CALCULUS ****

	private void calculatePy(final ArrayList<ArrayList<Tweet>> datas) {
		int total = 0;
		int cpt = 0;
		// for all class
		for (final ArrayList<Tweet> alt : datas) {
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
	private void calculateByw(final ArrayList<ArrayList<Tweet>> datas, final double k2) {
		// nb tweets in class i
		final double[] dy = new double[NB_CLASSES];
		for (int i = 0; i < NB_CLASSES; i++) {
			dy[i] = datas.get(i).size();
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
		for (final ArrayList<Tweet> classe : datas) {
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
					byw[classNb][wordNb] = k2 / dy[classNb];
				} else {
					byw[classNb][wordNb] = Math.max(k2, Math.min(wordOcc, dy[classNb] - k2)) / dy[classNb];
				}
			}
		}
	}

	/**
	 * Calculate Alpha
	 */
	private void calculateA() {
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
	private int calculatePxy2(final Tweet t) {
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
				try {
					logPXY += Math.log(byw[i][word])
							- Math.log(1 - byw[i][word]);
				} catch(final ArrayIndexOutOfBoundsException e) {
					//					boolean found = false;
					//					for(final Entry<String, Integer> data : dictionary.entrySet()) {
					//						if(data.getValue() == word) {
					//							System.out.println("Unknown word " + data.getKey());
					//							found = true;
					//						}
					//					}
					//					if(!found) {
					//						System.out.println("BUG !!! " + word);
					//					}
				}
			}
			final double nouvelleValeur = logPXY + py[i];
			if (maxLogPXY < nouvelleValeur) {
				classe = i;
				maxLogPXY = nouvelleValeur;
			}
		}
		//		System.out.println("Unknown words: " + nbUnknownWords);
		return classe;
	}

	/**
	 * 
	 * @param evaluationTweets
	 */
	public double calculateClass(
			final ArrayList<ArrayList<Tweet>> evaluationTweets, final boolean verbose) {
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
		if(verbose) {
			System.out.println("taux erreur : " + (total - ok) * 100 / total + "%");
			for (int i = 0; i < NB_CLASSES; i++) {
				System.out.println("err Classe " + itn(i) + ": "
						+ (totStats[i] - okStats[i]) + "/" + totStats[i] + " => "
						+ (totStats[i] - okStats[i]) * 100 / totStats[i] + "%");
			}
			// words known and unknown
			final int nbWordsKnown = byw[0].length;
			final int totNbWords = dictionary.size();
			System.out.println("nb words: " + totNbWords);
			System.out.println("unknown words: " + ((double)totNbWords - nbWordsKnown) / totNbWords * 100);
		}

		return (total - ok) * 100 / total;
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

	public int getNB_CLASSES() {
		return NB_CLASSES;
	}

}
