/**
 *
 * @author louienicholaslee International School Manila 13-inch
 * MacBook Pro, NetBeans. Purpose: To help facilitate learning of new vocabulary
 * in foreign language.
 *
 * Object record used to store the foreign language word, meaning, example usage
 * and image path name Word is an abstract class because a word cannot be just a
 * word, it must either be a word with pronunciation or a word without
 * pronunciation this is important because the program works with words
 * character based languages which require pronunciation and alphabet based
 * languages which don't.
 * 
 * Mastery Factor: User-defined objects
 */
public abstract class Word {
    //Stores the meaning of the word

    private String word;
    //Stores the word
    private String meaning;
    //Stores example usage of the word in a sentence
    private String example;
    //Stores the name of the image path that illustrates the meaning of the word
    private String imgName;

//-------------------------------Accessor Methods-----------------------------//
    //Mastery Factor: Encapsulation
    public String getWord() {
        return word;
    }

    public void setWord(String s) {
        word = s;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String s) {
        meaning = s;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String s) {
        example = s;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String s) {
        imgName = s;
    }
//----------------------------------------------------------------------------//

    //Overrides the compareTo method to compare the different fields of the word
    //This is done because of homonym words.
    public int compareTo(Word w) {
        int before = -1;
        int equal = 0;
        int after = 1;

        if (this.word.compareToIgnoreCase(w.word) < 0) {
            return before;
        } else if (this.word.compareToIgnoreCase(w.word) > 0) {
            return after;
        } else {
            if (this.meaning.compareToIgnoreCase(w.meaning) < 0) {
                return before;
            } else if (this.meaning.compareToIgnoreCase(w.meaning) > 0) {
                return after;
            } else {
                if (this.imgName.compareToIgnoreCase(w.imgName) < 0) {
                    return before;
                } else if (this.imgName.compareToIgnoreCase(w.imgName) > 0) {
                    return after;
                } else {
                    return equal;
                }
            }
        }
    }

    //Overrides the toString method that objects have. Used to display the Word 
    //object.
    public String toString() {
        return word + "#" + meaning + "#" + example + "#" + imgName;
    }
}

//Mastery Factor: Inheritance
class WordWPron extends Word {
    
    //Stores the pronunciation of the word
    private String pronunciation;

    //Constructor for the WordWPron automatically sets the member fields using 
    //arguments
    WordWPron(String word, String meaning, String ex, String img, String pron) {
        setWord(word);
        setMeaning(meaning);
        setExample(ex);
        setImgName(img);
        setPronunciation(pron);
    }

//-------------------------------Accessor Methods-----------------------------//
    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String s) {
        pronunciation = s;
    }
//----------------------------------------------------------------------------//

    //Mastery Factor: Polymorphism
    //Overrides compareTo method in abstract class adding another comparisson with
    //the pronunciation of the word
    public int compareTo(Word w) {
        int before = -1;
        int equal = 0;
        int after = 1;
        if (super.compareTo(w) == 0) {
            if (getPronunciation().compareToIgnoreCase(((WordWPron) w).getPronunciation()) < 0) {
                return before;
            } else if (getPronunciation().compareToIgnoreCase(((WordWPron) w).getPronunciation()) > 0) {
                return after;
            } else {
                return equal;
            }
        } else {
            return super.compareTo(w);
        }
    }
    
    //Mastery Factor: Polymorphism
    //Overrides toString method in abstract class appending the pronunciation
    public String toString() {
        return super.toString() + "#" + getPronunciation();
    }
}

//Mastery Factor: Inheritance
class WordWOPron extends Word {

    //Constructor for the WordWOPron automatically sets the member fields using 
    //arguments
    WordWOPron(String word, String meaning, String ex, String img) {
        setWord(word);
        setMeaning(meaning);
        setExample(ex);
        setImgName(img);
    }

    //Mastery Factor: Polymorphism
    public int compareTo(WordWOPron w) {
        return super.compareTo(w);
    }

    //Mastery Factor: Polymorphism
    public String toString() {
        return super.toString();
    }
}
