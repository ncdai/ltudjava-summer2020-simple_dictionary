package vn.edu.hcmus.fit.sv18120113.BT4;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class LogReaderWriter {
    public static void writeLog(String fileName, String word) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            String newText = word
                    .trim()
                    .replace(",", " ")
                    .replaceAll("\\s+", " ");

            String logItem = String.join(",", Arrays.asList(LocalDate.now().toString(), newText));

            bufferedWriter.write(logItem);
            bufferedWriter.newLine();

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> readLog(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader);

            ArrayList<String[]> logList = new ArrayList<>();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] str = line.split(",");
                logList.add(str);
            }

            reader.close();
            return logList;

        } catch (IOException e) {
            return null;
        }
    }
}

