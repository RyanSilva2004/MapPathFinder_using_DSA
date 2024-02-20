import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        LocationGraph graph = loadGraph("graph.ser");
        if (graph == null) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Location Pathfinder!");
            System.out.println("Enter the number of cities:");
            int numCities = scanner.nextInt();
            graph = new LocationGraph(numCities);
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline left-over
            switch (choice) {
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

    private static LocationGraph loadGraph(String filename) {
        LocationGraph graph = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            graph = (LocationGraph) ois.readObject();
            System.out.println("Graph loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            // e.printStackTrace(); // Uncomment this line if you want to print stack trace when the file doesn't exist
        }
        return graph;
    }
}

// Custom LinkedList implementation
class MyLinkedList implements Serializable {
    City head;

    void add(String cityId, String cityName) {
        City newNode = new City(cityId, cityName);
        if (head == null) {
            head = newNode;
        } else {
            City current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    void remove(String cityId) {
        if (head == null) return;

        if (head.city_id.equals(cityId)) {
            head = head.next;
            return;
        }

        City current = head;
        while (current.next != null) {
            if (current.next.city_id.equals(cityId)) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
    }

    boolean contains(String cityId) {
        City current = head;
        while (current != null) {
            if (current.city_id.equals(cityId)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}

// Map For Locations implemented using a Graph
class LocationGraph implements Serializable {
    double[][] adjacencyMatrix;
    MyLinkedList cities; // Changed ArrayList to MyLinkedList
    int size;

    public LocationGraph(int numCities) {
        adjacencyMatrix = new double[numCities][numCities];
        cities = new MyLinkedList(); // Changed ArrayList to MyLinkedList
        size = 0;
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
        if (cities.head == null) return;

        // Find the index of the city to remove
        int indexToRemove = findCityIndex(cityId);
        if (indexToRemove == -1) return; // City not found

        // Remove the city from the linked list
        if (cities.head.city_id.equals(cityId)) {
            cities.head = cities.head.next;
            size--;
        } else {
            City current = cities.head;
            while (current.next != null) {
                if (current.next.city_id.equals(cityId)) {
                    current.next = current.next.next;
                    size--;
                    break;
                }
                current = current.next;
            }
        }

        // Remove the corresponding row and column from the adjacency matrix
        for (int i = indexToRemove; i < size; i++) {
            for (int j = 0; j < size; j++) {
                adjacencyMatrix[i][j] = adjacencyMatrix[i + 1][j];
            }
        }
        for (int j = indexToRemove; j < size; j++) {
            for (int i = 0; i < size; i++) {
                adjacencyMatrix[i][j] = adjacencyMatrix[i][j + 1];
            }
        }

        // Remove all paths associated with the deleted city
        for (int i = 0; i < size; i++) {
            adjacencyMatrix[indexToRemove][i] = Double.POSITIVE_INFINITY;
            adjacencyMatrix[i][indexToRemove] = Double.POSITIVE_INFINITY;
        }

        // Update the size of the adjacency matrix
        size--;
    }


    public void addCity(City city) {
        cities.add(city.city_id, city.city_name);
        size++;
    }

    public void addPath(Path path) {
        int index1 = findCityIndex(path.city1.city_id);
        int index2 = findCityIndex(path.city2.city_id);
        if (index1 != -1 && index2 != -1) {
            adjacencyMatrix[index1][index2] = path.path_distance;
            adjacencyMatrix[index2][index1] = path.path_distance;
        }
    }

    private int findCityIndex(String cityId) {
        int index = 0;
        City current = cities.head;
        while (current != null) {
            if (current.city_id.equals(cityId)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    public void display() {
        System.out.println("Cities:");
        City current = cities.head;
        while (current != null) {
            System.out.println(current.city_id + ": " + current.city_name);
            current = current.next;
        }
        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                if (adjacencyMatrix[i][j] == Double.POSITIVE_INFINITY) {
                    System.out.print("Infinity ");
                } else {
                    System.out.print(adjacencyMatrix[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}

class City implements Serializable {
    String city_id;
    String city_name;
    City next;

    public City(String city_id, String city_name) {
        this.city_id = city_id;
        this.city_name = city_name;
        this.next = null;
    }
}

class Path implements Serializable {
    String path_id;
    City city1;
    City city2;
    double path_distance;
    boolean path_isAvailable;

    public Path(City city1, City city2, double distance, boolean isAvailable) {
        this.city1 = city1;
        this.city2 = city2;
        this.path_distance = distance;
        this.path_isAvailable = isAvailable;
    }

    // Sets Path Availability
    public void blockPath() {
        this.path_isAvailable = false;
    }

    public void unblockPath() {
        this.path_isAvailable = true;
    }
}