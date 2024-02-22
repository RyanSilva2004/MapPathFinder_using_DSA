import java.io.*;
import java.util.*;

class ShortestPathResult
{
    CustomQueue path;
    double distance;

    public ShortestPathResult(CustomQueue path, double distance)
    {
        this.path = path;
        this.distance = distance;
    }
}

public class Main
{
    public static void main(String[] args)
    {
        LocationGraph graph = loadGraph("graph.ser");
        if (graph == null) {
            // Create a new graph if loading fails
            Scanner scanner = new Scanner(System.in);
            int numCities;
            do {
                System.out.println("Welcome to the Location Pathfinder!");
                System.out.println("Enter the number of cities:");
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter the Number of Cities:");
                    scanner.next(); // Consume the invalid input
                }
                numCities = scanner.nextInt();
                scanner.nextLine(); // Consume newline left-over
                if (numCities <= 0) {
                    System.out.println("Number of cities must be greater than 0. Please enter the Number of Cities:");
                }
            } while (numCities <= 0);

            // Create a new graph with the specified number of cities
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
                    findShortestPath(graph, scanner);
                    break;
                case 6:
                    System.out.println("Thank you for using the Location Pathfinder. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private static void printMenu()
    {
        System.out.println("----- Menu -----");
        System.out.println("1. Add a city");
        System.out.println("2. Add/Update a path");
        System.out.println("3. Display the graph");
        System.out.println("4. Remove a city");
        System.out.println("5. Find the shortest path between cities");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addCity(LocationGraph graph, Scanner scanner)
    {
        System.out.println("----- Add a City -----");
        System.out.println("Enter the city ID:");
        String cityId;
        boolean isUnique;
        do {
            cityId = scanner.nextLine();
            isUnique = graph.isCityIdUnique(cityId);
            if (!isUnique) {
                System.out.println("City ID already exists. Please enter a unique city ID:");
            }
        } while (!isUnique);

        System.out.println("Enter the city name:");
        String cityName = scanner.nextLine();
        City city = new City(cityId, cityName);
        graph.addCity(city);
        System.out.println("City added successfully!");
    }


    private static void addPath(LocationGraph graph, Scanner scanner)
    {
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

        // Check if the city ID exists in the graph
        if (!graph.isCityIdExist(cityId)) {
            System.out.println("City ID does not exist. Please enter a valid city ID.");
            return;
        }

        // Remove the city if it exists
        graph.removeCity(cityId);
        System.out.println("City removed successfully!");
    }

    private static void saveGraph(LocationGraph graph, String filename)
    {
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

    private static void findShortestPath(LocationGraph graph, Scanner scanner) {
        System.out.println("----- Find Shortest Path -----");
        System.out.println("Enter the ID of the start city:");
        String startCityId = scanner.nextLine();
        System.out.println("Enter the ID of the destination city:");
        String endCityId = scanner.nextLine();
        System.out.println("Applying Dijkstra's Algorithm...");
        ShortestPathResult result = graph.shortestPathBetweenCities(startCityId, endCityId);
        if (result != null) {
            System.out.println("Shortest path between " + graph.getCityName(startCityId) + " and " +
                    graph.getCityName(endCityId) + ":");
            CustomQueue path = result.path;
            String[] pathArray = path.toArray();

            for(int i = pathArray.length - 1; i >= 0; --i) {
                System.out.print(graph.getCityName(pathArray[i]));
                if (!pathArray[i].equals(endCityId)) {
                    System.out.print(" >> ");
                }
            }

            System.out.println("\nDistance: " + result.distance);
            System.out.println("Do you want to start the journey? (yes/no)");
            String startJourneyResponse = scanner.nextLine().toLowerCase();
            if (startJourneyResponse.equals("yes")) {
                System.out.println("\nStarting the journey...");
                double remainingDistance = result.distance;

                for (int i = pathArray.length - 2; i >= 0; --i) {
                    String currentCity = pathArray[i];
                    String nextCity = pathArray[i + 1];
                    double distanceToNextCity = graph.distanceBetweenCities(currentCity, nextCity);
                    System.out.println("Crossing through " + graph.getCityName(currentCity) + " - Remaining distance: " + remainingDistance);
                    System.out.println("Do you want to continue to " + graph.getCityName(nextCity) + "? (yes/no)");
                    String continueResponse = scanner.nextLine().toLowerCase();
                    if (continueResponse.equals("no")) {
                        System.out.println("Journey stopped at " + graph.getCityName(currentCity));
                        break;
                    }

                    remainingDistance -= distanceToNextCity;
                }


                System.out.println("You have reached your destination: " + graph.getCityName(endCityId));
            } else {
                System.out.println("Journey not started. Exiting...");
            }
        }
    }




}

class LocationGraph implements Serializable
{
    double[][] adjacencyMatrix;
    boolean[][] graphPathsAvailable;
    City[] cities;

    public LocationGraph(int numCities)
    {
        adjacencyMatrix = new double[numCities][numCities];
        graphPathsAvailable = new boolean[numCities][numCities]; // Add this line
        cities = new City[numCities];
        for (int i = 0; i < numCities; i++)
        {
            for (int j = 0; j < numCities; j++)
            {
                if (i == j)
                {
                    adjacencyMatrix[i][j] = 0;
                }
                else
                {
                    adjacencyMatrix[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
    }
    public boolean isCityIdUnique(String cityId) {
        for (City city : cities) {
            if (city != null && city.city_id.equals(cityId)) {
                return false; // City ID already exists
            }
        }
        return true; // City ID is unique
    }
    public boolean isCityIdExist(String cityId) {
        for (City city : cities) {
            if (city != null && city.city_id.equals(cityId)) {
                return true;
            }
        }
        return false;
    }
    public double distanceBetweenCities(String cityId1, String cityId2)
    {
        int index1 = findCityIndex(cityId1);
        int index2 = findCityIndex(cityId2);

        if (index1 == -1 || index2 == -1) {
            System.out.println("One or both cities not found.");
            return Double.POSITIVE_INFINITY; // or throw an exception, depending on your design
        }

        return adjacencyMatrix[index2][index1]; // Reverse the order for distance retrieval
    }

    public ShortestPathResult shortestPathBetweenCities(String startCityId, String endCityId)
    {
        int startIndex = findCityIndex(startCityId);
        int endIndex = findCityIndex(endCityId);

        if (startIndex == -1 || endIndex == -1) {
            System.out.println("One or both cities not found.");
            return null;
        }

        // Dijkstra's algorithm
        double[] distances = new double[cities.length];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[startIndex] = 0;

        boolean[] visited = new boolean[cities.length];

        int[] previousCities = new int[cities.length]; // Store the previous city index for each city

        for (int count = 0; count < cities.length - 1; count++) {
            int u = minDistance(distances, visited);
            visited[u] = true;

            for (int v = 0; v < cities.length; v++) {
                if (!visited[v] && adjacencyMatrix[u][v] != Double.POSITIVE_INFINITY &&
                        distances[u] + adjacencyMatrix[u][v] < distances[v] && isPathAvailable(u, v)) {
                    distances[v] = distances[u] + adjacencyMatrix[u][v];
                    previousCities[v] = u; // Update the previous city for v
                }
            }
        }

        // Reconstruct path
        CustomQueue path = new CustomQueue();
        int current = endIndex;
        while (current != startIndex) {
            path.enqueue(cities[current].city_id);
            current = previousCities[current];
            if (current == -1) {
                System.out.println("No path found between the cities.");
                return null;
            }
        }
        path.enqueue(startCityId);

        return new ShortestPathResult(path, distances[endIndex]);
    }

    private boolean isPathAvailable(int u, int v) {
        // Check if the path between cities u and v is available
        return adjacencyMatrix[u][v] != Double.POSITIVE_INFINITY &&
                adjacencyMatrix[v][u] != Double.POSITIVE_INFINITY &&
                graphPathsAvailable[u][v] && graphPathsAvailable[v][u];
    }



    private int minDistance(double[] distances, boolean[] visited)
    {
        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;
        for (int v = 0; v < cities.length; v++)
        {
            if (!visited[v] && distances[v] <= min)
            {
                min = distances[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    private int findPreviousCity(double[] distances, int current)
    {
        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;
        for (int v = 0; v < cities.length; v++)
        {
            if (adjacencyMatrix[current][v] != Double.POSITIVE_INFINITY && distances[v] < min)
            {
                min = distances[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    public void removeCity(String cityId)
    {
        int index = findCityIndex(cityId);
        if (index != -1) {
            // Create a new array for cities
            City[] newCities = new City[cities.length - 1];

            // Copy over cities, skipping the removed city
            int newIndex = 0;
            for (int i = 0; i < cities.length; i++)
            {
                if (i == index) continue;  // Skip the removed city
                newCities[newIndex] = cities[i];
                newIndex++;
            }

            // Replace old cities array with the new one
            cities = newCities;

            // Update the adjacency matrix
            double[][] newAdjacencyMatrix = new double[cities.length][cities.length];
            for (int i = 0; i < cities.length; i++)
            {
                for (int j = 0; j < cities.length; j++)
                {
                    newAdjacencyMatrix[i][j] = adjacencyMatrix[i][j];
                }
            }
            adjacencyMatrix = newAdjacencyMatrix;
        }
    }

    public void addCity(City city)
    {
        // Find the first null index in cities array and add the new city
        for (int i = 0; i < cities.length; i++)
        {
            if (cities[i] == null)
            {
                cities[i] = city;
                break;
            }
        }
    }

    public void addPath(Path path)
    {
        int index1 = findCityIndex(path.city1.city_id);
        int index2 = findCityIndex(path.city2.city_id);
        if (index1 != -1 && index2 != -1 && path.path_isAvailable)
        {
            adjacencyMatrix[index1][index2] = path.path_distance;
            adjacencyMatrix[index2][index1] = path.path_distance;
            graphPathsAvailable[index1][index2] = true;
            graphPathsAvailable[index2][index1] = true;
        }
    }

    private int findCityIndex(String cityId)
    {
        for (int i = 0; i < cities.length; i++)
        {
            if (cities[i] != null && cities[i].city_id.equals(cityId))
            {
                return i;
            }
        }
        return -1;
    }

    public void display()
    {
        System.out.println("Cities:");
        for (int i = 0; i < cities.length; i++)
        {
            for (int j = 0; j < cities.length; j++)
            {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    public String getCityName(String cityId) {
        for (City city : cities) {
            if (city != null && city.city_id.equals(cityId)) {
                return city.city_name;
            }
        }
        return "City Name Not Found";
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
    String path_name;
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

class CustomQueue
{
    private static final int MAX_SIZE = 100; // Adjust the size as needed
    private String[] array;
    private int front, rear, size;

    public CustomQueue() {
        array = new String[MAX_SIZE];
        front = rear = size = 0;
    }

    public void enqueue(String element)
    {
        if (size == MAX_SIZE)
        {
            System.out.println("Queue is full. Cannot enqueue.");
            return;
        }
        array[rear] = element;
        rear = (rear + 1) % MAX_SIZE;
        size++;
    }

    public String dequeue()
    {
        if (isEmpty())
        {
            System.out.println("Queue is empty. Cannot dequeue.");
            return null;
        }
        String element = array[front];
        front = (front + 1) % MAX_SIZE;
        size--;
        return element;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    // In CustomQueue class
    public String[] toArray() {
        String[] result = new String[size];
        int index = 0;
        int i = front;
        while (index < size) {
            result[index++] = array[i];
            i = (i + 1) % MAX_SIZE;
        }
        return result;
    }

}