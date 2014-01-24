package sources2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The abstract manager is responsible for data loading and setting optimisation.
 * @author vincent
 *
 */
public abstract class AbstractManager {

	/**
	 * Number of final classes
	 */
	protected int NB_CLASSES = 0;

	/**
	 * Local dictionary to manage local words. This is a sub-part of the global dictionary but code needs new unique identifiers to avoid "arrayOutOfBounds" in byw.
	 */
	private final HashMap<String, Integer> dictionary = new HashMap<String, Integer>();

	/**
	 * To give a unique identifier to all words of this manager, in the {@link #dictionary}
	 */
	private Integer nextId = 0;

	/**
	 * Learn datas from the file
	 * @param datas the datas to use, ListOfClasses<ListOfTweetsFor1Class<Tweet>>
	 * @param k the "k" parameter to use
	 * @param verbose diplays infos if true
	 */
	public abstract void learn(ArrayList<ArrayList<Tweet>> datas, final double k, final boolean verbose);

	/**
	 * Calculates the languages of the datasTest
	 * @param dataTest data to analyse
	 * @param verbose diplays infos if true
	 * @return analysed data, ListOfDerivedClasses<ListOfTweetsFor1DerivedClass<Tweet>>
	 */
	public abstract ArrayList<ArrayList<Tweet>> work(final ArrayList<Tweet> dataTest, final boolean verbose);

	/**
	 * Print performance
	 * @param res the data analysed
	 * @param verbose displays infos if true
	 * @return the accuracy in %
	 */
	public abstract double check(final ArrayList<ArrayList<Tweet>> res, final boolean verbose);

	/**
	 * Transform the text before it's translated by any classifier or manager
	 * @param text Text to transform
	 * @return transformed text
	 */
	protected abstract String filter(String text);

	/**
	 * Natural to Integer for the second data of the tweets, gives the code of the second data
	 * @param marque the text of the second data
	 * @return the code of the second data
	 */
	protected abstract int nti2(String marque);

	/**
	 * Natural to Integer, gives the code of a final classe
	 * @param polarite the text of the final classe
	 * @return the code of the final classe
	 */
	public abstract Integer nti(final String polarite);

	/**
	 * Integer to Natural, gives the string representation of a final classe code.
	 * @param polarite the code of the final classe
	 * @return the string description of the final classe
	 */
	public abstract String itn(final Integer polarite);

	/**
	 * Make the system learn 10 times on 9/10 of a file and test on the last 1/10.
	 * @param f file to use
	 * @param k the k parameter
	 * @param verbose diplays infos if true
	 * @return the average accuracy of the system
	 */
	public double crossValidation(final File f, final double k, final boolean verbose) {
		final ArrayList<ArrayList<ArrayList<Tweet>>> datas = fileToArrayList(f, 10);
		final int size = datas.size();
		final double[] results = new double[size];
		// for all test
		for(int i=0; i<size; i++) {
			final ArrayList<ArrayList<Tweet>> learning = new ArrayList<ArrayList<Tweet>>();
			final ArrayList<Tweet> test = new ArrayList<Tweet>();
			for(int classe=0; classe<NB_CLASSES; classe++) {
				learning.add(classe, new ArrayList<Tweet>());
			}
			for(int j=0; j<size; j++) {
				if(i == j) {
					for(int classe=0; classe<NB_CLASSES; classe++) {
						test.addAll(datas.get(j).get(classe));
					}
				} else {
					for(int classe=0; classe<NB_CLASSES; classe++) {
						learning.get(classe).addAll(datas.get(j).get(classe));
					}
				}
			}
			// make calculus
			learn(learning, k, verbose);
			final ArrayList<ArrayList<Tweet>> res = work(test, true);
			results[i] = check(res, verbose);
			System.out.print("-");
			System.out.flush();
		}
		System.out.println();
		// print results
		int sum = 0;
		for(int i=0; i<size; i++) {
			sum += results[i];
		}
		return (double)sum / size;
	}

	/**
	 * Load the datas in a ListOfClasses<ListOfTweetsPerCLasse<Tweet>>, for learning()
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
				datas.get(tweet.getPolarit()).add(tweet);
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
	 * @param f file do load
	 * @return datas to use,
	 */
	public ArrayList<Tweet> fileToSimpleArrayList(final File f) {
		BufferedReader br = null;
		String line;
		final ArrayList<Tweet> datas = new ArrayList<Tweet>();
		try {
			br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null) {
				final Tweet tweet = getTweet(line);
				datas.add(tweet);
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
	 * Load file into nbOfBlocks<ListOfClasses<ListOfTweetsPerClasse<Tweet>>> for cross validation
	 * @param f file to load
	 * @return datas to use, nbOfBlocks<ListOfClasses<ListOfTweetsPerCLasse<Tweet>>>
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
			br.close();
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
				datas.get(tweet.getPolarit()).add(tweet);
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
		final String marque = line.substring(line.indexOf(',') + 1, middle);

		// treat tokens
		final String[] tokens = texte.split(" ");
		final Integer[] words = getNumbersFromWords(tokens);

		// create tweet
		return new Tweet(nti(polarite), nti2(marque), words);
	}


	/**
	 * Fill the dictionary <String, Integer> with words form a tweet and return the code of each word
	 * in a table
	 * 
	 * @param t
	 *            Tokenizer (the list of words)
	 * @return tab with the codes of the words in the dictionary
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


	/**
	 * Calculates the best k parameter with dichotomy
	 * @param trainFile file used to train
	 * @param min the minimum value of the interval
	 * @param max the maximum value of the interval
	 * @param limit the accuracy of the calculus
	 * @return the best k to use
	 */
	public double calculateMin(final File trainFile, final double min, final double max, final double limit) {
		final double middle = (max + min) / 2;
		if(middle - min < limit) {
			return middle;
		}
		final double littleMiddle = (middle + min) / 2;
		final double bigMiddle = (max + middle) / 2;
		final double littleMiddleValue = crossValidation(trainFile, littleMiddle, false);
		final double bigMiddleValue = crossValidation(trainFile, bigMiddle, false);

		if(littleMiddleValue < bigMiddleValue) {
			System.out.println("k="+littleMiddle+" => "+littleMiddleValue+" *");
			System.out.println("k="+bigMiddle+" => "+bigMiddleValue);
			System.out.println("-----");
			return calculateMin(trainFile, min, middle, limit);
		} else {
			System.out.println("k="+littleMiddle+" => "+littleMiddleValue);
			System.out.println("k="+bigMiddle+" => "+bigMiddleValue+" *");
			System.out.println("-----");
			return calculateMin(trainFile, middle, max, limit);
		}

	}


	/**
	 * getter for the dictionary
	 * @return the dictionary
	 */
	public HashMap<String, Integer> getDictionary() {
		return dictionary;
	}

}
