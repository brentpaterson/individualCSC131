package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MetricsApp implements IMetrics {

    boolean setPath(String path);   // sets the file path to process
    // returns true if current path is valid
    boolean isSource();             // returns true if the file is a source file

    // basic counts for any file
    //
    int getLineCount(String file) {
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        int lines = 0;

        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;
            else
                lines++;
        }

        return lines;
    }

    int getWordCount(String file) {
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        int words = 0;

        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;
            else {
                String trim = line.trim();

                words += trim.split("\\s+").length;
            }
        }

        return words;
    }
    int getCharacterCount(String file) {
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        int chars = 0;

        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;
            else
                chars += line.length();
        }

        return chars;
    }

    // source code line counts
    //
    int getSourceLineCount(String file) {
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        int sourceLines = 0;
        boolean comments = false;

        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;
            else {
                line = line.trim();
                if (line.startsWith("//")) // full line comment
                    continue;
                if (line.startsWith("/*") && !line.endsWith("*/") && line.contains("*/")) {
                    //starts and ends comment but continues
                    sourceLines++;
                }
                if (line.startsWith("/*") && !line.contains("*/")) {
                    // begins multi line comment but does not end it
                    comments = true;
                    continue;
                }
                if (!line.startsWith("/*") && !comments) {
                    sourceLines++;
                    if (line.contains("/*") && !line.contains("*/"))
                        comments = true;
                }
                if (line.contains("*/")) {
                    comments = false;
                    if (!line.endsWith("*/"))
                        sourceLines++;
                }
            }
        }

        return sourceLines;
    }
    int getCommentLineCount();

    // Halstead metrics
    //
    int getHalsteadn1();            // number of distinct operands
    int getHalsteadn2();            // number of distinct operators
    int getHalsteadN1();            // number of operands
    int getHalsteadN2();            // number of operators

    int getHalsteadVocabulary();
    int getHalsteadProgramLength();
    int getHalsteadCalculatedProgramLenght();
    int getHalsteadVolume();
    int getHalsteadDifficulty();
    int getHalsteadEffort();
    int getHalsteadTime();
    int getHalsteadBugs();
}
