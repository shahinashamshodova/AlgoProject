import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class UI {

	public static final String USER_OPTIONS = "A - for shortest path\nB - for bus stop search\nC - for arrival time\n:q! - exit";

	public static final String SHORTEST_PATH = "A";
	public static final String BUS_STOP_SEARCH = "B";
	public static final String ARRIVAL_TIME = "C";
	public static final String EXIT = ":q!";

	public static boolean isTime(String input) {
		try {
			String[] tokens = input.split(":");

			int h = Integer.parseInt(tokens[0]);
			int m = Integer.parseInt(tokens[1]);
			int s = Integer.parseInt(tokens[2]);

			return h >= 0 && h <= 23 && m >= 0 && m <= 59 && s >= 0 && s <= 59;
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args) {

		System.out.println("Hello, please wait while the files are loaded!");

		ShortestPath graph = new ShortestPath("stop_times.txt", "transfers.txt");
		TST tst = new TST("stops.txt");
		Stops stops = new Stops("stop_times.txt");

		Scanner input = new Scanner(System.in);
		boolean exited = false;

		while (!exited) {
			System.out.println(USER_OPTIONS);

			if (input.hasNextLine()) {
				if (input.hasNext(EXIT)) {
					System.out.println("Exited");
					exited = true;
				} else if (input.hasNext(SHORTEST_PATH)) {
					input.nextLine();

					System.out.println("Enter bus stops in the following format " +
							"(space separated): bus_stop_id1 bus_stop_id2");

					String userInput = input.nextLine();
					String[] tokens = userInput.split(" ");

					try {
						int stop1 = Integer.parseInt(tokens[0]);
						int stop2 = Integer.parseInt(tokens[1]);

						System.out.println(graph.findShortestPath(stop1, stop2));
					} catch (NumberFormatException e) {
						System.out.println("Incorrent number format");
					}

				} else if (input.hasNext(BUS_STOP_SEARCH)) {
					input.nextLine();
					System.out.println("Enter bus stop name or part of it");
					if (input.hasNextLine()) {
						String userInput = input.nextLine();

						ArrayList<String> search = tst.search(userInput.toUpperCase());
						for (int i = 0; i < search.size(); i++) {
							System.out.println(search.get(i));
						}
					}
				} else if (input.hasNext(ARRIVAL_TIME)) {
					input.nextLine();

					System.out.println("Enter arrival time as hh:mm:ss (24 hour format)");
					if (input.hasNextLine()) {
						String userInput = input.nextLine();

						System.out.println(userInput);

						if (isTime((userInput))) {
							List<Stops.TripData> myStops = stops.getStopsInfo(userInput);
							if (myStops != null && myStops.size() > 0) {
								System.out.println("Results count: " + myStops.size());

								for (Stops.TripData s : myStops) {
									System.out.println("Trip ID:" + s.trip_id);
									System.out.printf(
											"Arrival Time:%s Departure Time:%s %nStop Id:%d %nStop Sequence:%d"
													+ "%nStop Headsign:%s %nPickup Type:%d %nDrop Off Type:%d %nShape "
													+ "Distance Travelled:%.3f%n",
											s.arrival_time.toString(),
											s.departure_time.toString(),
											s.stop_id,
											s.stop_sequence,
											s.stop_headsign,
											s.pickup_type,
											s.drop_off_type,
											s.shape_dist_traveled);
									System.out.println();
								}
							} else {
								System.out.println("Not found");
							}

						} else {
							System.out.println("Wrong time format!");
						}
					}
				} else {
					input.nextLine();
					System.out.println("Not valid menu option!");
				}
			}
		}
		input.close();
	}
}