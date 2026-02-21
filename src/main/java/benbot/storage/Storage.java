package benbot.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import benbot.exception.BenBotException;
import benbot.task.Deadline;
import benbot.task.Event;
import benbot.task.Task;
import benbot.task.Todo;

/**
 * Handles the loading and saving of tasks to the hard disk.
 * This class manages file and directory creation, reading from the data file,
 * and writing task data back to storage.
 */
public class Storage {
    private final String filePath;
    
    /**
     * Constructs a Storage object with a specified file path.
     * @param filePath The relative or absolute path to the data file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * Creates the data file and any necessary parent directories if they do not exist.
     *
     * @throws IOException If the directory or file cannot be created.
     */
    public void createDataFileIfNeeded() throws IOException {
        File newFile = new File(filePath);
        File parentDir = newFile.getParentFile();
    
        if (parentDir != null && !parentDir.exists()) {
            boolean isDirCreated = parentDir.mkdirs();
            if (!isDirCreated) {
                throw new IOException("Can't create directory: " + parentDir.getAbsolutePath());
            }
        }
    
        if (!newFile.exists()) {
            boolean isFileCreated = newFile.createNewFile();
            if (!isFileCreated) {
                throw new IOException("Can't create file: " + filePath);
            }
        }
    }
    
    /**
     * Saves the current list of tasks to the hard disk.
     *
     * @param tasks The list of tasks to be written to the file.
     * @throws IOException If an error occurs during the writing process.
     */
    public void saveFile(List<Task> tasks) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
    
        for (Task task : tasks) {
            fileWriter.write(task.changeFileFormat() + System.lineSeparator());
        }
       
        fileWriter.close();
    }
    
    /**
     * Loads the tasks from the data file on the hard disk.
     *
     * @return A list of tasks parsed from the file.
     * @throws BenBotException If the file is not found or contains corrupted data.
     */
    public List<Task> loadFile() throws BenBotException {
        List<Task> loadedFiles = new ArrayList<>();
        File file = new File(filePath);
    
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                Task task = fromLineToTask(line);
                loadedFiles.add(task);
            }
        } catch (FileNotFoundException error) {
            throw new BenBotException("File not found: " + error.getMessage());
        }
        
        return loadedFiles;
    }

    /**
     * Parses a single line from the data file into a Task object.
     *
     * @param line The raw string line from the data file.
     * @return The corresponding Task object (Todo, Deadline, or Event).
     * @throws BenBotException If the line format is unrecognized or corrupted.
     */
    private Task fromLineToTask(String line) throws BenBotException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3 || parts.length > 5) {
            throw new BenBotException("Corrupted file format: " + line);
        }
       
        String type = parts[0];
        boolean isComplete = parts[1].equals("1");
        Task task = createSpecificTask(type, parts, line);
       
        if (isComplete) {
            task.markDone();
        }
        return task;
    }
    
    private Task createSpecificTask(String type, String[] parts, String originalLine) throws BenBotException {
        switch (type) {
        case "T":
            return new Todo(parts[2]);
        case "D":
            return parseDeadline(parts, originalLine);
        case "E":
            return parseEvent(parts, originalLine);
        default:
            throw new BenBotException("Unrecognised type: " + type);
        }
    }
    
    private Deadline parseDeadline(String[] parts, String line) throws BenBotException {
        if (parts.length < 4) { // Guard Clause
            throw new BenBotException("Corrupted deadline format: " + line);
        }
        return new Deadline(parts[2], parts[3]);
    }
    
    private Event parseEvent(String[] parts, String line) throws BenBotException {
        if (parts.length < 5) { // Guard Clause
            throw new BenBotException("Corrupted event format: " + line);
        }
        return new Event(parts[2], parts[3], parts[4]);
    }   
}

