package ee.tkasekamp.vickywaranalyzer.util;

import java.io.*;

/**
 * Handles file and directory operations related to the Victoria II game,
 * such as locating save game and installation directories.
 */
public class FolderHandler {
    // Constants for directory paths that are commonly used
    private static final String PROGRAM_FILES_X86 = "C:/Program Files (x86)";
    private static final String PROGRAM_FILES = "C:/Program Files";
    private static final String STEAM_PATH = "/Steam/steamapps/common/";
    private static final String PARADOX_FOLDER = "Paradox Interactive/";
    // List of possible game versions to check for installation directories
    private static final String[] VERSIONS = {
        "Victoria II - A Heart of Darkness",
        "Victoria 2 A House Divided",
        "Victoria 2"
    };

    // File name for storing user-defined paths to avoid reconfiguration
    private static final String PATHS = "./paths.txt";

    /**
     * Retrieves the saved game and installation folders from a file,
     * or determines the default directories if not previously saved.
     * 
     * @return Array of strings containing save game and installation paths.
     * @throws IOException if reading from the file fails.
     */
    public static String[] getFolders() throws IOException {
        String[] paths = new String[2];
        File pathFile = new File(PATHS);
        if (pathFile.exists()) {
            paths = readPaths();
        } else {
            paths[0] = checkSaveGameFolder();
            paths[1] = checkInstallFolder();
        }
        return paths;
    }

    /**
     * Constructs a default path for the save games based on the system's user directory.
     * 
     * @return The path to the save game folder or an empty string if the folder doesn't exist.
     */
    private static String checkSaveGameFolder() {
        String user = System.getProperty("user.name");
        String saveGameFolder = "C:/Users/" + user + "/Documents/Paradox Interactive/Victoria II/save games/";
        File folder = new File(saveGameFolder);
        return folder.exists() ? saveGameFolder : "";
    }

    /**
     * Checks various common installation directories for the game.
     * 
     * @return The installation directory if found, otherwise an empty string.
     */
    private static String checkInstallFolder() {
        for (String version : VERSIONS) {
            String[] paths = {
                PROGRAM_FILES + PARADOX_FOLDER + version,
                PROGRAM_FILES + STEAM_PATH + version,
                PROGRAM_FILES_X86 + STEAM_PATH + version,
                PROGRAM_FILES_X86 + PARADOX_FOLDER + version
            };
            for (String path : paths) {
                if (new File(path).exists()) {
                    return path;
                }
            }
        }
        return "";
    }

    /**
     * Extracts the directory path from a full file path.
     * 
     * @param path The full path to a file.
     * @return The directory part of the path.
     */
    public static String getDirectoryOnly(String path) {
        int lastIndex = path.lastIndexOf("/");
        return lastIndex > -1 ? path.substring(0, lastIndex + 1) : path;
    }

    /**
     * Saves the specified paths to a text file to retain user settings.
     * 
     * @param saveGameFolder Path to the save game folder.
     * @param installFolder Path to the installation folder.
     * @throws IOException if there is an issue saving the paths to the file.
     */
    public static void savePaths(String saveGameFolder, String installFolder) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PATHS), "UTF-8"))) {
            out.write(saveGameFolder);
            out.newLine();
            out.write(installFolder);
        } catch (IOException e) {
            throw new IOException("Could not save the paths.txt.", e);
        }
    }

    /**
     * Reads the save game and installation paths from a text file.
     * 
     * @return An array with the save game path as the first element and the installation path as the second.
     * @throws IOException if reading from the file fails.
     */
    private static String[] readPaths() throws IOException {
        String[] paths = new String[2];
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATHS), "UTF-8"))) {
            paths[0] = reader.readLine();  // Save game path
            paths[1] = reader.readLine();  // Install path
        }
        return paths;
    }
}