package vn.edu.hcmus.fit.sv18120113.BT4;

import java.util.Scanner;

public class App {
    public void run() {
        Dictionary dictionaryENVI = null;
        Dictionary dictionaryVIEN = null;

        Scanner scanner = new Scanner(System.in);

        while (true) {
            Utils.println();
            Utils.println();
            Utils.println("-- DICTIONARY APP --");
            Utils.println("@  Name   : Nguyen Chanh Dai");
            Utils.println("@  ID     : 18120113");
            Utils.println("@  Email  : 18120113@student.hcmus.edu.vn");
            Utils.println();

            Utils.println("1. English - Vietnamese");
            Utils.println("2. Vietnamese - English");
            Utils.println("3. Exit");
            Utils.println();

            String optionKey;

            do {
                Utils.print(">> Enter 1 - 3 : ");
                optionKey = scanner.nextLine();
            } while (optionKey.compareTo("1") < 0 || optionKey.compareTo("3") > 0);

            Utils.println();

            // 1. English - Vietnamese
            if (optionKey.equals("1")) {
                if (dictionaryENVI == null) {
                    Utils.println("> Loading [English - Vietnamese] ...");
                    Utils.println();
                    dictionaryENVI = new Dictionary(
                            "EN_VI",
                            "English - Vietnamese",
                            "en_vi.xml",
                            "my_en_vi.xml",
                            "fav_en_vi.xml",
                            "search_log.txt"
                    );
                }

                dictionaryENVI.run();
            }

            // 2. Vietnamese - English
            if (optionKey.equals("2")) {
                if (dictionaryVIEN == null) {
                    Utils.println("> Loading [Vietnamese - English] ...");
                    Utils.println();
                    dictionaryVIEN = new Dictionary(
                            "VI_EN",
                            "Vietnamese - English",
                            "vi_en.xml",
                            "my_vi_en.xml",
                            "fav_vi_en.xml",
                            "search_log.txt"
                    );
                }

                dictionaryVIEN.run();
            }

            // 3. Exit
            if (optionKey.equals("3")) {
                Utils.println("[i] Thank you for using this App ^^!");
                scanner.close();
                break;
            }
        }
    }
}
