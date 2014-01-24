package sources2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractManager {

	protected int NB_CLASSES = 4;
	/**
	 * ??? contains number of word in the corpus.
	 */
	private final HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
	/**
	 * To give a unique identifier to all words
	 */
	private Integer nextId = 0;

	public abstract void learn(ArrayList<ArrayList<Tweet>> datas, final double k, final boolean verbose);

	public abstract void work(final ArrayList<Tweet> dataTest, final boolean verbose);

	public abstract double check(boolean verbose);

	public abstract String filter(String text);

	public abstract Integer nti(final String polarite);

	public abstract String itn(final Integer polarite);

	public double crossValidation(final Classifier c, final File f, final double k, final boolean verbose) {
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
					// test.addAll(datas.get(j));
					for(int classe=0; classe<NB_CLASSES; classe++) {
						test.addAll(datas.get(j).get(classe));
					}
				} else {
					// learning.addAll(datas.get(j));
					for(int classe=0; classe<NB_CLASSES; classe++) {
						learning.get(classe).addAll(datas.get(j).get(classe));
					}
				}
			}
			// make calculus
			c.learn(this, learning, k, verbose);
			//			results[i] = c.calculateClass(test, verbose);
			final ArrayList<ArrayList<Tweet>> res = c.work(test);
			results[i] = c.check(res, verbose);
			if(verbose) {
				c.calculateAndDisplayConfusionMatrix(res);
			}
		}
		// print results
		int sum = 0;
		for(int i=0; i<size; i++) {
			sum += results[i];
		}
		//		System.out.println("Moyenne = " + (double)sum / size);
		return (double)sum / size;
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
	 * Load the datas
	 * @param f file do load
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

	/**
	 * Calculates the best k parameter with dichotomy
	 * @param classifier the Classifier
	 * @param trainFile file used to train
	 * @param min the minimum value of the interval
	 * @param max the maximum value of the interval
	 * @param limit the accuracy of the calculus
	 * @return
	 */
	public double calculateMin(final Classifier classifier, final File trainFile, final double min, final double max, final double limit) {
		final double middle = (max + min) / 2;
		if(middle - min < limit) {
			return middle;
		}
		final double littleMiddle = (middle + min) / 2;
		final double bigMiddle = (max + middle) / 2;
		final double littleMiddleValue = crossValidation(classifier, trainFile, littleMiddle, false);
		final double bigMiddleValue = crossValidation(classifier, trainFile, bigMiddle, false);

		if(littleMiddleValue < bigMiddleValue) {
			System.out.println("k="+littleMiddle+" => "+littleMiddleValue+" *");
			System.out.println("k="+bigMiddle+" => "+bigMiddleValue);
			System.out.println("-----");
			return calculateMin(classifier, trainFile, min, middle, limit);
		} else {
			System.out.println("k="+littleMiddle+" => "+littleMiddleValue);
			System.out.println("k="+bigMiddle+" => "+bigMiddleValue+" *");
			System.out.println("-----");
			return calculateMin(classifier, trainFile, middle, max, limit);
		}

	}

	public HashMap<String, Integer> getDictionary() {
		return dictionary;
	}

}
