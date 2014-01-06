import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * td1 from this page : http://perso.limsi.fr/lavergne
 * inutile
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

		final File f = new File(
				"/net/k14/u/etudiant/vvanhec/programmation/ECT/twitter/train.txt");
		final DataLoader dl = new DataLoader();
		dl.loadWordsFromFile(f);
		final ArrayList<ArrayList<Tweet>> learningPart = dl.getLearning();
		final ArrayList<ArrayList<Tweet>> evaluationPart = dl.getEvaluation();
		final HashMap<String, Integer> dictionary = dl.getDictionary();
		// System.out.println(dictionary);

		// System.out.println(evaluationPart);

		final double[] py = new double[Main.NB_CLASSES];
		final double[] alpha = new double[Main.NB_CLASSES];
		final double[][] byw = new double[Main.NB_CLASSES][dictionary.size()];

		final Calculus c = new Calculus(Main.NB_CLASSES, learningPart,
				dictionary, py, byw, alpha);
		c.calculatePy();
		c.calculateByw();
		c.calculateA();
		// c.displayPy();
		// c.displayByw();
		System.out.println("calculus done");
		c.calculateClass(evaluationPart);
		c.calculateAndDisplayConfusionMatrix(evaluationPart);

	}
}
