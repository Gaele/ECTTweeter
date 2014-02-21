package sources2;

import java.util.Comparator;

public class MonCouple<X> {
	public final X x;
	public final Tweet y;

	public MonCouple(final X x1, final Tweet y1) {
		this.x = x1;
		this.y = y1;
	}

	public X getX() {
		return x;
	}

	public Tweet getY() {
		return y;
	}

	// used to retrieve tweets by their appearance order in the corpus
	public static class MyComp2 implements Comparator<MonCouple<Integer>> {

		@Override
		public int compare(final MonCouple<Integer> tweet1, final MonCouple<Integer> tweet2) {
			if (tweet1.getY().getId() < tweet2.getY().getId()) {
				return -1;
			} else if (tweet1.getY().getId() == tweet2.getY().getId()) {
				return 0;
			} else {
				return 1;
			}
		}
	}
}
