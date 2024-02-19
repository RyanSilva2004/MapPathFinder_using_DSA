public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
class Graph
{
    City[] cities;
    Path[] paths;
    int citycount;
    int pathcount;

    public Graph(City[] cities, Path[] paths)
    {
        this.cities=cities;
        this.paths=paths;
        citycount = 0;
        pathcount = 0;
    }

    public void addCity(City city_id, City city_name)
    {
        City city = new City();
        city.city_id = city_id;
        city.city_name = city_name;
        cities[citycount] = city;
        citycount++;
    }
    public void addPath(String path_id, City city1, City city2, double distance)
    {
        Path path = new Path(city1,city2,distance);
        paths[pathcount] = path;
        pathcount++;
    }



}
class City
{
    String city_id;
    String city_name;
}
class Path
{
    String path_id;
    City city1;
    City city2;
    double distance;
    public Path(City city1,City city2, double distance)
    {
        this.distance=distance;
    }

}
