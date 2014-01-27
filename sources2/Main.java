package sources2;

import java.io.File;
import java.io.IOException;

import sources2.managers.LevelManager;

/**
 * This project is a student project. Teacher's page here :
 * http://perso.limsi.fr/lavergne Main class is responsible for the
 * instanciation of different managers and the management of user's interaction.
 * 
 * @author vvanhec
 * 
 */
public class Main {

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
		// final File f = new File(
		// "src/data/train.txt");
		final File learn = new File("src/dataProject/smallTrain.txt");
		final File test = new File("src/dataProject/dev.txt");

		// Pipe lined test
		// final AbstractManager manager = new ManagerAsieEurope();
		// final ArrayList<ArrayList<Tweet>> datas =
		// manager.fileToArrayList(learn);
		// manager.learn(datas, 0.1, true);
		// final ArrayList<Tweet> dataTest =
		// manager.fileToSimpleArrayList(test);
		// manager.work(dataTest, true);
		// manager.check(true);

		// test simple
		// final AbstractManager simple = new SimpleManager();
		// final ArrayList<ArrayList<Tweet>> datas =
		// simple.fileToArrayList(learn);
		// simple.learn(datas, 0.1, true);
		// final ArrayList<Tweet> dataTest = simple.fileToSimpleArrayList(test);
		// final ArrayList<ArrayList<Tweet>> res = simple.work(dataTest, true);
		// simple.check(res, true);

		// test crossvalidation
		// final AbstractManager simple = new SimpleManager();
		// final double moyenne = simple.crossValidation(learn, 0.1, true);
		// System.out.println("precision = "+moyenne);

		// Cherche la meilleur cross validation !
		// final AbstractManager simple = new SimpleManager();
		// final double bestK = simple.calculateMin(learn, 0.01, 1, 0.01);
		// System.out.println("bestK = "+bestK);
		// final double moyenne = simple.crossValidation(learn, bestK, false);
		// System.out.println("precision = "+moyenne);

		final AbstractManager level = new LevelManager();
		// final double bestK = level.calculateMin(learn, 0.01, 1, 0.01);
		// System.out.println("bestK = " + bestK);
		final double moyenne = level.crossValidation(learn, 0.729296875, true);
		System.out.println("precision = " + moyenne);

		System.out.println("\n\n> TOTAL TIME : " + (System.nanoTime() - start)
				/ 1000000000 + " sec");

	}

}
