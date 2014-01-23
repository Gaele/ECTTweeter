package sources2;
import java.io.File;
import java.util.ArrayList;

import sources2.classifiers.ClassifierRegion;
import sources2.classifiers.ClassifierSimple;

public class ClassifierManager {

	/**
	 * Train on trainFile and test on testFile
	 */
	private static void  trainDevTest(final Classifier classifier, final double k, final File trainFile, final File testFile, final boolean verbose) {
		final Long start = System.nanoTime();
		final ArrayList<ArrayList<Tweet>> datas = classifier.fileToArrayList(trainFile);
		final Long endLoad = System.nanoTime();
		if(verbose) {
			System.out.println("> Load done in : " + (endLoad - start) / 1000000 + " ms");
		}
		// calculus
		classifier.calculate(datas, k, false);
		final Long endCalculus = System.nanoTime();
		if(verbose) {
			System.out.println("> Calculus done in : " + (endCalculus - endLoad) / 1000000000 + " sec");
		}
		// printing results
		classifier.printResults(classifier.fileToArrayList(testFile));
		if(verbose) {
			System.out.println("> Stats done in : " + (System.nanoTime() - endCalculus) / 1000000000 + " sec");
		}
		System.out.println("\n\n> TOTAL TIME CLASSIFIER : " + (System.nanoTime() - start) / 1000000000 + " sec");
	}

	private static void  crossValidation(final Classifier classifier, final File f, final double k, final boolean verbose) {
		final Long start = System.nanoTime();
		final double errors = classifier.crossValidation(f, k, verbose);
		System.out.println("Average errors %: "+errors);
		System.out.println("\n\n> TOTAL TIME CLASSIFIER : " + (System.nanoTime() - start) / 1000000000 + " sec");
	}

	public static void simpleClassifier() {
		//		best k = 0.46342773437499996
		//		==> 30.77777777777778
		final Classifier classifier = new ClassifierSimple();
		final File trainFile = new File(
				"src/dataProject/smallTrain.txt");
		final File testFile = new File("src/dataProject/dev.txt");
		trainDevTest(classifier, 0.1, trainFile, testFile, false);
	}

	public static void regionClassifier() {
		// Will contain classifier for region of language
		final Classifier classifier = new ClassifierRegion();
		final File trainFile = new File(
				"src/dataProject/smallTrain.txt");
		final File testFile = new File("src/dataProject/dev.txt");
		//		trainDevTest(classifier, 0.1, trainFile, testFile, false);
		crossValidation(classifier, trainFile, 0.1, false);
	}

	public static double calculateMin(final Classifier classifier, final File trainFile, final double min, final double max, final double limit) {
		final double middle = (max + min) / 2;
		if(middle - min < limit) {
			return middle;
		}
		final double littleMiddle = (middle + min) / 2;
		final double bigMiddle = (max + middle) / 2;
		final double littleMiddleValue = classifier.crossValidation(trainFile, littleMiddle, false);
		final double bigMiddleValue = classifier.crossValidation(trainFile, bigMiddle, false);

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
