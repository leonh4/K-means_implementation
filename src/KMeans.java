import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class KMeans {
    int iterations;
    List<Vec> dataSet;
    int k;

    Map<Integer, List<Vec>> clusters;
    /*
        IdKlastra -> [v1, v2, ... ] - wektory należące do klastra
     */
    Map<Integer, double[]> centroids;
    /*
        IdCentroidu (które odpowiada IdKlastra) -> wektor
     */

    public KMeans(int k, String filePath) {
        this.iterations = 0;
        this.k = k;

        loadDataSet(filePath);
        initClusters();
        initCentroids();
        assignToClusters();
    }

    private void loadDataSet(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath)); // Pobranie wszytkich linijek w pliku do listy

            if (lines.size() < k) throw new IllegalArgumentException("The 'k' parameter is larger than the number of vectors in a given file. " + filePath) ;
            if (lines.isEmpty()) throw new IllegalArgumentException("Empty file " + filePath); // pusty plik

            int vectorDimension = lines.getFirst().length() - 1; // wymiar wektorów w pliku, odemjowane jest 1 ponieważ ostatnią wartością w linijce jest klasa wektora
            dataSet = new ArrayList<>(); // inicjalizacja pola klasy
            for (String line : lines) {
                String[] split = line.split(",");
                if (split.length - 1 != vectorDimension) throw new IllegalArgumentException("Dimensions of vectors don't match in give file. " + filePath); // jeżeli wymiary wektorów w pliku byłyby różne
                double[] tmpPoints = new double[vectorDimension]; // tymczasowa tablica przechowująca punkty wektora kolejnej linijki w pliku
                for (int i = 0; i < tmpPoints.length; i++) {
                    tmpPoints[i] = Double.parseDouble(split[i]);
                }
                dataSet.add(new Vec(tmpPoints, split[split.length - 1])); // do zbioru danych dodajemy nowy wektor (Vec - double[] points - wektor i String className - jego klasa)
            }
        } catch (IOException e) {
            System.out.println(e.getClass().getCanonicalName() + " " + e.getMessage());
        }
    }

    /*
        inicjalizacja klastrów, bądź reset przed dodaniem nowych wartości do klastrów po kolejnej iteracji
    */
    private void initClusters() {
        clusters = new HashMap<>();
        for (int i = 0; i < k; i++) {
            clusters.put(i, new ArrayList<>());
        }
    }

    /*
        inicjalizacja centroidów losowymi wartościami
     */
    private void initCentroids(){
        centroids = new HashMap<>();
        Set<Integer> usedIndexes = new HashSet<>(); // wykorzystane centroidy w drodze losowania
        int i = 0;
        while (usedIndexes.size() < k) {
            int randomIndex = (int) (Math.random() * k);
            if (!usedIndexes.contains(randomIndex)) { // żeby wylosowane centrodiy się nie powtarzały
                usedIndexes.add(randomIndex);
                centroids.put(i++, dataSet.get(randomIndex).points);
            }
        }
    }

    /*
        przypisane wektorów do klastrów na podstawie dystansu do najbliższego centroidu
     */
    private void assignToClusters() {
        initClusters(); // czyszczenie mapy z klastrami
        for (Vec vector : dataSet) {
            int closest = -1;
            double minDistance = Double.MAX_VALUE;
            for (Map.Entry<Integer, double[]> entry : centroids.entrySet()) { // wyłonienie który centroid jest najbliżej danego wektora
                double distance = euclideanDistance(vector.points, entry.getValue());
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = entry.getKey();
                }
            }
            clusters.get(closest).add(vector); // przypisane wektora do odpowiedniego klastra
        }
    }

    /*
        Klasyfikacja
            1. przypisz wektory do odpowiedniego klastra
            2. oblicz centroid do dla każdego klastra
            4. Oblicz błąd i porównaj z błędem w poprzedniej iteracji. Warunek stopu poprzedni i obecny błąd są takie same
     */
    public void classify() {
        double prevError = Double.MAX_VALUE;
        double currError;
        while (true) {
            iterations++;
            assignToClusters();
            updateCentroids();
            currError = updateError();
            if (currError == prevError) break;
            prevError = currError;
        }
        printCluster();
        System.out.println("Iterations: " + iterations);
        System.out.println("Error: " + currError);
    }

    /*
        wyliczenie nowego błędu na podstawie obecnej zawartości mapy z klastrami
     */
    private double updateError() {
        double sum = 0.0;
        for (Map.Entry<Integer, List<Vec>> entry : clusters.entrySet()) {
            int clusterId = entry.getKey();
            double[] centroid = centroids.get(clusterId);
            for (Vec vector : entry.getValue()) {
                sum += euclideanDistance(vector.points, centroid);
            }
        }
        return sum;
    }

    /*
        obliczenie wartości centroidów na podstawie zawrtości klastrów
     */
    private void updateCentroids() {
        for (Map.Entry<Integer, List<Vec>> entry : clusters.entrySet()) {
            List<Vec> cluster = entry.getValue();
            double[] newCentroid = new double[cluster.getFirst().points.length];

            for (Vec vector : cluster) {
                for (int i = 0; i < vector.points.length; i++) {
                    newCentroid[i] += vector.points[i];
                }
            }
            for (int i = 0; i < newCentroid.length; i++) {
                newCentroid[i] /= cluster.size();
            }
            centroids.put(entry.getKey(), newCentroid);
        }
    }

    /*
        wyświetlenie zawartości klastrów, format:
        Grupa IdGrupy (ilośc wektorów w grupie X):
            wektor1
            ...
            wektorX
     */
    private void printCluster() {
        for (Map.Entry<Integer, List<Vec>> entry : clusters.entrySet()) {
            int clusterId = entry.getKey();

            System.out.println("Group " + clusterId + " (number vectors in group " + entry.getValue().size() + "):" );
            for (Vec vector : entry.getValue()) {
                System.out.println("\t" + vector);
            }
        }
    }

    private static double euclideanDistance(double[] vector1, double[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }
        double distance = 0;

        for (int i = 0; i < vector1.length; i++) {
            distance += Math.pow( vector1[i] - vector2[i], 2);
        }
        return Math.sqrt(distance);
    }
}
