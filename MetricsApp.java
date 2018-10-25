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
    int getCommentLineCount(String file) {
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        int commentLines = 0;
        boolean comments = false;

        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;
            else {
                line = line.trim();
                if (comments)
                    commentLines++;
                if (line.contains("//"))
                    commentLines++;
                if (line.contains("/*")) {
                    commentLines++;
                    comments = true;
                    if (line.contains("*/"))
                        comments = false;
                }
            }
        }

        return commentLines;
    }

    // Halstead metrics
    //

    // number of distinct operands
    int getHalsteadn1() {

    }

    // number of distinct operators
    int getHalsteadn2();

    // number of operands
    int getHalsteadN1();

    // number of operators
    int getHalsteadN2();

    int getHalsteadVocabulary(int n1, int n2) {
        return n1 + n2;
    }

    int getHalsteadProgramLength(int N1, int N2) {
        return N1 + N2;
    }

    int getHalsteadCalculatedProgramLenght(int n1, int n2) {
        return (int) (n1 * (Math.log(n1) / Math.log(2)) + n2 * (Math.log(n2) / Math.log(2)));
    }

    int getHalsteadVolume(int nLength, int nVocabulary) {
        return (int) (nLength * (Math.log(nVocabulary) / Math.log(2)));
    }

    int getHalsteadDifficulty(int n1, int n2, int N2) {
        return (n1 / 2) + (N2 / n2);
    }

    int getHalsteadEffort(int nDifficulty, int nVolume) {
        return nDifficulty * nVolume;
    }

    int getHalsteadTime(int nEffort) {
        return nEffort / 18;
    }

    int getHalsteadBugs(int nVolume) {
        return nVolume / 3000;
    }
}
