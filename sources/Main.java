package sources;
import java.io.File;
import java.io.IOException;

/**
 * from this page : http://perso.limsi.fr/lavergne
 * 
 * @author vvanhec
 * 
 */
public class Main {

	public final static String TRAIN = "train";

	public static void main(final String[] args) {
		// wait key pressed to prepare monitoring...
		System.out.println("Enter sth to start");
		try {
			System.in.read();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println("Starting...");

		// initialisation
		final Long start = System.nanoTime();
		//		final File f = new File(
		//				"src/data/train.txt");
		final File f = new File(
				"src/dataProject/train.txt");
		ClassifierManager.simpleClassifier();

		//		final Classifier classifier = new ClassifierProject();
		//		final double optimum = ClassifierManager.calculateMin(classifier, f, 0.01, 1.0, 0.001);
		//		System.out.println("best k = "+optimum);
		//		System.out.println("==> " + classifier.crossValidation(f, 0.46342773437499996));

		System.out.println("\n\n> TOTAL TIME : " + (System.nanoTime() - start) / 1000000000 + " sec");
	}

}
