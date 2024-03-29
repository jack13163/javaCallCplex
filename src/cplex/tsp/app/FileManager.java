package cplex.tsp.app;

import cplex.tsp.graph.City;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager {

	private HashMap<String, String> args;

	FileManager(HashMap<String, String> args) {
		this.args = args;
	}

	public ArrayList<City> readInstance() {
		ArrayList<City> instance = new ArrayList<City>();

		try (BufferedReader br = new BufferedReader(new FileReader(new File(args.get("--instancePath"))))) {
			br.readLine();
			String line;
			int count = 0;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				double x = Double.parseDouble(parts[0]);
				double y = Double.parseDouble(parts[1]);
				instance.add(new City(count++, x, y));
				if (count == Integer.parseInt(this.args.get("--maximumRead"))) {
					break;
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			// TODO: handle exception
		}

		return instance;
	}

}
