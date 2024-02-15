package task_20231116;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class s1126111_SA {

	static class Edge {
		int node1;
		int node2;

		Edge(int node1, int node2) {
			this.node1 = node1;
			this.node2 = node2;
		}

		public void getInfo() {
			System.out.println("node1.." + node1 + "node2.." + node2);
		}
	}

	static class Solution {
		Map<Integer, Integer> partition;

		Solution(Map<Integer, Integer> partition) {
			this.partition = partition;
		}

		// Method to calculate the number of edges between different partitions
		int calculateCost(List<Edge> edges) {
			int cost = 0;
			for (Edge edge : edges) {
				if (!partition.get(edge.node1).equals(partition.get(edge.node2))) {
					cost++;
				}
			}
			return cost;
		}
	}

	public static Solution simulatedAnnealing(List<Edge> edges, List<Integer> nodes) {
		Random random = new Random();
		DecimalFormat df = new DecimalFormat("#.######");// double format
		Map<Integer, Integer> currentPartition = new HashMap<>();
		Integer numNodes = nodes.size();

		// Randomly assigning nodes to partitions
		for (Integer n : nodes) {
			currentPartition.put(n, random.nextInt(2));
		}

		// simulatedAnnealing config
		Solution currentSolution = new Solution(currentPartition);
		double temperature = 1.0;
		double freezeTemperature = 0.00001;
		double alpha = 0.95;
		Integer attempts = 100;
		double freezeTime = 600000;// ms
		double startTime = System.currentTimeMillis();// record start time to calculate use time
		double endTime = System.currentTimeMillis() + freezeTime;

		long count = 0;// count run times
		long balance = Math.round((double) nodes.size() / 2);// round|V|
		System.out.println(balance);
		int partitionSize = 0; // controller balance
		System.out.println("Initial temperature: " + temperature + "   Annealing coefficient: " + alpha);
		System.out.println("Initial cost: " + currentSolution.calculateCost(edges));

		while (temperature > freezeTemperature && System.currentTimeMillis() < endTime) {

			for (int i = 0; i < attempts; i++) {

				// sum(V)
				partitionSize = (int) currentPartition.values().stream().filter(val -> val == 1).count();

				// chose 1/0 node to change
				int chose = 0;

				if (partitionSize >= balance)
					chose = 1;
//				System.out.println("partitionSize.."+partitionSize+"..balance.."+balance+"..chose.."+chose);
				Map<Integer, Integer> newPartition = new HashMap<>(currentPartition);
				int nodeToChange = nodes.get(random.nextInt(numNodes));// index
				while (newPartition.get(nodeToChange) != chose) {

					nodeToChange = nodes.get(random.nextInt(numNodes));
				}
				newPartition.put(nodeToChange, 1 - newPartition.get(nodeToChange));
				Solution newSolution = new Solution(newPartition);

				int energyOld = currentSolution.calculateCost(edges);
				int energyNew = newSolution.calculateCost(edges);
				int deltaEnergy = energyNew - energyOld;

				if (deltaEnergy < 0 || Math.exp(-deltaEnergy / temperature) > Math.random()) {

					currentPartition = newPartition;
					currentSolution = newSolution;
				}
				count++;
			}
			temperature *= alpha;
			System.out.println(
					"Cost at temperature " + df.format(temperature) + "   " + currentSolution.calculateCost(edges));

		}
		int partition1Size = (int) currentPartition.values().stream().filter(val -> val == 0).count();
		int partition2Size = (int) currentPartition.values().stream().filter(val -> val == 1).count();
		System.out.println("balance.." + partition1Size + "..." + partition2Size);
		System.out.println(count);
		System.out.println("Use time: " + (System.currentTimeMillis() - startTime) + "ms");

		System.out.println("Final Partitioning:");
		System.out.println("Final temperature: " + df.format(temperature));

		System.out.println("edge size: " + edges.size());
		System.out.println("node size: " + nodes.size());
		System.out.println("Best cost: " + currentSolution.calculateCost(edges));
		return null;
	}

	public static void generateLPModel(List<Edge> edges, List<Integer> nodes, String fileName) {
		String filePath = "D:\\LPmodel\\" + fileName;//output filepath
		String fileString = "min : ";//output content
		Map<String, Integer> countMap = new HashMap();//count #of-nodes times

		//count
		for (Edge edge : edges) {
			if (countMap.containsKey("x" + edge.node1)) {
				countMap.put(("x" + edge.node1), countMap.get("x" + edge.node1) + 1);
			} else {
				countMap.put(("x" + edge.node1), 1);
			}

			if (countMap.containsKey("x" + edge.node2)) {
				countMap.put(("x" + edge.node2), countMap.get("x" + edge.node2) + 1);
			} else {
				countMap.put(("x" + edge.node2), 1);
			}
		}
		// System.out.println("countMap.."+countMap);
		System.out.println(edges.size());
		System.out.println(nodes.size());
		
		//generate content
		for (int i = 1; i < nodes.size() + 1; i++) {
			fileString += (countMap.get("x" + i) == 1 ? "" : countMap.get("x" + i)) + "x" + i + " + ";
		}
		fileString = fileString.substring(0, fileString.length() - 2);
		fileString += "-";
		for (int i = 1; i < edges.size() + 1; i++) {
			fileString += "2y" + i + " -";
		}

		fileString = fileString.substring(0, fileString.length() - 2);

		fileString += ";\n";
		for (Integer node : nodes) {
			fileString += "x" + node + " +";
		}
		fileString = fileString.substring(0, fileString.length() - 2);
		fileString += " = " + Math.round(Math.round((double) nodes.size() / 2)) + ";\n";

		for (int i = 1; i < edges.size() + 1; i++) {
			Edge edge = edges.get(i - 1);
			fileString += "y" + i + " <= x" + edge.node1 + ";\n";
			fileString += "y" + i + " <= x" + edge.node2 + ";\n";
			fileString += "y" + i + " >= x" + edge.node1 + " + x" + edge.node2 + "-1;\n\n";
		}

		fileString += "bin ";

		for (Integer node : nodes) {
			fileString += "x" + node + ", ";
		}

		fileString = fileString.substring(0, fileString.length() - 2);
		fileString += ";";
//		System.out.println(fileString);

		// file output
		try {
			FileWriter fileWriter = new FileWriter(new File(filePath));

			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(fileString);

			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("OK..");
	}

	public static void hMetisCostCalc(List<Integer> nodes, List<Edge> edges, String filename) throws Exception {
		String filePath = "task_20231116/" + filename + ".part.2";
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		List<Integer> metisList = new ArrayList();
		System.out.println(filePath);
		Map<Integer,Integer> map = new HashMap();
		
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
//			System.out.println(line);
			metisList.add(Integer.parseInt(line));

		}
		for (int i = 0; i < nodes.size(); i++) {
			map.put(nodes.get(i), metisList.get(i));
		}
		
		Solution solution = new Solution(map);
		Integer cost = solution.calculateCost(edges);
//		System.out.println(nodes);
//		System.out.println(metisList);
//		System.out.println(edges);
		System.out.println("nodes size:"+nodes.size());
		System.out.println("metis size:"+metisList.size());
		System.out.println("edge size:"+edges.size());
		System.out.println("cost:"+cost);
	}

	//main func to read file and analysis data
	public static void main(String[] args) throws Exception {
		List<String> list = Arrays.asList("s38584o");
		for (String str : list) {

			List<Edge> edges = new ArrayList<>();
			Set<Integer> nodesSet = new HashSet();
			String fileName = str;
			String filePath = "task_20231116/" + fileName;
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			int lineNum = 1;
			while (line != null) {
				if (lineNum > 1) {
					String[] edgeStr = line.split(" ");
					edges.add(new Edge(Integer.parseInt(edgeStr[0]), Integer.parseInt(edgeStr[1])));
					nodesSet.add(Integer.parseInt(edgeStr[0]));
					nodesSet.add(Integer.parseInt(edgeStr[1]));

				}
				line = reader.readLine();
				lineNum++;
			}

			// change set to list for index search;
			List<Integer> nodes = new ArrayList(nodesSet);

			reader.close();

			// just count
			System.out.println("fileName.." + fileName + "..nodes.." + nodes.size() + "..edges.." + edges.size());

			// SA FUNC
//			simulatedAnnealing(edges, nodes);

			// generate FUNC
//			generateLPModel(edges, nodes, fileName);

			// HMetis cost
			hMetisCostCalc(nodes, edges, str);

//			for (Map.Entry<Integer, Integer> entry : finalSolution.partition.entrySet()) {
//				System.out.println("Node " + entry.getKey() + " in partition " + entry.getValue());
//			}

		}

	}

}
