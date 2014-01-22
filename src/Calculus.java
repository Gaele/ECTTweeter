import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Calculus {

	private final double k = 0.1;// best 0.4
	int nbClasses;
	final ArrayList<ArrayList<Tweet>> learningTweets;
	HashMap<String, Integer> dictionary;

	// for the calculus
	double[] py;
	double[][] byw;
	double[] alpha;

	public Calculus(final int nbClasses,
			final ArrayList<ArrayList<Tweet>> learningTweets,
			final HashMap<String, Integer> dico, final double[] py,
			final double[][] byw, final double[] alpha) {
		this.learningTweets = learningTweets;
		this.dictionary = dico;
		this.alpha = alpha;
		this.nbClasses = learningTweets.size();
		this.py = py;// new int[this.nbClasses];
		this.byw = byw;// new int[this.nbClasses][this.dictionary.size()];
	}

	public void displayPy() {
		System.out.println("PY :");
		for (int i = 0; i < this.nbClasses; i++) {
			System.out.print(this.py[i] + " ");
		}
		System.out.println();
	}

	public void displayByw() {
		System.out.println("BYW :");
		// System.out.println("\tpos\tneg\tneu\tirr");
		for (int i = 0; i < this.dictionary.size(); i++) {
			System.out.print("\t" + i);
		}
		System.out.println();
		for (int i = 0; i < this.nbClasses; i++) {
			switch (i) {
			case 0:
				System.out.print("pos");
				break;
			case 1:
				System.out.print("neg");
				break;
			case 2:
				System.out.print("neu");
				break;
			case 3:
				System.out.print("irr");
				break;
			default:
				System.out.print("#");
				break;
			}
			for (int j = 0; j < this.dictionary.size(); j++) {
				System.out.print("\t" + this.byw[i][j]);
			}
			System.out.println();
		}
	}

	public void calculateAndDisplayConfusionMatrix(
			final ArrayList<ArrayList<Tweet>> evaluationTweets) {
		final Integer[][] confusionMatrix = new Integer[4][4];
		for (int i = 0; i <= 3; i++) {
			for (int j = 0; j <= 3; j++) {
				confusionMatrix[i][j] = 0;
			}
		}
		for (final ArrayList<Tweet> classe : evaluationTweets) {
			for (final Tweet tweet : classe) {
				final int real = tweet.getPolarite();
				final int calculated = this.calculatePxy2(tweet);
				confusionMatrix[calculated][real] += 1;
			}
		}

		System.out.println("\tpos\tneg\tneu\tirr");
		for (int i = 0; i <= 3; i++) {
			switch (i) {
			case 0:
				System.out.print("pos");
				break;
			case 1:
				System.out.print("neg");
				break;
			case 2:
				System.out.print("neu");
				break;
			case 3:
				System.out.print("irr");
				break;
			default:
				System.out.print("#");
				break;
			}
			for (int j = 0; j <= 3; j++) {
				System.out.print("\t" + confusionMatrix[i][j]);
			}
			System.out.println();
		}
	}

	public void calculatePy() {
		int total = 0;
		int cpt = 0;
		// for all class
		for (final ArrayList<Tweet> alt : this.learningTweets) {
			this.py[cpt] = alt.size();
			total += this.py[cpt];
			cpt++;
		}
		for (int i = 0; i < this.nbClasses; i++) {
			this.py[i] = Math.log(this.py[i] / total);
		}
	}

	/**
	 * calculate Beta
	 */
	public void calculateByw() {
		// nb tweets in class i
		final double[] dy = new double[this.nbClasses];
		for (int i = 0; i < dy.length; i++) {
			dy[i] = this.learningTweets.get(i).size();
		}
		int cptClass = 0;
		// for all word
		for (final int word : this.dictionary.values()) {
			// for all tweets in a class
			cptClass = 0;
			for (final ArrayList<Tweet> classe : this.learningTweets) {
				// for all tweet in a class "classe"
				int sumXw = 0;
				for (final Tweet tweet : classe) {
					// System.out.print("c: " + cptClass + ", m: "
					// + tweet.getMarque() + ", w: " + word);
					final Integer[] tweetWords = tweet.getWords();
					for (final Integer tweetWord : tweetWords) {
						if (tweetWord == word) {
							sumXw++;
							// System.out.print(" ok");
							break;
						}
					}
					// System.out.println();
				}
				// System.out.println("sum: "
				// + Math.max(this.k,
				// Math.min(sumXw, dy[cptClass] - this.k)));
				// System.out.println("Cy: " + dy[cptClass]);
				this.byw[cptClass][word] = Math.max(this.k,
						Math.min(sumXw, dy[cptClass] - this.k))
						/ dy[cptClass];
				// System.out.println(this.byw[cptClass][word]);
				cptClass++;
			}
		}
	}

	/**
	 * Calculate Alpha
	 */
	public void calculateA() {
		for (int i = 0; i < this.nbClasses; i++) {
			// for all type we calculate a different alpha
			this.alpha[i] = 0;
			for (final Integer w : this.dictionary.values()) {
				this.alpha[i] += Math.log(1 - this.byw[i][w]);
			}
		}
	}

	/**
	 * Calculated on the fly
	 * 
	 * @param t
	 * @return
	 */
	public int calculatePxy(final Tweet t) {
		final Integer[] words = t.getWords();
		final Set<Integer> s = new HashSet<Integer>();
		for (int i = 0; i < words.length; i++) {
			s.add(words[i]);
		}

		double logPXY;
		int classe = 0;
		double maxLogPXY = -Double.MAX_VALUE;
		for (int i = 0; i <= this.nbClasses - 1; i++) {
			// for all class
			logPXY = 0D;
			for (final int word : this.dictionary.values()) {
				// for all word
				if (s.contains(word)) {
					logPXY += Math.log(this.byw[i][word]);
				} else {
					logPXY += Math.log(1 - this.byw[i][word]);
				}
			}
			// System.out.println("class " + i + ": " + (logPXY + this.py[i])
			// + " || " + maxLogPXY);
			// maxLogPXY = Math.max(maxLogPXY, logPXY + this.py[i]);
			final double nouvelleValeur = logPXY + this.py[i];
			if (maxLogPXY < nouvelleValeur) {
				classe = i;
				maxLogPXY = nouvelleValeur;
			}
		}
		return classe;
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
		for (int i = 0; i < tweetWords.length; i++) {
			tweetWordsSet.add(tweetWords[i]);
		}
		double logPXY;
		int classe = -1;
		double maxLogPXY = -Double.MAX_VALUE;
		for (int i = 0; i <= this.nbClasses - 1; i++) {
			// for all class
			logPXY = this.alpha[i];
			// System.out.println("Alpha: " + this.alpha[i]);
			for (final int word : this.dictionary.values()) {
				// for all word
				boolean containWord = false;
				for (int j = 0; j < tweetWords.length; j++) {
					if (tweetWords[j] == word) {
						containWord = true;
						break;
					}
				}
				if (containWord) {
					logPXY += Math.log(this.byw[i][word])
							- Math.log(1 - this.byw[i][word]);
				}
			}
			// System.out.println("class " + i + ": " + (logPXY + this.py[i])
			// + " || " + maxLogPXY);
			final double nouvelleValeur = logPXY + this.py[i];
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
		// final Tweet t = evaluationTweets.get(0).get(0);
		double total = 0, ok = 0;
		final int okStats[] = new int[this.nbClasses];
		final int totStats[] = new int[this.nbClasses];
		for (int i = 0; i < this.nbClasses; i++) {
			okStats[i] = 0;
			totStats[i] = 0;
		}
		for (final ArrayList<Tweet> alt : evaluationTweets) {
			for (final Tweet t : alt) {
				final int classe = this.calculatePxy2(t);
				// System.out.print("(" + t.getPolarite() + "," + t.getMarque()
				// + "," + classe + ")\t");
				if (classe != t.getPolarite()) {
					// System.out.println("err: " + t.getPolarite() + " != "
					// + type);
				} else {
					okStats[t.getPolarite()]++;
					// System.out.println(t.getPolarite());
					ok++;
				}
				total++;
				totStats[t.getPolarite()]++;
			}
		}
		System.out.println("taux erreur : " + (total - ok) * 100 / total + "%");
		for (int i = 0; i < this.nbClasses; i++) {
			System.out.println("err Classe" + i + ": "
					+ (totStats[i] - okStats[i]) + "/" + totStats[i] + " => "
					+ (totStats[i] - okStats[i]) * 100 / totStats[i] + "%");

		}
	}
}
