import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotChosenException {
        int k = getValOfKFromUser();
        String filePath = getPathOfChosenFile();

        KMeans kMeans = new KMeans(k, filePath);
        kMeans.classify();
    }

    public static String getPathOfChosenFile() throws FileNotChosenException {
        String filePath;
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV file", "csv");
        fileChooser.setFileFilter(filter);

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().getAbsolutePath();
        } else throw new FileNotChosenException("There has to be a chosen file to run the algorithm");

        return filePath;
    }

    public static int getValOfKFromUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter value of k: ");
        return scanner.nextInt();
    }
}
