package vn.edu.hcmus.fit.sv18120113.BT4;

public class Vocabulary {
    private String word;
    private String meaning;

    Vocabulary(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public String getWord() {
        return this.word.trim();
    }

    public String getMeaning() {
        return this.meaning.trim();
    }

    public static void printHeader() {
        System.out.printf("%-32s%-32s\n", "Vocabulary", "Meaning");
        for (int i = 0; i < 64; ++i) {
            Utils.print("-");
        }
        Utils.println();
    }

    public void print() {
        System.out.printf("%-32s", this.getWord());

        String[] lines = Utils.getLines(this.getMeaning());

        for (int i = 0; i < lines.length; ++i) {
            String meaning = lines[i].trim();
            if (meaning.isEmpty()) continue;

            if (i == 0) {
                System.out.printf("%-32s", meaning);
            } else {
                System.out.printf("%-32s%-32s", "", meaning);
            }

            if (i < lines.length - 1) {
                Utils.println();
            }
        }

        Utils.println();
    }

    public void printWithHeader() {
        printHeader();
        this.print();
    }
}
