import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
		final File f = new File(
				"src/data/train.txt");
		final Classifier classifier = new Classifier01();
		final ArrayList<ArrayList<Tweet>> datas = classifier.fileToArrayList(f);
		final Long endLoad = System.nanoTime();
		System.out.println("> Load done in : " + (endLoad - start) / 1000000000 + " sec");

		// calculus
		classifier.calculate(datas);
		final Long endCalculus = System.nanoTime();
		System.out.println("> Calculus done in : " + (endCalculus - endLoad) / 1000000000 + " sec");

		// printing results
		classifier.printResults(classifier.fileToArrayList(new File("src/data/dev.txt")));

		System.out.println("> Stats done in : " + (System.nanoTime() - endCalculus) / 1000000000 + " sec");
		System.out.println("\n\n> TOTAL TIME : " + (System.nanoTime() - start) / 1000000000 + " sec");
	}
}
