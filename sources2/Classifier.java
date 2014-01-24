package sources2;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The abstract classifier is responsible for naive bayesien calculations, data treatment and local performance analysis.
 * @author vincent
 *
 */
public abstract class Classifier {

	/**
	 * code final classe (the final tag) => code derivee (local tag)
	 */
	protected Integer[] toDerivatedClasses;

	/**
	 * Number of derived classes (i.e. local tags used by the classifier)
	 */
	protected int NB_CLASSES_DERIVEES = 4;

	/**
	 * Local dictionary to manage local words. This is a sub-part of the global dictionary but code needs new unique identifiers to avoid "arrayOutOfBounds" in byw.
	 */
	private final HashMap<Integer, Integer> localDictionary;

	/**
	 * Gives a unique identifier to all words of the {@link #localDictionary}
	 */
	protected Integer nextLocalId = 0;

	/**
	 * for the calculus
	 */
	double[] py;
	double[][] byw;
	double[] alpha;

	public Classifier() {
		localDictionary = new HashMap<Integer, Integer>();
	}

	/**
	 * Weather this tweet can be used to learn by this classifier. Some classifiers can learn on some special student level or tongue
	 * @param t the Tweet
	 * @return true if we can use this tweet to learn
	 */
	protected abstract boolean isUsable(Tweet t);

	/**
	 * Initialises the local dictionary and transform final/global classes into derived/local classes.
	 * @param man the manager
	 * @param datas the global datas (orded by final classes)
	 * @param localDictionary to load
	 * @return
	 */
	public abstract ArrayList<ArrayList<Tweet>> preTraitement(final AbstractManager man, final ArrayList<ArrayList<Tweet>> datas, HashMap<Integer, Integer> localDictionary);

	/**
	 * Get the used data (classe of marqueur) we analyse on tweets with this classifier.
	 * @param t The tweet
	 * @return the code of the "tag" of this tweet
	 */
	public abstract int getTag(Tweet t);

	/**
	 * Learn form the global datas
	 * @param man the manager using this classifier
	 * @param globalDatas the global datas (ordered by final classes)
	 * @param k the k parameter
	 * @param verbose prints datas if true
	 */
	public void learn(final AbstractManager man, final ArrayList<ArrayList<Tweet>> globalDatas, final double k, final boolean verbose) {
		// Initialises local dico
		final Long startCalculus = System.nanoTime();
		localDictionary.clear();
		nextLocalId = 0;
		final ArrayList<ArrayList<Tweet>> datas = preTraitement(man, globalDatas, localDictionary);
		final Long endLocalDico = System.nanoTime();
		if(verbose) {
			System.out.println(">>> Time for localDico: " + (endLocalDico - startCalculus) / 1000000000 + " sec");
		}
		// Initialises data structures for Bayesian calculations
		py = new double[NB_CLASSES_DERIVEES];
		alpha = new double[NB_CLASSES_DERIVEES];
		byw = new double[NB_CLASSES_DERIVEES][localDictionary.size()];
		// Calculates Py
		calculatePy(datas);
		final Long endPy = System.nanoTime();
		if(verbose) {
			System.out.println(">>> Time for Py: " + (endPy - endLocalDico) / 1000000000 + " sec");
		}
		// Calculates Byw
		calculateByw(man, datas, k);
		final Long endByw = System.nanoTime();
		if(verbose) {
			System.out.println(">>> Time for Byw: " + (endByw - endPy) / 1000000000 + " sec");
		}
		// Calculates A
		calculateA(man);
		if(verbose) {
			System.out.println(">>> Time for A: " + (System.nanoTime() - endByw) / 1000000000 + " sec");
		}
	}

	/**
	 * Integer to Natural, gives the string representation of a derived/local classe code.
	 * @param polarite the code of the derived/local classe
	 * @return the string description of the derived/local classe
	 */
	public abstract String itn(final Integer polarite);

	/////////////// **** CALCULUS ****

	/**
	 * Calculates Py
	 * @param datas local datas
	 */
	private void calculatePy(final ArrayList<ArrayList<Tweet>> datas) {
		int total = 0;
		int cpt = 0;
		// for all class
		for (final ArrayList<Tweet> alt : datas) {
			py[cpt] = alt.size();
			total += py[cpt];
			cpt++;
		}
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			py[i] = Math.log(py[i] / total);
		}
	}

	/**
	 * Calculates Byw
	 * @param man the manager using this classifier
	 * @param datas the local datas
	 * @param k2 the k parameter
	 */
	private void calculateByw(final AbstractManager man, final ArrayList<ArrayList<Tweet>> datas, final double k) {
		// nb tweets in class i
		final double[] dy = new double[NB_CLASSES_DERIVEES];
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			dy[i] = datas.get(i).size();
		}

		// content of hash table : (class number, (word number in dictionary, nb of occurrences of this word))
		final HashMap<Integer, HashMap<Integer, Integer>> occurrences = new HashMap<Integer, HashMap<Integer, Integer>>();
		// map initialization
		int classNb;
		for (classNb = 0; classNb < NB_CLASSES_DERIVEES; classNb++) {
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
		for (classNb = 0; classNb < NB_CLASSES_DERIVEES; classNb++) {
			for (final int wordNb : localDictionary.values()) {

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
	 * Calculated Alpha
	 * @param man the manager using this classifier
	 */
	private void calculateA(final AbstractManager man) {
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			// for all type we calculate a different alpha
			alpha[i] = 0;
			for (final Integer w : localDictionary.values()) {
				alpha[i] += Math.log(1 - byw[i][w]);
			}
		}
	}

	/**
	 * Get the class of an unknown peace of data (text, tweet...)
	 * @param t the Tweet
	 * @return classe's code
	 */
	private int calculatePxy2(final Tweet t) {
		double logPXY;
		int classe = -1;
		double maxLogPXY = Double.NEGATIVE_INFINITY;
		// for all class
		for (int i = 0; i <= NB_CLASSES_DERIVEES - 1; i++) {
			logPXY = alpha[i];
			for(final int word : t.getWords()) {
				try {
					logPXY += Math.log(byw[i][word])
							- Math.log(1 - byw[i][word]);
				} catch(final ArrayIndexOutOfBoundsException e) {
				}
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
	 * Calculates the classes of a list of datas (tweets)
	 * @param tweets the tweets
	 * @param checkUsable if true, we use only the tweets which are {@link #isUsable(Tweet)}. CheckUsable is true for local analysis, false for global analysis.
	 * @return datas with their local class : the posotion of each list of tweet is a local class' code.
	 */
	public ArrayList<ArrayList<Tweet>> work(final ArrayList<Tweet> tweets, final boolean checkUsable) {
		final ArrayList<ArrayList<Tweet>> result = new ArrayList<ArrayList<Tweet>>();
		for(int i=0; i<NB_CLASSES_DERIVEES; i++) {
			result.add(new ArrayList<Tweet>());
		}
		for (final Tweet t : tweets) {
			if(checkUsable && !isUsable(t)) {
				continue;
			}
			final int classe = calculatePxy2(t);
			result.get(classe).add(t);
		}
		return result;
	}

	/**
	 * Check the calculated data for local performance analysis
	 * @param res the calculated datas
	 * @param verbose prints infos if true
	 * @return the % of errors
	 */
	public double check(final ArrayList<ArrayList<Tweet>> res, final boolean verbose) {
		// initialisation
		int nbClasse = 0;
		double total = 0, ok = 0;
		final int okStats[] = new int[NB_CLASSES_DERIVEES];
		final int totStats[] = new int[NB_CLASSES_DERIVEES];
		final int nbInClasse[] = new int[NB_CLASSES_DERIVEES];
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			okStats[i] = 0;
			totStats[i] = 0;
			nbInClasse[i] = 0;
		}
		// calculates
		for(final ArrayList<Tweet> classe : res) {
			for(final Tweet t : classe) {
				if (nbClasse == toDerivatedClasses[t.getTag(this)]) {
					okStats[nbClasse]++;
					ok++;
				}
				total++;
				totStats[toDerivatedClasses[t.getTag(this)]]++;
				nbInClasse[nbClasse]++;
			}
			nbClasse++;
		}
		if(verbose) {
			System.out.println("taux erreur : " + (total - ok) * 100 / total + "%");
			for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
				if(totStats[i] == 0) {
					totStats[i] = 1;
				}
				System.out.println("err Classe " + itn(i) + " (" + nbInClasse[i] + "): "
						+ (totStats[i] - okStats[i]) + "/" + totStats[i] + " => "
						+ (totStats[i] - okStats[i]) * 100 / totStats[i] + "%");
			}
			// words known and unknown. can't be used for pipe-lined work since the final checking classifier don't have any dictionary.
			//			final int nbWordsKnown = byw[0].length;
			//			final int totNbWords = localDictionary.size();
			//			System.out.println("nb words: " + totNbWords);
			//			System.out.println("unknown words: " + ((double)totNbWords - nbWordsKnown) / totNbWords * 100);
		}

		return (total - ok) * 100 / total;
	}

	////////////// *** DISPLAY ***
	/**
	 * Display Py to debug
	 */
	public void displayPy() {
		System.out.println("PY :");
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			System.out.print(py[i] + " ");
		}
		System.out.println();
	}

	/**
	 * Display the Beta to debug
	 */
	public void displayByw() {
		System.out.println("BYW :");
		for (int i = 0; i < localDictionary.size(); i++) {
			System.out.print("\t" + i);
		}
		System.out.println();
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			// display class
			System.out.println(itn(i));
			for (int j = 0; j < localDictionary.size(); j++) {
				System.out.print("\t" + byw[i][j]);
			}
			System.out.println();
		}
	}

	/**
	 * Display the confusion matrix
	 * @param evaluationTweets the tweets to evaluate, sorted by derived/local class
	 */
	public void calculateAndDisplayConfusionMatrix(
			final ArrayList<ArrayList<Tweet>> evaluationTweets) {
		final Integer[][] confusionMatrix = new Integer[NB_CLASSES_DERIVEES][NB_CLASSES_DERIVEES];
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			for (int j = 0; j < NB_CLASSES_DERIVEES; j++) {
				confusionMatrix[i][j] = 0;
			}
		}
		int nbClasse=0;
		for (final ArrayList<Tweet> classe : evaluationTweets) {
			for (final Tweet tweet : classe) {
				final int real = toDerivatedClasses[tweet.getTag(this)];
				final int calculated = nbClasse;
				confusionMatrix[calculated][real] += 1;
			}
			nbClasse++;
		}
		for(int i=0; i < NB_CLASSES_DERIVEES; i++) {
			System.out.print("\t"+itn(i));
		}
		System.out.println();
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			System.out.print(itn(i));
			for (int j = 0; j < NB_CLASSES_DERIVEES; j++) {
				System.out.print("\t" + confusionMatrix[i][j]);
			}
			System.out.println();
		}
	}

	/**
	 * get the number of derived/local classes
	 * @return
	 */
	public int getNB_CLASSES_DERIVEES() {
		return NB_CLASSES_DERIVEES;
	}

}
