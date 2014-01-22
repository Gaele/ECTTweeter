import java.io.File;
import java.io.IOException;

/**
 * td1 from this page : http://perso.limsi.fr/lavergne
 * 
 * @author vvanhec
 * 
 */
public class Main {

	public final static int NB_CLASSES = 4;

	public static void main(final String[] args) {
		// chargement des donnees
		// calcul des parametres du modele
		// evaluation du modele sur des donnees de test
		System.out.println("Enter sth to start");
		try {
			System.in.read();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println("Starting...");

		final Long start = System.nanoTime();
		final File f = new File(
				"/home/vincent/Programmation/ECT/twitter/train.txt");
		final Classifier classifier = new Classifier01();
		classifier.load(f);
		final Long endLoad = System.nanoTime();
		System.out.println("> Load done in : " + (endLoad - start) / 1000000000 + " sec");
		classifier.calculate();
		//		classifier.displayByw();
		//		System.out.println(classifier.getDictionary());

		//		final DataLoader dl = new DataLoader();
		//		dl.loadWordsFromFile(f);
		//		final ArrayList<ArrayList<Tweet>> learningPart = dl.getLearning();
		//		final ArrayList<ArrayList<Tweet>> evaluationPart = dl.getEvaluation();
		//		final HashMap<String, Integer> dictionary = dl.getDictionary();
		// System.out.println(dictionary);

		// System.out.println(evaluationPart);

		//		final double[] py = new double[Main.NB_CLASSES];
		//		final double[] alpha = new double[Main.NB_CLASSES];
		//		final double[][] byw = new double[Main.NB_CLASSES][dictionary.size()];

		//		final Calculus c = new Calculus(Main.NB_CLASSES, learningPart,
		//				dictionary, py, byw, alpha);
		//		c.calculatePy();
		//		c.calculateByw();
		//		c.calculateA();
		// c.displayPy();
		// c.displayByw();

		final Long endCalculus = System.nanoTime();
		System.out.println("> Calculus done in : " + (endCalculus - endLoad) / 1000000000 + " sec");
		classifier.calculateClass(classifier.getEvaluation());
		classifier.calculateAndDisplayConfusionMatrix(classifier.getEvaluation());
		//		c.calculateClass(evaluationPart);
		//		c.calculateAndDisplayConfusionMatrix(evaluationPart);
		System.out.println("> Stats done in : " + (System.nanoTime() - endCalculus) / 1000000000 + " sec");
		System.out.println("\n\n> TOTAL TIME : " + (System.nanoTime() - start) / 1000000000 + " sec");
	}
}
