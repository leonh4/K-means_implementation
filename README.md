# K-Means Clustering Implementation

Projekt stanowi implementację algorytmu k-średnich (K-Means). Jest to algorytm uczenia nienadzorowanego służący do grupowania danych (klasteryzacji) w $k$ rozłącznych zbiorów na podstawie podobieństwa cech.

## Zasada działania algorytmu

Program wykonuje iteracyjny proces optymalizacji położenia centroidów i przynależności punktów do grup:

1. **Inicjalizacja**: Losowe wybranie $k$ punktów ze zbioru danych jako początkowe środki ciężkości (centroidy).
    
2. **Przypisanie (Assignment)**: Każdy wektor ze zbioru danych jest przypisywany do najbliższego centroidu na podstawie metryki odległości euklidesowej:
    
    $$d(x, y) = \sqrt{\sum_{i=1}^{n} (x_i - y_i)^2}$$
    
3. **Aktualizacja (Update)**: Nowe położenie każdego centroidu jest obliczane jako średnia arytmetyczna współrzędnych wszystkich punktów przypisanych do danej grupy.
    
4. **Warunek stopu**: Algorytm kończy działanie, gdy całkowity błąd kwadratowy (suma odległości punktów od ich centroidów) przestaje się zmieniać między iteracjami.
    

## Funkcjonalności

- **Interfejs graficzny wyboru pliku**: Wykorzystanie klasy `JFileChooser` do wygodnego wskazywania źródła danych.
    
- **Obsługa formatu CSV**: Program wczytuje dane w formacie tekstowym, gdzie ostatnia kolumna traktowana jest jako etykieta (klasa), a pozostałe jako współrzędne wektora.
    
- **Dynamiczny wymiar danych**: Algorytm automatycznie dostosowuje się do liczby cech (wymiarowości) wektorów w pliku.
    
- **Analiza błędu**: Obliczanie i wyświetlanie końcowej wartości błędu oraz liczby iteracji potrzebnych do uzyskania konwergencji.
    

## Struktura projektu

- **Main**: Klasa startowa obsługująca interakcję z użytkownikiem (pobranie parametru $k$ oraz ścieżki do pliku).
    
- **KMeans**: Serce systemu zawierające logikę ładowania danych, obliczania odległości, aktualizacji centroidów oraz pętlę główną algorytmu.
    
- **Vec**: Model danych przechowujący tablicę współrzędnych typu `double` oraz oryginalną etykietę klasy.
    
- **FileNotChosenException**: Dedykowany wyjątek obsługujący sytuację przerwania wyboru pliku przez użytkownika.
    

## Instrukcja uruchomienia

1. **Przygotowanie danych**: Plik wejściowy powinien być formatu `.csv`, gdzie wartości są rozdzielone przecinkami, np.:
    
    
    ```
    5.1,3.5,1.4,0.2,Iris-setosa
    7.0,3.2,4.7,1.4,Iris-versicolor
    ```
    
2. **Kompilacja**:
    
    Bash
    
    ```
    javac *.java
    ```
    
3. **Uruchomienie**:
    
    Bash
    
    ```
    java Main
    ```
    
4. **Obsługa**: Po uruchomieniu wpisz w konsoli liczbę grup ($k$), a następnie wybierz plik `.csv` w oknie dialogowym.
    

## Wymagania

- Java Development Kit (JDK) 8 lub nowszy.
    
- Środowisko graficzne (wymagane do wyświetlenia okna wyboru pliku `Swing`).