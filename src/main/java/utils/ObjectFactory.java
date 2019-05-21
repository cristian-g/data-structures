package utils;

import datastructures.LinkedList.LinkedList;
import models.Hashtag;
import models.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ObjectFactory {
    public static String[] computeSpanishWords() {

        BufferedReader br = null;
        LinkedList<String> words = new LinkedList<>();
        int i = 0;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("datasets/assets/palabras_es.txt"));

            while ((sCurrentLine = br.readLine()) != null) {
                words.add(sCurrentLine);
                i++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return words.toArray(new String[words.getSize()]);
    }

    public static User[] computeUsersWithRandomUsername(int quantity) {

        String[] usernames = ObjectFactory.computeSpanishWords();

        User[] users = new User[usernames.length];
        for (int j = 0; j < users.length; j++) {
            users[j] = new User(usernames[j]);
        }

        return users;
    }

    public static Hashtag[] computeHashtagsWithRandomNames(int quantity) {

        String[] usernames = ObjectFactory.computeSpanishWords();

        Hashtag[] hashtags = new Hashtag[usernames.length];
        for (int j = 0; j < hashtags.length; j++) {
            hashtags[j] = new Hashtag(usernames[j]);
        }

        return hashtags;
    }
}
