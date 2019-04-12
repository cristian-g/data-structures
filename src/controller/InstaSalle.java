package controller;

import com.google.gson.JsonIOException;
import models.Post;
import models.User;
import utils.JsonReader;

import java.io.FileNotFoundException;
import java.util.*;

public class InstaSalle {
    private Scanner kb;
    private User[] users;
    private Post[] posts;

    public InstaSalle() throws JsonIOException, FileNotFoundException {
        users = JsonReader.parseUsers();
        posts = JsonReader.parsePosts();
    }

    public void showFunctionalitiesMenu() {
        System.out.println("---------- InstaSalle ------------");
        System.out.println("0. Print read data of JSON");
        System.out.println("1. Import files");
        System.out.println("2. Export files");
        System.out.println("Select an option from the menu:");
    }

    /**
     * Start one of the application options.
     *
     * @param functionalityOption The selected functionality.
     */
    private void handleOption(int functionalityOption) {

        switch (functionalityOption) {

            case 0:

                for (User user: this.users) {
                    System.out.println(user);
                }

                for (Post post: this.posts) {
                    System.out.println(post);
                }

                break;

            case 1:
                break;

            case 2:
                break;

            default:
                break;
        }
    }

    /**
     * This method allows toId enter options toId execute.
     */
    public void init() {
        int functionalityOption;
        boolean exit = false;
        this.kb = new Scanner(System.in);

        showFunctionalitiesMenu();

        try {
            functionalityOption = Integer.parseInt(kb.nextLine());
            this.handleOption(functionalityOption);
        } catch (NumberFormatException e) {
            System.out.println("Wrong option.\n");
        }

        kb.close();
    }
}
