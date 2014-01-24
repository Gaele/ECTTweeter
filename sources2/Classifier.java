package sources2;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Classifier {

	/**
	 * code classe => code derivee
	 */
	protected Integer[] toDerivatedClasses;

	/**
	 * Number of classes to consider
	 */
	protected int NB_CLASSES_DERIVEES = 4;

	/**
	 * code derivee => code classe
	 */
	//	protected int[] toClasses;

	//	private final HashSet<Integer> localDictionary;
	private final HashMap<Integer, Integer> localDictionary;
	protected Integer nextLocalId = 0;

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
		//		localDictionary = new HashSet<Integer>();
		localDictionary = new HashMap<Integer, Integer>();
	}

	public abstract boolean isUsable(Tweet t);

	public abstract ArrayList<ArrayList<Tweet>> preTraitement(final AbstractManager man, final ArrayList<ArrayList<Tweet>> datas, HashMap<Integer, Integer> localDictionary);

	public void learn(final AbstractManager man, final ArrayList<ArrayList<Tweet>> globalDatas, final double k, final boolean verbose) {
		// initialises local dico
		final Long startCalculus = System.nanoTime();
		localDictionary.clear();
		nextLocalId = 0;
		final ArrayList<ArrayList<Tweet>> datas = preTraitement(man, globalDatas, localDictionary);
		final Long endLocalDico = System.nanoTime();
		if(verbose) {
			System.out.println(">>> Time for localDico: " + (endLocalDico - startCalculus) / 1000000000 + " sec");
		}

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
	 * Print stats about the performance (performance per class + confusion matrix)
	 */
	public void printResults(final ArrayList<ArrayList<Tweet>> evaluation) {
		calculateClass(evaluation, true);
		calculateAndDisplayConfusionMatrix(evaluation);
	}
	public abstract Integer nti(final String polarite);

	public abstract String itn(final Integer polarite);

	//	public abstract Integer ntiDerive(final String polarite);

	//	public abstract String itnDerive(final Tweet polarite);

	public void print(final ArrayList<Tweet> tweets) {
		for (final Tweet t : tweets) {
			System.out.println(t);
		}
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
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			py[i] = Math.log(py[i] / total);
		}
	}

	/**
	 * calculate Beta
	 */
	private void calculateByw(final AbstractManager man, final ArrayList<ArrayList<Tweet>> datas, final double k2) {
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
	 * Pre-calculated result
	 * 
	 * @param t
	 * @return
	 */
	private int calculatePxy2(final Tweet t) {
		double logPXY;
		int classe = -1;
		double maxLogPXY = Double.NEGATIVE_INFINITY;
		// for all class
		for (int i = 0; i <= NB_CLASSES_DERIVEES - 1; i++) {
			logPXY = alpha[i];
			//			System.out.println("alpha="+alpha[i]);
			for(final int word : t.getWords()) {
				try {
					logPXY += Math.log(byw[i][word])
							- Math.log(1 - byw[i][word]);
					//					System.out.println("actual logPXY: "+logPXY);
				} catch(final ArrayIndexOutOfBoundsException e) {
					//					e.printStackTrace();
				}
			}
			final double nouvelleValeur = logPXY + py[i];
			//			System.out.println("nouvelleValeur: " + nouvelleValeur);
			//			System.out.println("logPXY: " + logPXY);
			//			System.out.println("py: " + py[i]);
			if (maxLogPXY < nouvelleValeur) {
				classe = i;
				maxLogPXY = nouvelleValeur;
			}
		}
		//		System.out.println("Unknown words: " + nbUnknownWords);
		//		System.out.println(classe);
		return classe;
	}

	public ArrayList<ArrayList<Tweet>> work(final ArrayList<Tweet> tweets) {
		final ArrayList<ArrayList<Tweet>> result = new ArrayList<ArrayList<Tweet>>();
		for(int i=0; i<NB_CLASSES_DERIVEES; i++) {
			result.add(new ArrayList<Tweet>());
		}
		for (final Tweet t : tweets) {
			if(!isUsable(t)) {
				continue;
			}
			final int classe = calculatePxy2(t);
			result.get(classe).add(t);
		}
		return result;
	}

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
				if (nbClasse == toDerivatedClasses[t.getPolarite()]) {
					okStats[nbClasse]++;
					ok++;
				}
				total++;
				totStats[toDerivatedClasses[t.getPolarite()]]++;
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
			// words known and unknown
			final int nbWordsKnown = byw[0].length;
			final int totNbWords = localDictionary.size();
			System.out.println("nb words: " + totNbWords);
			System.out.println("unknown words: " + ((double)totNbWords - nbWordsKnown) / totNbWords * 100);
		}

		return (total - ok) * 100 / total;
	}


	/**
	 * @param evaluationTweets
	 */
	public double calculateClass(
			final ArrayList<ArrayList<Tweet>> evaluationTweets, final boolean verbose) {
		double total = 0, ok = 0;
		final int okStats[] = new int[NB_CLASSES_DERIVEES];
		final int totStats[] = new int[NB_CLASSES_DERIVEES];
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			okStats[i] = 0;
			totStats[i] = 0;
		}
		for (final ArrayList<Tweet> alt : evaluationTweets) {
			for (final Tweet t : alt) {
				final int classe = calculatePxy2(t);
				if (classe == t.getPolarite()) {
					okStats[t.getPolarite()]++;
					ok++;
				}
				total++;
				totStats[t.getPolarite()]++;
			}
		}
		if(verbose) {
			System.out.println("taux erreur : " + (total - ok) * 100 / total + "%");
			for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
				System.out.println("err Classe " + itn(i) + ": "
						+ (totStats[i] - okStats[i]) + "/" + totStats[i] + " => "
						+ (totStats[i] - okStats[i]) * 100 / totStats[i] + "%");
			}
			// words known and unknown
			final int nbWordsKnown = byw[0].length;
			final int totNbWords = localDictionary.size();
			System.out.println("nb words: " + totNbWords);
			System.out.println("unknown words: " + ((double)totNbWords - nbWordsKnown) / totNbWords * 100);
		}

		return (total - ok) * 100 / total;
	}

	////////////// *** DISPLAY ***
	public void displayPy() {
		System.out.println("PY :");
		for (int i = 0; i < NB_CLASSES_DERIVEES; i++) {
			System.out.print(py[i] + " ");
		}
		System.out.println();
	}

	/**
	 * Display the Beta
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
	 * @param evaluationTweets the tweets to evaluate
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
				final int real = toDerivatedClasses[tweet.getPolarite()];
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

	public int getNB_CLASSES_DERIVEES() {
		return NB_CLASSES_DERIVEES;
	}

}
