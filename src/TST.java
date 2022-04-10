import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class TST {

    public class Node {
        public char key;
        public int value;
        public Node centre, left, right;
        public StopData info;

        public Node(char key, int value) {
            this.key = key;
            this.value = value;
            this.centre = null;
            this.left = null;
            this.right = null;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public void setStopInfo(StopData info) {
            this.info = info;
        }
    }

    public Node root;
    public boolean matched;

    TST(String filename) {
        root = null;
        File file = new File(filename);
        try {
            Scanner scanner = new Scanner(file);

            String line = "";

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] tokens = new String[10];

                String[] parsed_tokens = line.split(",");

                for (int i = 0; i < 10; i++) {
                    if (i < parsed_tokens.length) {
                        tokens[i] = parsed_tokens[i];
                    } else {
                        tokens[i] = "N/A";
                    }
                }

                String stopName = tokens[2];
                String prefix = stopName.substring(0, 2);
                if (prefix.equals("NB") || prefix.equals("WB") || prefix.equals("SB") || prefix.equals("EB")) {
                    stopName = stopName.substring(3).concat(" " + prefix);
                }

                StopData newInfo = new StopData(tokens);

                add(stopName.toCharArray(), newInfo);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Initialisation failed - TST");
        }
    }

    public void add(char[] stopName, StopData info) {
        if (stopName.length != 0) {
            if (root == null)
                root = new Node(stopName[0], -1);
            add(stopName, 0, root, info);
        }
    }

    public Node add(char[] stopName, int i, Node node, StopData info) {
        if (node == null)
            node = new Node(stopName[i], -1);

        if (node.key < stopName[i])
            node.left = add(stopName, i, node.left, info);
        else if (node.key > stopName[i])
            node.right = add(stopName, i, node.right, info);
        else if (i < stopName.length - 1)
            node.centre = add(stopName, ++i, node.centre, info);

        if (i == stopName.length - 1) {
            node.setValue(i);
            node.setStopInfo(info);
        }

        return node;
    }

    public void match(Node node, ArrayList<String> match, ArrayList<StopData> info, String prefix) {
        if (node != null) {
            match(node.left, match, info, prefix);
            match(node.centre, match, info, prefix + node.key);
            match(node.right, match, info, prefix);

            if (node.value != -1) {
                prefix += node.key;
                match.add(prefix);
                info.add(node.info);
            }
        }
    }

    public Node search(char[] stopName, int i, Node node) {
        if (node != null) {
            if (node.key < stopName[i])
                node = search(stopName, i, node.left);
            else if (node.key > stopName[i])
                node = search(stopName, i, node.right);
            else if (i < stopName.length - 1)
                node = search(stopName, ++i, node.centre);

            if (node != null && i == stopName.length - 1 && node.value != -1)
                matched = true;

            return node;
        }
        return null;
    }

    public ArrayList<String> search(String input) {

        matched = false;
        ArrayList<String> results = new ArrayList<String>();
        ArrayList<StopData> results_info = new ArrayList<StopData>();
        Node initialResult = search(input.toCharArray(), 0, root);

        if (matched) {
            System.out.println("Matched with " + input);
            results.add(input);
        }
        if (initialResult != null) {
            match(initialResult.centre, results, results_info, "");
            System.out.println("Results number: " + results.size());
            int i;
            if (matched) {
                i = 1;
            } else {
                i = 0;
            }

            for (; i < results.size(); i++) {
                results.set(i, input + results.get(i) + ", stop_code: " + results_info.get(i).stop_code);
            }
        } else {
            System.out.println("No match found.");
        }
        return results;
    }

    public class StopData {

        public String stop_id;
        public String stop_code;
        public String stop_name;
        public String stop_desc;
        public String stop_lat;
        public String stop_lon;
        public String zone_id;
        public String stop_url;
        public String location_type;
        public String parent_station;

        public StopData(String[] tokens) {
            this.stop_id = tokens[0];
            this.stop_code = tokens[1];
            this.stop_name = tokens[2];
            this.stop_desc = tokens[3];
            this.stop_lat = tokens[4];
            this.stop_lon = tokens[5];
            this.zone_id = tokens[6];
            this.stop_url = tokens[7];
            this.location_type = tokens[8];
            this.parent_station = tokens[9];
        }
    }

}