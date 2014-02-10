package sources2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * The abstract manager is responsible for data loading and setting
 * optimisation.
 * 
 * @author vincent
 * 
 */
public abstract class AbstractManager {

	/**
	 * Number of final classes
	 */
	protected int NB_CLASSES = 0;

	/**
	 * Local dictionary to manage local words. This is a sub-part of the global
	 * dictionary but code needs new unique identifiers to avoid
	 * "arrayOutOfBounds" in byw.
	 */
	private final HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
	/**
	 * Occurrences of words
	 */
	private final HashMap<String, Integer> occurrences = new HashMap<String, Integer>();

	/**
	 * To give a unique identifier to all words of this manager, in the
	 * {@link #dictionary}
	 */
	private Integer nextId = 0;

	/**
	 * Learn datas from the file
	 * 
	 * @param datas
	 *            the datas to use, ListOfClasses<ListOfTweetsFor1Class<Tweet>>
	 * @param k
	 *            the "k" parameter to use
	 * @param verbose
	 *            diplays infos if true
	 */
	public abstract void learn(ArrayList<ArrayList<Tweet>> datas,
			final double k, final boolean verbose);

	/**
	 * Calculates the languages of the datasTest
	 * 
	 * @param dataTest
	 *            data to analyse
	 * @param verbose
	 *            diplays infos if true
	 * @return analysed data,
	 *         ListOfDerivedClasses<ListOfTweetsFor1DerivedClass<Tweet>>
	 */
	public abstract ArrayList<ArrayList<Tweet>> work(
			final ArrayList<Tweet> dataTest, final boolean verbose);

	/**
	 * Print performance
	 * 
	 * @param res
	 *            the data analysed
	 * @param verbose
	 *            displays infos if true
	 * @return the accuracy in %
	 */
	public abstract double check(final ArrayList<ArrayList<Tweet>> res,
			final boolean verbose);

	/**
	 * Transform the text before it's translated by any classifier or manager
	 * 
	 * @param text
	 *            Text to transform
	 * @return transformed text
	 */
	protected abstract String filter(String text);

	/**
	 * Natural to Integer for the second data of the tweets, gives the code of
	 * the second data
	 * 
	 * @param marque
	 *            the text of the second data
	 * @return the code of the second data
	 */
	protected abstract int nti2(String marque);

	/**
	 * Natural to Integer, gives the code of a final classe
	 * 
	 * @param polarite
	 *            the text of the final classe
	 * @return the code of the final classe
	 */
	public abstract Integer nti(final String polarite);

	/**
	 * Integer to Natural, gives the string representation of a final classe
	 * code.
	 * 
	 * @param polarite
	 *            the code of the final classe
	 * @return the string description of the final classe
	 */
	public abstract String itn(final Integer polarite);

	/**
	 * Make the system learn 10 times on 9/10 of a file and test on the last
	 * 1/10.
	 * 
	 * @param f
	 *            file to use
	 * @param k
	 *            the k parameter
	 * @param verbose
	 *            diplays infos if true
	 * @return the average accuracy of the system
	 */
	public double crossValidation(final File f, final double k,
			final boolean verbose) {
		final ArrayList<ArrayList<ArrayList<Tweet>>> datas = this
				.fileToArrayList(f, 10);
		final int size = datas.size();
		final double[] results = new double[size];
		// for all test
		for (int i = 0; i < size; i++) {
			final ArrayList<ArrayList<Tweet>> learning = new ArrayList<ArrayList<Tweet>>();
			final ArrayList<Tweet> test = new ArrayList<Tweet>();
			for (int classe = 0; classe < this.NB_CLASSES; classe++) {
				learning.add(classe, new ArrayList<Tweet>());
			}
			for (int j = 0; j < size; j++) {
				if (i == j) {
					for (int classe = 0; classe < this.NB_CLASSES; classe++) {
						test.addAll(datas.get(j).get(classe));
					}
				} else {
					for (int classe = 0; classe < this.NB_CLASSES; classe++) {
						learning.get(classe).addAll(datas.get(j).get(classe));
					}
				}
			}
			// make calculus
			this.learn(learning, k, verbose);
			final ArrayList<ArrayList<Tweet>> res = this.work(test, true);
			results[i] = this.check(res, verbose);
			System.out.print("-");
			System.out.flush();
		}
		System.out.println();
		// print results
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum += results[i];
		}
		return (double) sum / size;
	}

	/**
	 * Load the datas in a ListOfClasses<ListOfTweetsPerCLasse<Tweet>>, for
	 * learning()
	 * 
	 * @param f
	 *            file do load
	 */
	public ArrayList<ArrayList<Tweet>> fileToArrayList(final File f) {
		BufferedReader br = null;
		String line;
		final ArrayList<ArrayList<Tweet>> datas = new ArrayList<ArrayList<Tweet>>();
		for (int i = 0; i < this.NB_CLASSES; i++) {
			datas.add(new ArrayList<Tweet>());
		}
		try {
			br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null) {
				final Tweet tweet = this.getTweet(line);
				datas.get(tweet.getPolarit()).add(tweet);
			}
			// rare words are removed from the dictionary
			for (final Entry<String, Integer> entry : this.occurrences
					.entrySet()) {
				if (entry.getValue() <= 10) {
					this.dictionary.remove(entry.getKey());
				}
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
	 * Load the datas in a ListOfTweetsPerClasse<Tweet>, for working()
	 * 
	 * @param f
	 *            file do load
	 * @return datas to use,
	 */
	public ArrayList<Tweet> fileToSimpleArrayList(final File f) {
		BufferedReader br = null;
		String line;
		final ArrayList<Tweet> datas = new ArrayList<Tweet>();
		try {
			br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null) {
				final Tweet tweet = this.getTweet(line);
				datas.add(tweet);
			}
			// rare words are removed from the dictionary
			for (final Entry<String, Integer> entry : this.occurrences
					.entrySet()) {
				if (entry.getValue() <= 10) {
					this.dictionary.remove(entry.getKey());
				}
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
	 * Load file into nbOfBlocks<ListOfClasses<ListOfTweetsPerClasse<Tweet>>>
	 * for cross validation
	 * 
	 * @param f
	 *            file to load
	 * @return datas to use,
	 *         nbOfBlocks<ListOfClasses<ListOfTweetsPerCLasse<Tweet>>>
	 */
	public ArrayList<ArrayList<ArrayList<Tweet>>> fileToArrayList(final File f,
			final int nbArrayToCreate) {
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
			br.close();
			br = new BufferedReader(new FileReader(f));
			int cpt = 0;
			while ((line = br.readLine()) != null) {
				if (cpt % nbDataPerArray == 0) {
					if (cpt != 0) {
						res.add(datas);
					}
					datas = new ArrayList<ArrayList<Tweet>>();
					for (int i = 0; i < this.NB_CLASSES; i++) {
						datas.add(new ArrayList<Tweet>());
					}
				}
				final Tweet tweet = this.getTweet(line);
				datas.get(tweet.getPolarit()).add(tweet);
				cpt++;
			}
			// rare words are removed from the dictionary
			for (final Entry<String, Integer> entry : this.occurrences
					.entrySet()) {
				if (entry.getValue() <= 10) {
					this.dictionary.remove(entry.getKey());
				}
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
		texte = this.filter(texte);
		final String polarite = line.substring(1, line.indexOf(','));
		final String marque = line.substring(line.indexOf(',') + 1, middle);

		// treat tokens
		final String[] tokens = texte.split(" ");
		final Integer[] words = this.getNumbersFromWords(tokens, 2, false);

		// create tweet
		return new Tweet(this.nti(polarite), this.nti2(marque), words);
	}

	/**
	 * Fills the dictionary <String, Integer> with n-grams from a tweet and
	 * returns the code of each n-gram in a table. Also fills the occurrence
	 * map.
	 * 
	 * @param tweet
	 *            Tokenizer (the list of words)
	 * @param n
	 *            number of words or letters in an n-gram
	 * @param letterNgram
	 *            false to make n-grams of words, true to make n-grams of
	 *            letters
	 * @return tab with the codes of the n-grams in the dictionary
	 */
	public Integer[] getNumbersFromWords(final String[] tweet, final int n,
			final boolean letterNgram) {

		final int numberOfWords = tweet.length;

		// n-grams of words
		if (letterNgram == false) {
			// number of n-grams of words in the tweet
			// = n (numberOfWords + numberOfWords - n + 1) / 2
			// = n (2 * numberOfWords - n + 1) / 2
			// example : n = 3, numberOfWords = 5 : n-grams = 5 + 4 + 3
			final int numberOfNGrams = n * (2 * numberOfWords - n + 1) / 2;
			final Integer[] nGrams = new Integer[numberOfNGrams];

			int cpt = 0;
			for (int i = 1; i <= n; i++) {
				for (int j = 0; j < tweet.length - i + 1; j++) {
					String nGram = null;
					if (i == 1) {
						nGram = tweet[j];
					} else {
						// concatenation of the n-gram (without spaces between
						// words)
						final StringBuilder sb = new StringBuilder();
						for (int k = 0; k < i; k++) {
							sb.append(tweet[j + k]);
						}
						nGram = sb.toString();
					}
					final Integer nGramNumber = this.dictionary.get(nGram);
					if (nGramNumber == null) {
						this.dictionary.put(nGram, this.nextId);
						this.occurrences.put(nGram, 1);
						nGrams[cpt] = this.nextId;
						this.nextId++;
					} else {
						nGrams[cpt] = nGramNumber;
						final int occ = this.occurrences.get(nGram);
						this.occurrences.put(nGram, occ + 1);
					}
					cpt++;
				}
			}
			return nGrams;
		}
		// n-grams of letters (each token considered separately, without spaces)
		else {
			final ArrayList<Integer> nGrams = new ArrayList<Integer>();
			for (final String token : tweet) {
				final char[] tokenChar = token.toCharArray();
				for (int i = 1; i <= n; i++) {
					for (int j = 0; j < tokenChar.length - i + 1; j++) {
						// concatenation of the n-gram
						final StringBuilder sb = new StringBuilder();
						for (int k = 0; k < i; k++) {
							sb.append(tokenChar[j + k]);
						}
						final String nGram = sb.toString();
						final Integer nGramNumber = this.dictionary.get(nGram);
						if (nGramNumber == null) {
							this.dictionary.put(nGram, this.nextId);
							this.occurrences.put(nGram, 1);
							nGrams.add(this.nextId);
							this.nextId++;
						} else {
							nGrams.add(nGramNumber);
							final int occ = this.occurrences.get(nGram);
							this.occurrences.put(nGram, occ + 1);
						}
					}
				}
			}
			return nGrams.toArray(new Integer[nGrams.size()]);
		}
	}

	/**
	 * Calculates the best k parameter with dichotomy
	 * 
	 * @param trainFile
	 *            file used to train
	 * @param min
	 *            the minimum value of the interval
	 * @param max
	 *            the maximum value of the interval
	 * @param limit
	 *            the accuracy of the calculus
	 * @return the best k to use
	 */
	public double calculateMin(final File trainFile, final double min,
			final double max, final double limit) {
		final double middle = (max + min) / 2;
		if (middle - min < limit) {
			return middle;
		}
		final double littleMiddle = (middle + min) / 2;
		final double bigMiddle = (max + middle) / 2;
		final double littleMiddleValue = this.crossValidation(trainFile,
				littleMiddle, false);
		final double bigMiddleValue = this.crossValidation(trainFile,
				bigMiddle, false);

		if (littleMiddleValue < bigMiddleValue) {
			System.out.println("k=" + littleMiddle + " => " + littleMiddleValue
					+ " *");
			System.out.println("k=" + bigMiddle + " => " + bigMiddleValue);
			System.out.println("-----");
			return this.calculateMin(trainFile, min, middle, limit);
		} else {
			System.out
					.println("k=" + littleMiddle + " => " + littleMiddleValue);
			System.out.println("k=" + bigMiddle + " => " + bigMiddleValue
					+ " *");
			System.out.println("-----");
			return this.calculateMin(trainFile, middle, max, limit);
		}

	}

	public double calculateMinStrongly(final File trainFile, final double min,
			final double max, final double limit) {
		System.out.println("min=" + min + ", max=" + max + ", limit=" + limit);
		final int NB_BLOCKS = 10;
		final double step = (max - min) / NB_BLOCKS;
		if (step < limit) {
			return (max - min) / 2;
		}

		double currentK = min;
		double minK = Double.MAX_VALUE;
		double minValue = Double.MAX_VALUE;
		double currentValue = Double.MAX_VALUE;
		for (int i = 0; i < NB_BLOCKS; i++) {
			// minVal[i] = currentK;
			currentValue = this.crossValidation(trainFile, currentK, false);
			if (currentValue < minValue) {
				minK = currentK;
				minValue = currentValue;
			}
			System.out.println("k=" + currentK + " => " + currentValue);
			currentK += step;
		}
		System.out.println("keep k=" + minK);

		return this.calculateMinStrongly(trainFile, Math.max(minK - step, min),
				minK + step, limit);
	}

	/**
	 * getter for the dictionary
	 * 
	 * @return the dictionary
	 */
	public HashMap<String, Integer> getDictionary() {
		return this.dictionary;
	}

}
