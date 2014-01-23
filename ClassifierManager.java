import java.io.File;


public class ClassifierManager {

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
