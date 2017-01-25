
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author louienicholaslee International School Manila 13-inch
 * MacBook Pro, NetBeans. Purpose: To help facilitate learning of new vocabulary in
 * foreign languages
 * 
 * NodeBinTree-a Binary Search Tree Node
 * Mastery Factor: ADT Tree
 */
public class NodeBinTree {
    //Stores the word as a key
    private Word word;
    private NodeBinTree left;
    private NodeBinTree right;

    //Constructor for a NodBinTree class
    NodeBinTree(Word w) {
        word = w;
        right = null;
        left = null;
    }
    
//-------------------------------Accessor Methods-----------------------------// 
    public Word getWord() {
        return word;
    }

    public void setWord(Word w) {
        word = w;
    }

    public NodeBinTree getLeft() {
        return left;
    }

    public NodeBinTree getRight() {
        return right;
    }

    public void setLeft(NodeBinTree l) {
        left = l;
    }

    public void setRight(NodeBinTree r) {
        right = r;
    }
//----------------------------------------------------------------------------//    

    //Mastery Factor: Recursion
    //Recursive add function which takes in a word parameter
    public void add(Word w) {
        if (w.compareTo(word) < 0) {
            if (left == null) {
                left = new NodeBinTree(w);
            } else {
                left.add(w);
            }
        } else {
            if (right == null) {
                right = new NodeBinTree(w);
            } else {
                right.add(w);
            }
        }
    }
    
    //Mastery Factor: Recursion
    //Recursive search function used to see whether a word has been memorized yet
    //or not. Returns null if the word memorized
    public Word find(Word w) {
        Word result = null;
        if (w.compareTo(word) == 0) {
            result = word;
        } else {
            if (w.compareTo(word) < 0) {
                if (left != null) {
                    result = left.find(w);
                }
            } else {
                if (right != null) {
                    result = right.find(w);
                }
            }
        }
        return result;
    }

    //Mastery Factor: Recursion
    //Recursive delete function, deletes the word and corrects the tree. Returns
    //the pointer to the reconstructed portion of the tree
    public NodeBinTree remove(Word w){
        NodeBinTree result = this;
        if (w.compareTo(word) == 0) {
            if (left == null && right == null) {
                result = null;
            } else if (left != null && right == null) {
                result = left;
            } else if (left == null && right != null) {
                result = right;
            } else {
                //getSuccessor is used to find the closest word that comes after
                //the current word.
                result = getSuccessor();
                result.left = left;
                result.right = right;
            }
        } else {
            if (w.compareTo(word) < 0) {
                if (left != null) {
                    left = left.remove(w);
                }
            } else {
                if (right != null) {
                    right = right.remove(w);
                }
            }
        }
        return result;
    }

    //Mastery Factor: Recursion
    //Recursive functions which searches for the next closest word that comes after
    //the current word. Also removes the word that was found and returns it.
    public NodeBinTree getSuccessor() {
        NodeBinTree successor = right;
        while (successor.left != null) {
            successor = successor.left;
        }
        right.remove(successor.word);
        return successor;
    }
    
    //Saves the word tree in memory via pre-order
    public void saveWordTree(){
        BufferedWriter out = null;
        try {
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("Words Not Yet Memorized.txt"), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            try {
                out.write(this.word.toString()+"\n");
                if(left!=null){
                    left.saveWordTree();
                }
                if(right!= null){
                    right.saveWordTree();
                }
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
