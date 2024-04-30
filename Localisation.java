package ee.tkasekamp.vickywaranalyzer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import ee.tkasekamp.vickywaranalyzer.core.Country;

/**
 * Handles the loading and parsing of localization data from CSV files for countries.
 */
public class Localisation {

    private static final String CHARSET_NAME = "ISO8859_1"; // Define charset as a constant

    /**
     * Reads localization files from the specified installation path and updates countries with official names.
     * @param installPath Path where localization files are stored.
     * @param countryTreeMap Map of country tags to Country objects to update.
     */
    public static void readLocalisation(String installPath, TreeMap<String, Country> countryTreeMap) {
        try {
            List<String> localisationFiles = getLocalisationFiles(installPath);
            for (String filename : localisationFiles) {
                readCSV(installPath + "/localisation/" + filename, countryTreeMap);
            }
        } catch (IOException e) {
            System.err.println("Error reading localisation files: " + e.getMessage());
            // Consider a more sophisticated logging approach or rethrowing custom exceptions
        }
    }

    /**
     * Parses a CSV file to update the official names of countries in the map.
     * @param filename Full path to the CSV file.
     * @param countryTreeMap Map where country tags and their corresponding Country objects are stored.
     * @throws IOException If an error occurs during file reading.
     */
    private static void readCSV(String filename, TreeMap<String, Country> countryTreeMap) throws IOException {
        try (BufferedReader scanner = new BufferedReader(new InputStreamReader(new FileInputStream(filename), CHARSET_NAME))) {
            String line;
            while ((line = scanner.readLine()) != null) {
                String[] dataArray = line.split(";");
                if (dataArray.length > 1 && countryTreeMap.containsKey(dataArray[0])) {
                    countryTreeMap.get(dataArray[0]).setOfficialName(dataArray[1]);
                }
            }
        } // Automatically closes the scanner
    }

    /**
     * Retrieves a list of all CSV files from the specified directory.
     * @param path Directory to search for CSV files.
     * @return List of file names with .csv extension.
     * @throws IOException If an error occurs accessing the directory.
     */
    private static List<String> getLocalisationFiles(String path) throws IOException {
        List<String> locList = new ArrayList<>();
        File folder = new File(path + "/localisation");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                    locList.add(file.getName());
                }
            }
        } else {
            throw new IOException("Failed to list files in directory: " + path);
        }

        return locList;
    }
}
