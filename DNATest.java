import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author Richie Ramrati
 * 
 * The following is a list of sequences that shared relative low edit distances (< 100). The first value is the sequence number. 
 * 
 * 1 - 3, 6, 12, 14, 19 
 * 2 - 3, 9, 10, 11, 13, 15, 17, 19
 * 3 - 1, 2, 9, 10, 11, 13, 15, 16, 17, 19 
 * 4 - 5, 7, 8, 18, 20 
 * 5 - 4, 7, 8, 16, 18, 20 
 * 6 - 1, 12, 14 
 * 7 - 4, 5, 8, 16, 18, 20 
 * 8 - 4, 5, 7, 16, 18, 20 
 * 9 - 2, 3, 10, 11, 13, 15, 16, 17, 19 
 * 10 - 2, 3, 9, 11, 13, 15 , 17, 19 
 * 11 - 2, 3, 9, 10, 13, 15, 17 19 
 * 12 - 1, 6, 14
 * 13 - 2, 3, 9, 10, 11, 15, 17, 19 
 * 14 - 1, 6, 12
 * 15 - 2, 3, 9, 10, 11, 13, 16, 17, 19 
 * 16 - 3, 4, 5, 7, 8, 9, 10, 15, 18, 19, 20 ** 
 * 17 - 2, 3, 9, 10, 11, 13, 15,19
 * 18 - 4, 5, 7, 8, 16, 20 
 * 19 - 1, 2, 3, 9, 10, 11, 13, 15, 16, 17
 * 20 - 4, 5, 7, 8, 16, 18 
 * 
 * Many groups demonstrate a good degree of overlap, suggesting that they can be clustered together into three main groups.
 * For example 
 * GROUP 1 - 6, 12, 14
 * GROUP 2 - 1, 2, 3, 9, 10, 11, 13, 15, 17, 
 * GROUP 3 - 4, 5, 7, 8, 18, 20 
 * 
 * 16** could fit in both 2 and 3
 *
 */
public class DNATest {
	private static ArrayList<char[]> DNA = new ArrayList<char[]>();

	public static void main(String[] args) {
		readFile(args[0]);
		if (!DNA.isEmpty()) {
			ArrayList<SimpleEntry<char[], char[]>> seqPairs = possiblePairs(DNA);
			int count = 0;
			for (SimpleEntry<char[], char[]> pair : seqPairs) {
				System.out.print(editDistance(pair) + "\t");
				count++;
				if (count == DNA.size()) {
					count = 0;
					System.out.println();
				}
			}
		}
	}

	/**
	 * 
	 * @param pair
	 *            of character arrays
	 * @return cost of translating string to another string given insertion,
	 *         deletion, transposition, matching and replacement operations.
	 */
	private static int editDistance(SimpleEntry<char[], char[]> pair) {
		int n = pair.getKey().length;
		int m = pair.getValue().length;
		int[] opCost = new int[4];
		int[][] dynArr = new int[n + 1][m + 1];
		for (int r = 0; r <= n; r++) {
			for (int c = 0; c <= m; c++) {
				if (r == 0) {
					dynArr[r][c] = c;
				} else if (c == 0) {
					dynArr[r][c] = r;
				} else if (pair.getKey()[r - 1] == pair.getValue()[c - 1]) {
					dynArr[r][c] = dynArr[r - 1][c - 1];
				} else if ((r > 1) && (c > 1) && ((pair.getKey()[r - 1] == pair.getValue()[c - 2])
						&& (pair.getKey()[r - 2] == pair.getValue()[c - 1]))) {
					opCost[0] = dynArr[r][c - 1];
					opCost[1] = dynArr[r - 1][c];
					opCost[2] = dynArr[r - 1][c - 1];
					opCost[3] = dynArr[r - 2][c - 2] + 1;
					dynArr[r][c] = min(opCost) + 1;
				} else {
					opCost[0] = dynArr[r][c - 1];
					opCost[1] = dynArr[r - 1][c];
					opCost[2] = dynArr[r - 1][c - 1];
					opCost[3] = Integer.MAX_VALUE; 
					dynArr[r][c] = min(opCost) + 1;
				}
			}
		}
		return dynArr[n][m];
	}

	/**
	 * @param array
	 * @return smallest value in array
	 */
	private static int min(int[] arr) {
		int min = arr[0];
		for (int num : arr) {
			if (num < min) {
				min = num;
			}
		}
		return min;
	}

	/**
	 * @param DNA
	 *            list of character arrays
	 * @return all possible pair of character arrays
	 */
	private static ArrayList<SimpleEntry<char[], char[]>> possiblePairs(ArrayList<char[]> DNA) {
		ArrayList<SimpleEntry<char[], char[]>> pairs = new ArrayList<SimpleEntry<char[], char[]>>();
		for (int i = 0; i < DNA.size(); i++) {
			for (int j = 0; j < DNA.size(); j++) {
				// if (i != j) {
				pairs.add(new SimpleEntry<char[], char[]>(DNA.get(i), DNA.get(j)));

				// }
			}
		}
		return pairs;
	}

	/**
	 * Reads each line from a file and convert each line into an array of characters
	 * 
	 * @param filePath
	 */
	private static void readFile(String filePath) {
		try {
			Scanner fileScanner = new Scanner(new File(filePath));
			while (fileScanner.hasNext()) {
				DNA.add(fileScanner.nextLine().toCharArray());
			}
		} catch (Exception e) {
			System.out.println("Error: Incorrect File Path");
			return;
		}
	}

}
