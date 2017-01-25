
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author louienicholaslee International School Manila 13-inch
 * MacBook Pro, NetBeans. Purpose: To help facilitate learning of new vocabulary
 * in foreign languages
 *
 * BinarySearchTree used to store temporarily the words not yet memorized.
 * Mastery Factor: ADT Tree
 */
public class WordTree {

    private NodeBinTree root = null;

    //Accessor method (root cannot be changed by outside classes unless via the 
    //clear method)
    public NodeBinTree getRoot() {
        return root;
    }

    //Checks to see if the WordTree is empty
    public boolean isEmpty() {
        if (root == null) {
            return true;
        } else {
            return false;
        }
    }

    //Add method with the appropriate check to make sure that if the tree is empty
    //it adds to the root. Also checks for duplicate adding. Primarily used to
    //populate the binary tree
    public void add(Word w) {
        if (root == null) {
            root = new NodeBinTree(w);
        } else {
            if (!find(w)) {
                root.add(w);
            }
        }
    }

    //Add method with the appropriate check to make sure that if the tree is empty
    //it adds to the root. Also checks for duplicate adding. Primarily used to
    //add words that have not yet been memorized to the word tree and directly
    //writes the words to file
    public void insert(Word w) {
        if (root == null) {
            root = new NodeBinTree(w);
        } else {
            if (!find(w)) {
                root.add(w);
            }
        }
        //Writes the word to file
        BufferedWriter out = null;
        try {
            try {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        AppLogic.getWorkingDirectory() + "/Words Not Yet Memorized.txt", true), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            try {
                out.write(w.toString() + "\n");
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    //Removes the word from the binary tree
    public void remove(Word w) {
        if (!isEmpty()) {
            root = root.remove(w);
        }
        //Rewrites the word list to file
        BufferedWriter out = null;
        BufferedReader in = null;
        String currentLine;
        String[] wordComponents;
        File tempFile = new File(AppLogic.getWorkingDirectory() + "/tmp.txt");
        File inputFile = new File(AppLogic.getWorkingDirectory() + "/Words Not Yet Memorized.txt");
        try {
            try {
                //Creates a temporary file to write all the words that should appear
                //in the file
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        tempFile, true), "UTF-8"));
                //Reads all the words in the file
                in = new BufferedReader(new FileReader(
                        inputFile));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            try {
                while ((currentLine = in.readLine()) != null) {
                    wordComponents = currentLine.split("#");
                    if (wordComponents[0].trim().equalsIgnoreCase(w.getWord())
                            && wordComponents[1].trim().equalsIgnoreCase(w.getMeaning())
                            && wordComponents[2].trim().equalsIgnoreCase(w.getExample())
                            && wordComponents[3].trim().equalsIgnoreCase(w.getImgName())) {
                        continue;
                    }
                    out.write(currentLine+"\n");
                }
                in.close();
                out.close();
                tempFile.renameTo(inputFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    //Clears the binary tree so that the whole tree can be rewritten again
    public void clear() {
        root = null;
    }

    //Populates the binary tree from the appropriate language using the Working 
    //directory
    //Mastery Factor: Parsing a file
    public void populate(String workingDir) {
        boolean phoneticLanguage = false;
        if (workingDir.equalsIgnoreCase("Chinese")) {
            phoneticLanguage = true;
        } else {
            phoneticLanguage = false;
        }
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(workingDir + "/Words Not Yet Memorized.txt"));
            String line;
            String word;
            String meaning;
            String example;
            String imgName;
            String pronunciation;
            String[] wordComponents = null;
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
                if (phoneticLanguage) {
                    pronunciation = wordComponents[4];
                    add(new WordWPron(word, meaning, example, imgName, pronunciation));
                } else {
                    add(new WordWOPron(word, meaning, example, imgName));
                }
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Searches whether the word is in the binary tree or not. Returs true if found
    //false if not
    public boolean find(Word w) {
        if (isEmpty()) {
            return false;
        } else {
            if (root.find(w) != null) {
                return true;
            } else {
                return false;
            }
        }
    }

    //Saves the word tree to memory
    public void saveWordTree() {
        root.saveWordTree();
    }
}
