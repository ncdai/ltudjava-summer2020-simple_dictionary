package vn.edu.hcmus.fit.sv18120113.BT4;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class LogList {
    String fileName;

    LogList(String logFile) {
        this.fileName = logFile;
    }

    public void writeLog(String word) {
        LogReaderWriter.writeLog(this.fileName, word);
    }

    ArrayList<Log> getLogByTimePeriod(LocalDate fromDate, LocalDate toDate) {
        Map<String, Log> tempLogList = new HashMap<>();

        ArrayList<String[]> data = LogReaderWriter.readLog(this.fileName);
        if (data == null) return null;

        for (String[] logItem : data) {
            String strDate = logItem[0];
            String strWord = logItem[1];

            LocalDate date = LocalDate.parse(strDate);
            String logKey = Utils.slugify(strWord);

            boolean isAfterEqual = date.isAfter(fromDate) || date.isEqual(fromDate);
            boolean isBeforeEqual = date.isBefore(toDate) || date.isEqual(toDate);

            if (isAfterEqual && isBeforeEqual) {
                try {
                    Log currentLog = tempLogList.get(logKey);
                    currentLog.incTotal();

                    tempLogList.put(logKey, currentLog);

                } catch (NullPointerException e) {
                    Log newLogItem = new Log(strWord, 1);
                    tempLogList.put(logKey, newLogItem);
                }
            }
        }

        ArrayList<Log> res = new ArrayList<>();

        Set<String> keys = tempLogList.keySet();
        for (String key : keys) {
            Log logItem = tempLogList.get(key);
            res.add(logItem);
        }

        return res;
    }

    public void printLogByTimePeriod(LocalDate fromDate, LocalDate toDate) {
        System.out.printf("%-32s%-16s\n", "Word", "Times");
        for (int i = 0; i < 32 + 16; ++i) {
            System.out.print("-");
        }
        Utils.println();

        ArrayList<Log> logList = this.getLogByTimePeriod(fromDate, toDate);
        for (Log logItem : logList) {
            System.out.printf("%-32s%-16s\n", logItem.getWord(), logItem.getTotal());
        }
    }

    static class Log {
        String word;
        int total;

        Log(String word, int total) {
            this.word = word;
            this.total = total;
        }

        public String getWord() {
            return this.word.trim();
        }

        public int getTotal() {
            return this.total;
        }

        public void incTotal() {
            this.total += 1;
        }
    }
}
