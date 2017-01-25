/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author "Louie Nicholas Lee International School Manila"
 * Program: Language Helper
 * Computer: 13-inch MacBook Pro, IDE: NetBeans
 * Purpose: To help facilitate learning of new vocabulary in foreign languages
 * Starts the program
 */

public class Main {
    public static void main (String[] args){
        AppLogic al = new AppLogic();
        StartScreenForm start = new StartScreenForm(al);
        start.setVisible(true);
    }
}
