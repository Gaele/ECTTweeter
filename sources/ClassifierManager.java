package sources;
import java.io.File;
import java.util.ArrayList;

import sources.classifiers.ClassifierSimple;


public class ClassifierManager {

	public static void simpleClassifier() {
		//		best k = 0.46342773437499996
		//		==> 30.77777777777778
		final Long start = System.nanoTime();
		final File f = new File(
				"src/dataProject/smallTrain.txt");
		final Classifier classifier = new ClassifierSimple();
		final ArrayList<ArrayList<Tweet>> datas = classifier.fileToArrayList(f);
		final Long endLoad = System.nanoTime();
		System.out.println("> Load done in : " + (endLoad - start) / 1000000000 + " sec");

		// calculus
		classifier.calculate(datas, 0.1, false);
		final Long endCalculus = System.nanoTime();
		System.out.println("> Calculus done in : " + (endCalculus - endLoad) / 1000000000 + " sec");

		// printing results
		classifier.printResults(classifier.fileToArrayList(new File("src/dataProject/dev.txt")));

		//		System.out.println("> Stats done in : " + (System.nanoTime() - endCalculus) / 1000000000 + " sec");
		System.out.println("\n\n> TOTAL TIME CLASSIFIER : " + (System.nanoTime() - start) / 1000000000 + " sec");

	}

	public static void RegionClassifier() {
		// Will contain classifier for region of language


		final Long start = System.nanoTime();
		final File f = new File(
				"src/dataProject/smallTrain.txt");
		final Classifier classifier = new ClassifierSimple();
		final ArrayList<ArrayList<Tweet>> datas = classifier.fileToArrayList(f);
		final Long endLoad = System.nanoTime();
		System.out.println("> Load done in : " + (endLoad - start) / 1000000000 + " sec");

		// calculus
		classifier.calculate(datas, 0.1, false);
		final Long endCalculus = System.nanoTime();
		System.out.println("> Calculus done in : " + (endCalculus - endLoad) / 1000000000 + " sec");

		// printing results
		classifier.printResults(classifier.fileToArrayList(new File("src/dataProject/dev.txt")));

		//		System.out.println("> Stats done in : " + (System.nanoTime() - endCalculus) / 1000000000 + " sec");
		System.out.println("\n\n> TOTAL TIME CLASSIFIER : " + (System.nanoTime() - start) / 1000000000 + " sec");

	}

	public static double calculateMin(final Classifier classifier, final File trainFile, final double min, final double max, final double limit) {
		final double middle = (max + min) / 2;
		if(middle - min < limit) {
			return middle;
		}
		final double littleMiddle = (middle + min) / 2;
		final double bigMiddle = (max + middle) / 2;
		final double littleMiddleValue = classifier.crossValidation(trainFile, littleMiddle);
		final double bigMiddleValue = classifier.crossValidation(trainFile, bigMiddle);

		if(littleMiddleValue < bigMiddleValue) {
			System.out.println("k="+littleMiddle+" => "+littleMiddleValue+" *");
			System.out.println("k="+bigMiddle+" => "+bigMiddleValue);
			System.out.println("-----");
			return calculateMin(classifier, trainFile, min, middle, limit);
		} else {
			System.out.println("k="+littleMiddle+" => "+littleMiddleValue);
			System.out.println("k="+bigMiddle+" => "+bigMiddleValue+" *");
			System.out.println("-----");
			return calculateMin(classifier, trainFile, middle, max, limit);
		}

	}

}
