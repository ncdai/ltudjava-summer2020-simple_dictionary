package vn.edu.hcmus.fit.sv18120113.BT4;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Dictionary {
    // Language
    String LANGUAGE_KEY;
    String LANGUAGE_LABEL;

    // Files
    String DEFAULT_DICTIONARY_FILE;
    String MY_DICTIONARY_FILE;
    String FAVORITE_DICTIONARY_FILE;
    String LOG_FILE;

    // Default Dictionary
    private Map<String, Vocabulary> dictionary = new HashMap<>();

    // My Dictionary
    private Map<String, Vocabulary> myDictionary = new HashMap<>();

    // My Favorite
    private Map<String, Vocabulary> favDictionary = new HashMap<>();

    // Search Log
    LogList logger = null;

    Dictionary (String LANGUAGE_KEY, String LANGUAGE_LABEL, String DEFAULT_FILE, String MY_FILE, String FAV_FILE, String LOG_FILE) {
        this.LANGUAGE_KEY = LANGUAGE_KEY;
        this.LANGUAGE_LABEL = LANGUAGE_LABEL;
        this.DEFAULT_DICTIONARY_FILE = DEFAULT_FILE;
        this.MY_DICTIONARY_FILE = MY_FILE;
        this.FAVORITE_DICTIONARY_FILE = FAV_FILE;
        this.LOG_FILE = LOG_FILE;

        this.logger = new LogList(LOG_FILE);
        this.syncDictionaryFromXML();
        this.syncMyDictionaryFromXML();
        this.syncFavDictionaryFromXML();
    }

    void syncFavDictionaryFromXML() {
        Map<String, Vocabulary> data = XMLReaderWriter.readDictionaryFromXML(this.FAVORITE_DICTIONARY_FILE);
        if (data == null) return;

        this.favDictionary = data;
    }

    void syncFavDictionaryToXML() {
        boolean success = XMLReaderWriter.writeDictionaryToXML(this.FAVORITE_DICTIONARY_FILE, this.favDictionary);
        if (!success) {
            Utils.println("[!] Write file [" + this.FAVORITE_DICTIONARY_FILE + "] failed");
        }
    }

    void syncDictionaryFromXML() {
        Map<String, Vocabulary> data = XMLReaderWriter.readDictionaryFromXML(this.DEFAULT_DICTIONARY_FILE);
        if (data == null) return;

        this.dictionary = data;
    }

    void syncMyDictionaryFromXML() {
        Map<String, Vocabulary> data = XMLReaderWriter.readDictionaryFromXML(this.MY_DICTIONARY_FILE);
        if (data == null) return;

        this.myDictionary = data;

        Set<String> keys = data.keySet();
        for (String key : keys) {
            this.dictionary.put(key, data.get(key));
        }

    }

    void syncMyDictionaryToXML() {
        boolean success = XMLReaderWriter.writeDictionaryToXML(this.MY_DICTIONARY_FILE, this.myDictionary);
        if (!success) {
            Utils.println("[!] Write file [" + this.MY_DICTIONARY_FILE + "] failed");
        }
    }

    void searchVocabulary() {
        Utils.println("[SEARCH VOCABULARY > " + this.LANGUAGE_LABEL + "]");

        Scanner scanner = new Scanner(System.in);
        Utils.print(">> Enter word : ");
        String keyword = scanner.nextLine();

        String searchKey = Utils.slugify(keyword);
        Vocabulary res = this.dictionary.get(searchKey);

        Utils.println();

        if (res != null) {
            res.printWithHeader();

            this.logger.writeLog(keyword);
            return;
        }

        System.out.println("[!] No vocabulary found");
    }

    void addVocabulary() {
        Scanner scanner = new Scanner(System.in);

        Utils.println("[ADD VOCABULARY > " + this.LANGUAGE_LABEL + "]");
        Utils.print(">> Enter word : ");
        String vocabulary = scanner.nextLine();
        String vocabularyKey = Utils.slugify(vocabulary);

        Utils.print(">> Enter meaning : ");
        String meaning = scanner.nextLine();
        String newMeaning = "@" + vocabulary + "\n- " + meaning;

        Vocabulary newVocabulary = new Vocabulary(vocabulary, newMeaning);
        this.dictionary.put(vocabularyKey, newVocabulary);
        this.myDictionary.put(vocabularyKey, newVocabulary);
        this.syncMyDictionaryToXML();

        Utils.println();
        Utils.println("[i] Added @" + vocabulary + " to [" + this.LANGUAGE_LABEL + "]!");
    }

    void removeVocabulary() {
        Scanner scanner = new Scanner(System.in);

        Utils.println("[REMOVE VOCABULARY > " + this.LANGUAGE_LABEL + "]");
        Utils.print(">> Enter word : ");
        String word = scanner.nextLine();
        String vocabularyKey = Utils.slugify(word);

        Utils.println();

        Vocabulary res = this.dictionary.remove(vocabularyKey);

        if (res != null) {
            this.myDictionary.remove(vocabularyKey);
            this.syncMyDictionaryToXML();

            Utils.println("[i] @" + word + " has been removed from the [" + this.LANGUAGE_LABEL + "]!");
        } else {
            Utils.println("[!] @" + word + " does not exists!");
        }
    }

    void addFavoriteVocabulary() {
        Utils.println("[FAVORITE VOCABULARY > ADD > From " + this.LANGUAGE_LABEL + "]");

        Utils.print(">> Enter word : ");
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        String vocabularyKey = Utils.slugify(word);

        Utils.println();

        Vocabulary vocabulary = this.dictionary.get(vocabularyKey);

        if (vocabulary != null) {
            this.favDictionary.put(vocabularyKey, vocabulary);
            this.syncFavDictionaryToXML();

            Utils.println("[i] Added @" + word + " to [Favorite]!");
        } else {
            Utils.println("[!] @" + word + " does not exists!");
        }
    }

    void viewFavoriteVocabulary() {
        Utils.println("[FAVORITE VOCABULARY > VIEW ALL]");

        Utils.println("[?] Which order do you want to see the list ?");
        Utils.println("1. From A to Z");
        Utils.println("2. From Z to A");

        Scanner scanner = new Scanner(System.in);
        String optionKey;

        do {
            Utils.print(">> Enter 1 or 2 : ");
            optionKey = scanner.nextLine();
        } while (!optionKey.equals("1") && !optionKey.equals("2"));

        Utils.println();

        Map<String, Vocabulary> sorted = new HashMap<>();

        if (optionKey.equals("1")) {
            Utils.println("[FAVORITE VOCABULARY > VIEW ALL > From A to Z]");
            Vocabulary.printHeader();

            sorted = this.favDictionary
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(
                            Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (e1, e2) -> e2, LinkedHashMap::new
                            )
                    );
        }

        if (optionKey.equals("2")) {
            Utils.println("[FAVORITE VOCABULARY > VIEW ALL > From Z to A]");
            Vocabulary.printHeader();

            sorted = this.favDictionary
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                    .collect(
                            Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (e1, e2) -> e2, LinkedHashMap::new
                            )
                    );
        }

        Set<String> keys = sorted.keySet();
        for (String key : keys) {
            Utils.println();
            this.favDictionary.get(key).print();
        }
    }

    void removeFavoriteVocabulary() {
        Utils.println("[REMOVE FAVORITE VOCABULARY]");

        Utils.print(">> Enter word : ");
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        String vocabularyKey = Utils.slugify(word);

        this.favDictionary.remove(vocabularyKey);
        this.syncFavDictionaryToXML();

        Utils.println("[i] @" + word + " has been removed from [Favorite]!");
    }

    boolean parseDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException dpe) {
            return false;
        }
    }

    void viewSearchLog() {
        Utils.println("[SEARCH LOG]");

        Scanner scanner = new Scanner(System.in);

        String strFromDate, strToDate;
        do {
            Utils.print(">> From Date (YYYY-MM-DD) : ");
            strFromDate = scanner.nextLine();

            Utils.print(">> To Date (YYYY-MM-DD) : ");
            strToDate = scanner.nextLine();
        } while (!this.parseDate(strFromDate) || !this.parseDate(strToDate));

        LocalDate fromDate = LocalDate.parse(strFromDate);
        LocalDate toDate = LocalDate.parse(strToDate);

        Utils.println();

        if (fromDate.compareTo(toDate) > 0) {
            Utils.println("[!] Time Period is not valid!");
            return;
        }

        Utils.println("[SEARCH LOG > " + fromDate.toString() + " -> " + toDate.toString() + "]");
        this.logger.printLogByTimePeriod(fromDate, toDate);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String optionKey;

        do {
            Utils.println("[" + this.LANGUAGE_LABEL + "]");
            Utils.println();

            Utils.println("1. Search");
            Utils.println("2. Add / Remove Vocabulary");
            Utils.println("3. Favorite Vocabulary");
            Utils.println("4. Search Log");
            Utils.println("5. Change Dictionary / Back");

            Utils.println();
            Utils.println("[?] Which feature would you like to use ?");

            do {
                Utils.print(">> Enter 1 - 5 : ");
                optionKey = scanner.nextLine();
            } while (optionKey.compareTo("1") < 0 || optionKey.compareTo("6") > 0);

            Utils.println();

            switch (optionKey) {
                // 1. Search
                case "1":
                    do {
                        this.searchVocabulary();

                        Utils.println();
                        Utils.println("[?] Do you want to continue using [SEARCH VOCABULARY] ?");
                        Utils.print(">> Enter (Y)/N : ");
                        optionKey = scanner.nextLine();

                        Utils.println();
                    } while (!optionKey.toUpperCase().equals("N"));

                    break;

                // 2. Add / Remove Vocabulary
                case "2":
                    do {
                        Utils.println("[ADD / REMOVE VOCABULARY]");
                        Utils.println("1. Add");
                        Utils.println("2. Remove");

                        do {
                            Utils.print(">> Enter 1 or 2 : ");
                            optionKey = scanner.nextLine();
                        } while (optionKey.compareTo("1") < 0 || optionKey.compareTo("2") > 0);

                        Utils.println();

                        if (optionKey.equals("1")) {
                            this.addVocabulary();
                        }

                        if (optionKey.equals("2")) {
                            this.removeVocabulary();
                        }

                        Utils.println();
                        Utils.println("[?] Do you want to continue using [ADD / REMOVE VOCABULARY] ?");
                        Utils.print(">> Enter (Y)/N : ");
                        optionKey = scanner.nextLine();

                        Utils.println();
                    } while (!optionKey.toUpperCase().equals("N"));

                    break;

                // 3. Favorite Vocabulary
                case "3":
                    do {
                        Utils.println("[FAVORITE VOCABULARY]");
                        Utils.println("1. View");
                        Utils.println("2. Add");
                        Utils.println("3. Remove");

                        do {
                            Utils.print(">> Enter 1, 2, or 3 : ");
                            optionKey = scanner.nextLine();
                        } while (optionKey.compareTo("1") < 0 || optionKey.compareTo("3") > 0);

                        Utils.println();

                        if (optionKey.equals("1")) {
                            this.viewFavoriteVocabulary();
                        }

                        if (optionKey.equals("2")) {
                            this.addFavoriteVocabulary();
                        }

                        if (optionKey.equals("3")) {
                            this.removeFavoriteVocabulary();
                        }

                        Utils.println();
                        Utils.println("[?] Do you want to continue using [FAVORITE VOCABULARY] ?");
                        Utils.print(">> Enter (Y)/N : ");
                        optionKey = scanner.nextLine();

                        Utils.println();
                    } while (!optionKey.toUpperCase().equals("N"));

                    break;

                // 4. Search Log
                case "4":
                    do {
                        this.viewSearchLog();

                        Utils.println();
                        Utils.println("[?] Do you want to continue using [SEARCH LOG] ?");
                        Utils.print(">> Enter (Y)/N : ");
                        optionKey = scanner.nextLine();

                        Utils.println();
                    } while (!optionKey.toUpperCase().equals("N"));

                    break;
            }

        // 5. Change Dictionary / Back and Sync Data
        } while (!optionKey.equals("5"));
    }
}
