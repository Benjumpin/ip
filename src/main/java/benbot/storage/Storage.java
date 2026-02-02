package benbot.storage;

import benbot.exception.BenBotExceptions;
import benbot.task.Deadline;
import benbot.task.Event;
import benbot.task.Task;
import benbot.task.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 Handling the loading and saving of tasks to the hard disk
 */

public class Storage {
   private final String filePath;

   public Storage(String filePath) {
       this.filePath = filePath;
   }

   public void createDataFileIfNeeded() throws IOException {
       File newFile = new File(filePath);
       File parentDir = newFile.getParentFile();

       if (parentDir != null && !parentDir.exists()) {
           boolean isCreateDir = parentDir.mkdirs();
           if (!isCreateDir) {
               throw new IOException("Can't create directory: " + parentDir.getAbsolutePath());
           }
       }

       if (!newFile.exists()) {
           boolean isCreateFile = newFile.createNewFile();
           if (!isCreateFile) {
               throw new IOException("Can't create file: " + filePath);
           }
       }
   }

   public void saveFile(List<Task> tasks) throws IOException {
       FileWriter fw = new FileWriter(filePath);

       for (Task task : tasks) {
           fw.write(task.changeFileFormat() + System.lineSeparator());
       }
       fw.close();
   }

   public List<Task> loadFile() throws BenBotExceptions {
       List<Task> loadedFiles = new ArrayList<>();
       File file = new File(filePath);

       try {
           Scanner sc = new Scanner(file);
           while (sc.hasNext()) {
               String line = sc.nextLine();
               Task task = fromLineToTask(line);
               loadedFiles.add(task);
           }
       } catch (FileNotFoundException error) {
           // just in case
           throw new BenBotExceptions("File not found: " + error.getMessage());
       }

       return loadedFiles;
   }

   private Task fromLineToTask(String line) throws BenBotExceptions {
       String[] parts = line.split(" \\| ");
       if (parts.length < 3 || parts.length > 5) {
           throw new BenBotExceptions("Corrupted file format: " + line);
       }
       String type = parts[0];
       boolean isComplete = parts[1].equals("1");
       String item = parts[2];
       Task task;

       switch (type) {
           case "T":
               task = new Todo(item);
               break;
           case "D":
               if (parts.length < 4) {
                   throw new BenBotExceptions("Corrupted deadline format: " + line);
               }
               String by = parts[3];
               task = new Deadline(item, by);
               break;
           case "E":
               if (parts.length < 5) {
                   throw new BenBotExceptions("Corrupted event format: " + line);
               }
               String from = parts[3];
               String to = parts[4];
               task = new Event(item, from, to);
               break;
           default:
               throw new BenBotExceptions("Unrecognised type: " + type);
       }
       if (isComplete) {
           task.markDone();
       }

       return task;
   }
}

