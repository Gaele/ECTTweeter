package sources2;
import java.io.File;
import java.io.IOException;

import sources2.managers.SimpleManager;

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

		//		final AbstractManager manager = new ManagerAsieEurope();
		//		final ArrayList<ArrayList<Tweet>> datas = manager.fileToArrayList(learn);
		//		manager.learn(datas, 0.1, true);
		//		final ArrayList<Tweet> dataTest = manager.fileToSimpleArrayList(test);
		//		manager.work(dataTest, true);
		//		manager.check(true);

		// test simple
		//		final AbstractManager simple = new SimpleManager();
		//		final ArrayList<ArrayList<Tweet>> datas = simple.fileToArrayList(learn);
		//		simple.learn(datas, 0.1, true);
		//		final ArrayList<Tweet> dataTest = simple.fileToSimpleArrayList(test);
		//		final ArrayList<ArrayList<Tweet>> res = simple.work(dataTest, true);
		//		simple.check(res, true);

		// test crossvalidation
		//		final AbstractManager simple = new SimpleManager();
		//		final double moyenne = simple.crossValidation(learn, 0.1, true);
		//		System.out.println("precision = "+moyenne);

		// Cherche la meilleur cross validation !
		final AbstractManager simple = new SimpleManager();
		final double bestK = simple.calculateMin(learn, 0.01, 1, 0.01);
		System.out.println("bestK = "+bestK);
		final double moyenne = simple.crossValidation(learn, bestK, false);
		System.out.println("precision = "+moyenne);

		// other test
		//		final Classifier asianEurope = new ClassifierAsie();
		//		final double accuracy = manager.crossValidation(asianEurope, learn, 0.1, true);
		//		System.out.println("accuracy: " + accuracy);

		//		final Classifier classifier = new ClassifierProject();
		//		final double optimum = ClassifierManager.calculateMin(classifier, f, 0.01, 1.0, 0.001);
		//		System.out.println("best k = "+optimum);
		//		System.out.println("==> " + classifier.crossValidation(f, 0.46342773437499996));
		System.out.println("\n\n> TOTAL TIME : " + (System.nanoTime() - start) / 1000000000 + " sec");

	}

}
