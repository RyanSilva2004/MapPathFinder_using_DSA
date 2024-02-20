import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of cities:");
        int numCities = scanner.nextInt();
        LocationGraph graph = new LocationGraph(numCities);
        loadSavedData(graph); // Load saved data if exists

        Runtime.getRuntime().addShutdownHook(new Thread(() -> saveData(graph)));

        while (true) {
            System.out.println("Enter 1 to add a city, 2 to add a path, 3 to display the graph, or 4 to exit:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline left-over
            if (choice == 1) {
                System.out.println("Enter the city ID:");
                String cityId = scanner.nextLine();
                System.out.println("Enter the city name:");
                String cityName = scanner.nextLine();
                City city = new City(cityId, cityName);
                graph.addCity(city);
            } else if (choice == 2) {
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
            } else if (choice == 3) {
                graph.display();
            } else if (choice == 4) {
                break;
            }
        }

        scanner.close();
    }

    private static void loadSavedData(LocationGraph graph) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("data.ser"))) {
            ArrayList<City> cities = (ArrayList<City>) inputStream.readObject();
            double[][] adjacencyMatrix = (double[][]) inputStream.readObject(); // Load adjacency matrix
            graph.cities = cities; // Set the loaded cities
            graph.setAdjacencyMatrix(adjacencyMatrix); // Set the loaded adjacency matrix
        } catch (IOException | ClassNotFoundException e) {
            // Ignore if file doesn't exist or error reading file
        }
    }

    private static void saveData(LocationGraph graph) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data.ser"))) {
            outputStream.writeObject(graph.getCities());
            outputStream.writeObject(graph.getAdjacencyMatrix()); // Save adjacency matrix
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

//Map For Locations implemented using a Graph
class LocationGraph {
    double[][] adjacencyMatrix;
    ArrayList<City> cities;

    public LocationGraph(int numCities) {
        adjacencyMatrix = new double[numCities][numCities];
        cities = new ArrayList<>();
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
            // Remove the city from the adjacency matrix
            for (int i = index; i < cities.size() - 1; i++) {
                for (int j = 0; j < cities.size(); j++) {
                    adjacencyMatrix[i][j] = adjacencyMatrix[i + 1][j];
                }
            }
            for (int j = index; j < cities.size() - 1; j++) {
                for (int i = 0; i < cities.size(); i++) {
                    adjacencyMatrix[i][j] = adjacencyMatrix[i][j + 1];
                }
            }
            // Resize the adjacency matrix
            double[][] newAdjacencyMatrix = new double[cities.size() - 1][cities.size() - 1];
            for (int i = 0; i < cities.size() - 1; i++) {
                for (int j = 0; j < cities.size() - 1; j++) {
                    newAdjacencyMatrix[i][j] = adjacencyMatrix[i][j];
                }
            }
            adjacencyMatrix = newAdjacencyMatrix;

            // Remove the city from the cities list
            cities.remove(index);
        }
    }



    public void addCity(City city) {
        cities.add(city);
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
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).city_id.equals(cityId)) {
                return i;
            }
        }
        return -1;
    }

    public void display() {
        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    public ArrayList<City> getCities() {
        return cities;
    }
    public double[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setAdjacencyMatrix(double[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }
}

class City implements Serializable {
    String city_id;
    String city_name;

    public City(String city_id, String city_name) {
        this.city_id = city_id;
        this.city_name = city_name;
    }
}

class Path {
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

    //Sets Path Availability
    public void blockPath() {
        this.path_isAvailable = false;
    }

    public void unblockPath() {
        this.path_isAvailable = true;
    }
}
