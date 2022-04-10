import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShortestPath {

	public static final int NUMBER_OF_STOPS = 12749;
	public static final int TRIP_ID = 0;
	public static final int STOP_ID = 3;
	public static final int FROM = 0;
	public static final int TO = 1;
	public static final int TRANSFER_TYPE = 2;
	public static final int MINIMUM_TRANSFER_TIME = 3;

	private double adjMatrix[][] = new double[NUMBER_OF_STOPS][NUMBER_OF_STOPS];

	ShortestPath(String stopTimesFileString, String transfersFileString) {
		try {
			createMatrix(stopTimesFileString, transfersFileString);
		} catch (Exception e) {
			System.out.println("Initialisation failed - ShortestPath");
		}
	}

	private void createMatrix(String stopTimesFileString, String transfersFileString) throws FileNotFoundException {
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				if (i != j) {
					adjMatrix[i][j] = Double.NaN;
				} else {
					adjMatrix[i][j] = 0;
				}
			}
		}

		File stopTimesFile = new File(stopTimesFileString);
		Scanner scanner = new Scanner(stopTimesFile);
		scanner.nextLine();

		int from = 0, to = 0;
		int prevTripId = 0, tripId = 0;
		String currentLine;

		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			String tokens[] = currentLine.split(",");
			prevTripId = tripId;
			tripId = Integer.parseInt(tokens[TRIP_ID]);
			from = to;
			to = Integer.parseInt(tokens[STOP_ID]);
			if (prevTripId == tripId) {
				adjMatrix[from][to] = 1;
			}
		}
		scanner.close();

		int transferType;
		double minimumTransferTime;
		File transfersFile = new File(transfersFileString);

		scanner = new Scanner(transfersFile);
		scanner.nextLine();

		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			String tokens[] = currentLine.split(",");

			from = Integer.parseInt(tokens[FROM]);
			to = Integer.parseInt(tokens[TO]);
			transferType = Integer.parseInt(tokens[TRANSFER_TYPE]);

			if (transferType == 0) {
				adjMatrix[from][to] = 2;
			} else if (transferType == 2) {
				minimumTransferTime = Double.parseDouble(tokens[MINIMUM_TRANSFER_TIME]);
				adjMatrix[from][to] = minimumTransferTime / 100;
			}
		}
		scanner.close();
	}

	public String findShortestPath(int from, int to) {

		if (from == to) {
			return "weight: 0, path: " + from + "->" + to;
		}

		int visited[] = new int[NUMBER_OF_STOPS];
		double dst[] = new double[NUMBER_OF_STOPS];
		int edgeTo[] = new int[NUMBER_OF_STOPS];

		for (int i = 0; i < dst.length; i++) {
			if (i != from) {
				dst[i] = Double.POSITIVE_INFINITY;
			}
		}
		visited[from] = 1;
		dst[from] = 0;
		int current = from;
		int processed = 0;

		while (processed < NUMBER_OF_STOPS) {
			for (int i = 0; i < adjMatrix[current].length; i++) {
				if (!Double.isNaN(adjMatrix[current][i]) && visited[i] == 0) {
					relax(current, i, dst, edgeTo);
				}
			}
			visited[current] = 1;

			double shortest = Integer.MAX_VALUE;
			for (int i = 0; i < dst.length; i++) {
				if (visited[i] != 1 && shortest > dst[i]) {
					current = i;
					shortest = dst[i];
				}
			}
			processed++;
		}

		if (dst[to] == Double.POSITIVE_INFINITY) {
			return "No path exists!";
		}

		int u = from;
		int v = to;
		String path = "";

		while (v != u) {
			path = "->" + edgeTo[v] + path;
			v = edgeTo[v];
		}
		path = path + "->" + to;

		return "weight: " + dst[to] + ", path: " + path;
	}

	private void relax(int from, int to, double[] dst, int[] edgeTo) {
		if (dst[to] > dst[from] + adjMatrix[from][to]) {
			dst[to] = dst[from] + adjMatrix[from][to];
			edgeTo[to] = from;
		}
	}
}