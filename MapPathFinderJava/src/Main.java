import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        LocationGraph graph = loadGraph("graph.ser");
        if (graph == null)
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Location Pathfinder!");
            System.out.println("Enter the number of cities:");
            int numCities = scanner.nextInt();
            graph = new LocationGraph(numCities);
        }

        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline left-over
            switch (choice)
            {
                case 1:
                    addCity(graph, scanner);
                    saveGraph(graph, "graph.ser");
                    break;
                case 2:
                    addPath(graph, scanner);
                    saveGraph(graph, "graph.ser");
                    break;
                case 3:
                    graph.display();
                    break;
                case 4:
                    removeCity(graph, scanner);
                    saveGraph(graph, "graph.ser");
                    break;
                case 5:
                    System.out.println("Thank you for using the Location Pathfinder. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("----- Menu -----");
        System.out.println("1. Add a city");
        System.out.println("2. Add a path");
        System.out.println("3. Display the graph");
        System.out.println("4. Remove a city");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addCity(LocationGraph graph, Scanner scanner) {
        System.out.println("----- Add a City -----");
        System.out.println("Enter the city ID:");
        String cityId = scanner.nextLine();
        System.out.println("Enter the city name:");
        String cityName = scanner.nextLine();
        City city = new City(cityId, cityName);
        graph.addCity(city);
        System.out.println("City added successfully!");
    }

    private static void addPath(LocationGraph graph, Scanner scanner) {
        System.out.println("----- Add a Path -----");
        System.out.println("Enter the first city ID:");
        String cityId1 = scanner.nextLine();
        System.out.println("Enter the second city ID:");
        String cityId2 = scanner.nextLine();
        System.out.println("Enter the distance:");
        double distance = scanner.nextDouble();
        scanner.nextLine(); // consume newline left-over
        System.out.println("Is the path available? (yes/no)");
        String isAvailableStr = scanner.nextLine();
        boolean isAvailable = isAvailableStr.equalsIgnoreCase("yes");
        Path path = new Path(new City(cityId1, ""), new City(cityId2, ""), distance, isAvailable);
        graph.addPath(path);
        System.out.println("Path added successfully!");
    }

    private static void removeCity(LocationGraph graph, Scanner scanner) {
        System.out.println("----- Remove a City -----");
        System.out.println("Enter the city ID to remove:");
        String cityId = scanner.nextLine();
        graph.removeCity(cityId);
        System.out.println("City removed successfully!");
    }

    private static void saveGraph(LocationGraph graph, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(graph);
            System.out.println("Graph saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LocationGraph loadGraph(String filename)
    {
        LocationGraph graph = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename)))
        {
            graph = (LocationGraph) ois.readObject();
            System.out.println("Graph loaded successfully.");
        }
        catch (IOException | ClassNotFoundException e)
        {
            // e.printStackTrace(); // Uncomment this line if you want to print stack trace when the file doesn't exist
        }
        return graph;
    }
}


// Map For Locations implemented using a Graph

class LocationGraph implements Serializable {
    double[][] adjacencyMatrix;
    City[] cities;

    public LocationGraph(int numCities) {
        adjacencyMatrix = new double[numCities][numCities];
        cities = new City[numCities];
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i == j) {
                    adjacencyMatrix[i][j] = 0;
                } else {
                    adjacencyMatrix[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    public void removeCity(String cityId) {
        int index = findCityIndex(cityId);
        if (index != -1) {
            // Create a new array for cities
            City[] newCities = new City[cities.length - 1];

            // Copy over cities, skipping the removed city
            int newIndex = 0;
            for (int i = 0; i < cities.length; i++) {
                if (i == index) continue;  // Skip the removed city
                newCities[newIndex] = cities[i];
                newIndex++;
            }

            // Replace old cities array with the new one
            cities = newCities;

            // Update the adjacency matrix
            double[][] newAdjacencyMatrix = new double[cities.length][cities.length];
            for (int i = 0; i < cities.length; i++) {
                for (int j = 0; j < cities.length; j++) {
                    newAdjacencyMatrix[i][j] = adjacencyMatrix[i][j];
                }
            }
            adjacencyMatrix = newAdjacencyMatrix;
        }
    }

    public void addCity(City city) {
        // Find the first null index in cities array and add the new city
        for (int i = 0; i < cities.length; i++) {
            if (cities[i] == null) {
                cities[i] = city;
                break;
            }
        }
    }

    public void addPath(Path path) {
        int index1 = findCityIndex(path.city1.city_id);
        int index2 = findCityIndex(path.city2.city_id);
        if (index1 != -1 && index2 != -1) {
            adjacencyMatrix[index1][index2] = path.path_distance;
            adjacencyMatrix[index2][index1] = path.path_distance;
        }
    }

    private int findCityIndex(String cityId)
    {
        for (int i = 0; i < cities.length; i++) {

            if (cities[i] != null && cities[i].city_id.equals(cityId))
            {
                return i;
            }
        }
        return -1;
    }

    public void display() {
        for (int i = 0; i < cities.length; i++) {
            for (int j = 0; j < cities.length; j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}


class City implements Serializable
{
    String city_id;
    String city_name;

    public City(String city_id, String city_name)
    {
        this.city_id = city_id;
        this.city_name = city_name;
    }
}

class Path implements Serializable
{
    String path_id;
    City city1;
    City city2;
    double path_distance;
    boolean path_isAvailable;

    public Path(City city1, City city2, double distance, boolean isAvailable)
    {
        this.city1 = city1;
        this.city2 = city2;
        this.path_distance = distance;
        this.path_isAvailable = isAvailable;
    }

    // Sets Path Availability
    public void blockPath()
    {
        this.path_isAvailable = false;
    }

    public void unblockPath()
    {
        this.path_isAvailable = true;
    }
}
