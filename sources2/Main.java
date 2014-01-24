package sources2;
import java.io.File;
import java.io.IOException;

import sources2.classifiers.ClassifierAsie;

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
		final File learn = new File(
				"src/dataProject/smallTrain.txt");
		final File test = new File(
				"src/dataProject/dev.txt");
		final AbstractManager manager = new ClassifierManager();
		//		final ArrayList<ArrayList<Tweet>> datas = manager.fileToArrayList(learn);
		//		manager.learn(datas, 0.1, true);
		//		final ArrayList<Tweet> dataTest = manager.fileToSimpleArrayList(test);
		//		manager.work(dataTest, true);
		//		manager.check(true);

		// other test
		final Classifier asianEurope = new ClassifierAsie();
		final double accuracy = manager.crossValidation(asianEurope, learn, 0.1, true);
		System.out.println("accuracy: " + accuracy);

		//		final Classifier classifier = new ClassifierProject();
		//		final double optimum = ClassifierManager.calculateMin(classifier, f, 0.01, 1.0, 0.001);
		//		System.out.println("best k = "+optimum);
		//		System.out.println("==> " + classifier.crossValidation(f, 0.46342773437499996));
		System.out.println("\n\n> TOTAL TIME : " + (System.nanoTime() - start) / 1000000000 + " sec");

	}

}
