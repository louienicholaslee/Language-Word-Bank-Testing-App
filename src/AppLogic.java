
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author louienicholaslee International School Manila 13-inch
 * MacBook Pro, NetBeans. Purpose: To help facilitate learning of new vocabulary in
 * foreign languages
 * 
 * AppLogic class controls the logic of between GUI, program logic, and files stored
 * in RAM
 */
public class AppLogic {
    //A static member variable that is constant throughout all instances of
    //the AppLogic. Stores the number of bytes that a title (title of vocabulary
    //sets) record can have
    public static final int TITLE_RECORD_SIZE = 50;
    //Dictates the maximum number of words that can be in a vocabulary set
    public static final int MAX_NUM_WORDS = 20;
    //A static wordTree that stores the words that have not yet been memorized 
    //as a binary search tree.
    public static WordTree wordsNotMemorized = new WordTree();
    //Boolean showing if the current language being worked on is phonetic (ie has
    //pronunciation
    private boolean phoneticLanguage;
    //Shows the current working directory: Chinese, French or Spanish. Changes
    //depending on the language being worked on
    private static String workingDir;

//-------------------------------Accessor Methods-----------------------------// 
    public static String getWorkingDirectory() {
        return workingDir;
    }
    
    public void setWorkingDirectory(String dir) {
        workingDir = dir;
        if (workingDir.equalsIgnoreCase("chinese")) {
            phoneticLanguage = true;
        } else {
            phoneticLanguage = false;
        }
        //creates the Words Not Yet Memorized file if not already done so
        File file = new File(workingDir + "/Words Not Yet Memorized.txt");
        try {
            file.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //clears the current wordTree
        wordsNotMemorized.clear();
        //populates wordTree with words not yet memorized from the chosen language
        wordsNotMemorized.populate(workingDir);
    }

    public boolean isPhoneticLanguage() {
        return phoneticLanguage;
    }
//----------------------------------------------------------------------------//    

    //Mastery Factor: Deleting RandomAccessFile using seek method
    //Deletes a title from the saved titles file by direct manipulation of file,
    //using the index number of the title that will be deleted
    public void deleteTitle(int index) {
        try {
            RandomAccessFile raf = new RandomAccessFile(workingDir + "/Word Sets Titles.txt", "rw");
            int numTitlesToMove = (int) (raf.length() - (index + 1) * TITLE_RECORD_SIZE) / TITLE_RECORD_SIZE;
            int posTitleToDelete = index * TITLE_RECORD_SIZE;
            long newRAFLength = raf.length() - TITLE_RECORD_SIZE;
            if (numTitlesToMove != 0) {
                raf.seek(posTitleToDelete + TITLE_RECORD_SIZE);
                String titlesToRewrite = raf.readLine();
                raf.seek(posTitleToDelete);
                raf.writeBytes(titlesToRewrite);
                raf.setLength(newRAFLength);
            } else {
                raf.setLength(newRAFLength);
            }
            raf.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Edits the file directly via RandomAccessFile with parameters of index and
    //title that will be changed to 
    //Mastery Factor: User-defined methods
    public void editTitle(int index, String title) {
        try {
            RandomAccessFile raf = new RandomAccessFile(workingDir + "/Word Sets Titles.txt", "rw");
            int posTitleToEdit = index * TITLE_RECORD_SIZE;
            raf.seek(posTitleToEdit);
            raf.writeBytes(title);
            for (int i = 0; i < (TITLE_RECORD_SIZE - title.length()); i++) {
                raf.writeBytes(" ");
            }
            raf.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Mastery Factor: Adding RandomAccessFile using seek method
    //Adds the tile title to the file
    public void writeTitle(String title) {
        try {
            RandomAccessFile raf = new RandomAccessFile(workingDir + "/Word Sets Titles.txt", "rw");
            try {
                if (raf.length() == 0) {
                    raf.writeBytes(title);
                } else {
                    raf.seek(raf.length() + ((TITLE_RECORD_SIZE - raf.length()) % TITLE_RECORD_SIZE));
                    raf.writeBytes(title);
                }
                for (int i = 0; i < (TITLE_RECORD_SIZE - title.length()); i++) {
                    raf.writeBytes(" ");
                }
                raf.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    //Reads the titles from the file and returns a string array of the titles
    //Mastery Factor: loops
    public String[] readTitles() {
        String[] titles = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(workingDir + "/Word Sets Titles.txt", "rw");
            int numTitles = (int) (raf.length() / TITLE_RECORD_SIZE);
            if (wordsNotMemorized.isEmpty()) {
                titles = new String[numTitles];
            } else {
                titles = new String[numTitles + 1];
                titles[numTitles] = "Words Not Yet Memorized";
            }
            for (int i = 0; i < numTitles; i++) {
                byte[] titleCharArray = new byte[TITLE_RECORD_SIZE];
                try {
                    for (int j = 0; j < TITLE_RECORD_SIZE; j++) {
                        titleCharArray[j] = raf.readByte();
                    }
                } catch (EOFException e) {
                    e.printStackTrace();
                }
                titles[i] = new String(titleCharArray).trim();
            }
            raf.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return titles;
    }

    //Writes/rewrites the whole vocabulary set to the proper txt file using title
    //parameter and wordArray
    public void saveWordSet(Word[] wordArray, String title) {
        BufferedWriter out = null;
        try {
            int fileLength = wordArray.length;
            try {
                //first empties the file so that when written, the is no leftover
                //entries that were written before
                deleteSet(title);
                //UTF-8 allows writing of chinese and accent based characters
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(workingDir + "/" + title + ".txt"), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            try {
                for (int i = 0; i < fileLength; i++) {
                    out.write(wordArray[i].toString() + "\n");
                }
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    //returns the counts the number of words in the set by counting the number of 
    //lines, returns this as int
    //Mastery Factor: loop
    public static int numWordsSet(String title) {
        BufferedReader reader = null;
        int lines = 0;
        try {
            reader = new BufferedReader(new FileReader(workingDir + "/" + title + ".txt"));
            while (reader.readLine() != null) {
                lines++;
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lines;
    }

    //reads the whole vocabulary set from memory using the title parameter, returning
    //a word Array
    public Word[] readWordSet(String title) {
        Word[] wordArray = new Word[numWordsSet(title)];
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(workingDir + "/" + title + ".txt"));
            String line;
            String word;
            String meaning;
            String example;
            String imgName;
            String pronunciation;
            String[] wordComponents = null;
            int counter = 0;
            while (true) {
                line = in.readLine();
                if (line == null) {
                    break;
                }
                wordComponents = line.split("#");
                word = wordComponents[0];
                meaning = wordComponents[1];
                example = wordComponents[2];
                imgName = wordComponents[3];
                //Distinguishes between phonetic and non-phonetic languages
                if (phoneticLanguage) {
                    pronunciation = wordComponents[4];
                    wordArray[counter++] = new WordWPron(word, meaning, example, imgName, pronunciation);
                } else {
                    wordArray[counter++] = new WordWOPron(word, meaning, example, imgName);
                }
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return wordArray;
    }

    //Empties the title.txt file
    public static void deleteSet(String title) {
        File file = new File(workingDir + "/" + title + ".txt");
    }
}