package com.example.quicklasdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LasParser {
    public static Map<String, List<Float>> parse(ArrayList<String> fileLines) {

        Map<String, List<Float>> curves = new HashMap<>();
        int currIndex = 0;

        // read file content line-by-line.
        while (currIndex < fileLines.size()) {
            String line = fileLines.get(currIndex++);
            String[] lineList = splitByWhitespace(line);

            if (lineList[0].equals("~A")) {
                for (int i = 1; i < lineList.length; i++) {
                    curves.put(lineList[i], new ArrayList<>());
                }
                while (currIndex < fileLines.size()) {
                    String dataLine = fileLines.get(currIndex++);
                    String[] dataList = splitByWhitespace(dataLine);

                    for (int i = 1; i < dataList.length; i++) {
                        curves.get(lineList[i]).add(Float.parseFloat(dataList[i]));
                    }
                }
            }
        }

        return curves;
    }

    static String[] splitByWhitespace(String line){
        line = line.replaceAll("( )+", " ");
        return line.split(" ");
    }
}
