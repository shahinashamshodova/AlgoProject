import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Stops {


	public class Time {
		public int h, m, s;
		public String time;

		public Time(int h, int m, int s) {
			this.h = h;
			this.m = m;
			this.s = s;
		}

		public Time(String input) {
			this.time = input;
			String[] tokens = input.split(":");
			this.h = Integer.parseInt(tokens[0]);
			this.m = Integer.parseInt(tokens[1]);
			this.s = Integer.parseInt(tokens[2]);
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}

			if (!(o instanceof Time)) {
				return false;
			}

			Time t = (Time) o;

			return this.h == t.h && this.m == t.m && this.s == t.s;
		}

		public String toString() {
			return this.time;
		}
	}

	public class TripData {
		public int trip_id;
		public Time arrival_time;
		public Time departure_time;
		public int stop_id;
		public int stop_sequence;
		public String stop_headsign;
		public int pickup_type;
		public int drop_off_type;
		public double shape_dist_traveled;

		public TripData(int trip_id, Time arrival_time, Time departure_time, int stop_id, int stop_sequence,
				String stop_headsign, int pickup_type, int drop_off_type, double shape_dist_traveled) {
			this.trip_id = trip_id;
			this.arrival_time = arrival_time;
			this.departure_time = departure_time;
			this.stop_id = stop_id;
			this.stop_sequence = stop_sequence;
			this.stop_headsign = stop_headsign;
			this.pickup_type = pickup_type;
			this.drop_off_type = drop_off_type;
			this.shape_dist_traveled = shape_dist_traveled;
		}
	}

	public List<TripData> TripInfos;

	Stops(String filename) {
		File file;
		try {
			TripInfos = new ArrayList<>();
			double shape_dist_traveled = -1;
			file = new File(filename);
			Scanner scanner = new Scanner(file);
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String tokens[] = scanner.nextLine().split("\\s+|,\\s*");
				int trip_id = (tokens[0] != "") ? Integer.parseInt(tokens[0]) : -1;
				Time arrival_time = new Time(tokens[1]);
				Time departure_time = new Time(tokens[2]);
				int stop_id = (tokens[3] != "") ? Integer.parseInt(tokens[3]) : -1;
				int stop_sequence = (tokens[4] != "") ? Integer.parseInt(tokens[4]) : -1;
				String stop_headsign = (tokens[5] != "") ? tokens[5] : null;
				int pickup_type = (tokens[6] != "") ? Integer.parseInt(tokens[6]) : -1;
				int drop_off_type = (tokens[7] != "") ? Integer.parseInt(tokens[7]) : -1;
				try {
					shape_dist_traveled = (tokens[8] != "") ? Double.parseDouble(tokens[8]) : -1;
				} catch (Exception e1) {
					shape_dist_traveled = -1;
				}

				TripInfos.add(new TripData(trip_id, arrival_time, departure_time, stop_id, stop_sequence,
						stop_headsign, pickup_type, drop_off_type, shape_dist_traveled));
			}
			scanner.close();
		} catch (Exception e) {
			System.out.println("Initialisation failed - Stops");
		}
	}

	public static Comparator<TripData> sortByTripId = new Comparator<TripData>() {
		@Override
		public int compare(TripData a, TripData b) {
			return a.trip_id - b.trip_id;
		}
	};

	public List<TripData> getStopsInfo(String timeString) {

		List<TripData> stopsByArrival = new ArrayList<>();
		try {
			Time arrivalTime = new Time(timeString);
			for (int i = 0; i < TripInfos.size(); i++) {
				if ((TripInfos.get(i).arrival_time).equals(arrivalTime)) {
					stopsByArrival.add(TripInfos.get(i));

				}
			}
			Collections.sort(stopsByArrival, Stops.sortByTripId);

			return stopsByArrival;
		} catch (Exception e) {
			System.out.println("String input is not in time format");
			return null;
		}

	}
}