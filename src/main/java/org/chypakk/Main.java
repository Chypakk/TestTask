package org.chypakk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Main {

    private static String cleanLine(String line) {
        String[] fields = line.split(";", -1);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i].trim();

            if (field.startsWith("\"") && field.endsWith("\"")) {
                String content = field.substring(1, field.length() - 1);
                if (content.contains("\"")) return null;
                stringBuilder.append(content);
            } else {
                if (field.contains("\"")) return null;
                stringBuilder.append(field);
            }
            if (i < fields.length - 1) stringBuilder.append(';');
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) throws IOException {
        Instant start = Instant.now();

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("lng.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String cleaned = cleanLine(line);
                if (cleaned != null) lines.add(cleaned);
            }
        }

        Set<String> uniqueSet = new HashSet<>(lines);
        List<String> uniqueList = new ArrayList<>(uniqueSet);
        int n = uniqueList.size();

        List<String[]> partsList = new ArrayList<>(n);
        int maxColumns = 0;
        for (String line : uniqueList) {
            String[] parts = line.split(";", -1);
            partsList.add(parts);
            if (parts.length > maxColumns) {
                maxColumns = parts.length;
            }
        }

        DSU dsu = new DSU(n);

        for (int col = 0; col < maxColumns; col++) {
            Map<String, Integer> firstOccurrence = new HashMap<>();

            for (int row = 0; row < n; row++) {
                String[] parts = partsList.get(row);

                if (col < parts.length) {
                    String val = parts[col].trim();

                    if (!val.isEmpty()) {
                        if (firstOccurrence.containsKey(val)) {
                            dsu.union(firstOccurrence.get(val), row);
                        } else {
                            firstOccurrence.put(val, row);
                        }
                    }
                }
            }
        }

        Map<Integer, List<String>> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            groups.computeIfAbsent(root, a -> new ArrayList<>()).add(uniqueList.get(i));
        }

        List<List<String>> sortedGroups = new ArrayList<>(groups.values());
        sortedGroups.sort(
                (g1, g2) -> Integer.compare(g2.size(), g1.size())
        );

        try (PrintWriter out = new PrintWriter("output.txt")) {
            out.println("Количество групп: " + sortedGroups.size());
            int groupNum = 1;
            for (List<String> group : sortedGroups) {
                out.println("Группа " + groupNum++);
                for (String line : group) {
                    out.println(line);
                }
            }
        }

        Duration duration = Duration.between(start, Instant.now());
        System.out.println("Execution time: " + duration.toSeconds() + " s");
    }
}