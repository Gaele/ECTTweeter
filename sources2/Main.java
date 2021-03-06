package sources2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sources2.managers.ManagerRegions2;

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
		/*System.out.println("Enter sth to start");
		try {
			System.in.read();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println("Starting...");*/

		// initialisation
		// final Long start = System.nanoTime();
		// final File f = new File(
		// "src/data/train.txt");
		if(args.length != 1) {
			System.out.println("usage: java -cp bin sources2.Main fichierTest");
			System.exit(1);
		}

		final String testFnm = args[0];
		System.out.println("testFnm: "+testFnm);
		final File learn = new File("src/dataProject/smallTrain_dev.txt");
		//		final File test2 = new File("src/dataProject/internTest.txt");
		final File test = new File(testFnm);
		final File results = new File("src/dataProject/results.txt");

		// Pipe lined test
		final AbstractManager manager = new ManagerRegions2();
		final ArrayList<ArrayList<Tweet>> datas = manager.fileToArrayList(learn);
		manager.learn(datas, 0.1, false);
		final ArrayList<Tweet> dataTest = manager.fileToSimpleArrayList(test);
		//		System.out.println(dataTest);
		final ArrayList<ArrayList<Tweet>> res = manager.work(dataTest, true);
		//		System.out.println("RES: " + res);
		// final double accuracy = manager.check(res, true);
		// System.out.println(accuracy);
		try {
			manager.writeResult(res, results);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// test simple
		// final AbstractManager simple = new SimpleManager();
		// final ArrayList<ArrayList<Tweet>> datas =
		// simple.fileToArrayList(learn);
		// simple.learn(datas, 0.1, true);
		// final ArrayList<Tweet> dataTest = simple.fileToSimpleArrayList(test);
		// final ArrayList<ArrayList<Tweet>> res = simple.work(dataTest, true);
		// simple.check(res, true);
		// simple.crossValidation(learn, 0.1, false);

		// Classifieur region
		// final AbstractManager region = new ManagerRegions2();
		// final ArrayList<ArrayList<Tweet>> datas =
		// region.fileToArrayList(learn);
		// final Classifier classifierRegions = new ClassifierRegions3Groupes();
		// classifierRegions.learn(region, datas, 0.1, false);
		// final ArrayList<Tweet> dataTest = region.fileToSimpleArrayList(test);
		// final ArrayList<ArrayList<Tweet>> res = classifierRegions.work(
		// dataTest, true);
		// classifierRegions.check(res, true);
		// classifierRegions.calculateAndDisplayConfusionMatrix(res);

		// Classifieur latin
		// final AbstractManager region = new ManagerRegions();
		// final ArrayList<ArrayList<Tweet>> datas = region.fileToArrayList(learn);
		// final Classifier classifierRegions = new ClassifierLatin();
		// classifierRegions.learn(region, datas, 0.1, false);
		// final ArrayList<Tweet> dataTest = region.fileToSimpleArrayList(test);
		// final ArrayList<ArrayList<Tweet>> res = classifierRegions.work(
		// dataTest, true);
		// classifierRegions.check(res, true);
		// classifierRegions.calculateAndDisplayConfusionMatrix(res);

		// test crossvalidation
		// final AbstractManager simple = new SimpleManager();
		// final double moyenne = simple.crossValidation(learn, 0.1, true);
		// System.out.println("precision = "+moyenne);

		// Cherche la meilleur cross validation !
		// final AbstractManager simple = new SimpleManager();
		// final double bestK = simple.calculateMinStrongly(learn, 0.01, 0.5,
		// 0.001);
		// System.out.println("bestK = " + bestK);
		// final double moyenne = simple.crossValidation(learn, bestK, false);
		// System.out.println("precision = " + moyenne);

		// System.out.println("\n\n> TOTAL TIME : " + (System.nanoTime() - start) / 1000000000 + " sec");
	}
}
