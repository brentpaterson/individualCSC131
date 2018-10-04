/* Brent Paterson
 * CSC131
 * Individual sprint 2, Metrics.java
 */
import java.io.*;

class filesHolder {
    String names[];
    int lines[];
    int words[];
    int chars[];

    public filesHolder() {

    }
}

public class Metrics {
    private static String[] argsHolder;
    private static long totalLines = 0;
    private static long totalWords = 0;
    private static long totalChars = 0;
    private static Boolean l = false;
    private static Boolean w = false;
    private static Boolean c = false;

    public static void main(String[] args) {
        // check for help request
        Boolean h = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-' && args[i].contains("h")) {
                h = true;
                break;
            }
        }

        argsHolder = args;
        if (args.length > 0 && !h) {
            analyzeFile();
        } else {
            // no args or help requested
            instructions();
        }
    }

    private static void instructions() {
        System.out.println("wc will print instructions for how to use wc \n" +
                "wc -l <filename> will print the line count of a file\n" +
                "wc -c <filename> will print the character count\n" +
                "wc -w <filename> will print the word count\n" +
                "wc -s <filename> will print the source lines count\n" +
                "wc -C <filename> will print the comment lines count\n" +
                "wc -h <filename> will print this message\n" +
                "wc <filename> will print all of the above");
    }

    private static void analyzeFile() {
        Boolean fileExists = false;
        for (int i = 0; i < argsHolder.length; i++) {
            // is a file?
            if (argsHolder[i].charAt(0) != '-') {
                fileExists = true;
                runFile(argsHolder[i]);
            }
        }

        // output total if there was a file, otherwise instructions
        if (fileExists) {
            outputTotal();
        } else {
            instructions();
        }
    }

    private static void runFile(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
            long lines = 0;
            long words = 0;
            long chars = 0;

            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    lines++;

                    // get words in line
                    String trim = line.trim();
                    if (!trim.isEmpty())
                        words += trim.split("\\s+").length;

                    chars += line.length();
                } else {
                    break;
                }
            }

            totalLines += lines;
            totalWords += words;
            totalChars += chars;

            output(lines, words, chars, file);
        } catch (Exception e) {
            instructions();
            System.out.println("Error: " + e);
        }
    }

    private static void output(long lines, long words, long chars, String file) {

        // find what params were given for output
        for (int i = 0; i < argsHolder.length; i++) {
            if (argsHolder[i].charAt(0) == '-') {
                if (argsHolder[i].contains("l"))
                    l = true;
                if (argsHolder[i].contains("w"))
                    w = true;
                if (argsHolder[i].contains("c"))
                    c = true;
            }
        }

        // no params, all true
        if (!l && !w && !c) {
            l = true;
            w = true;
            c = true;
        }

        if (l)
            System.out.print(lines + " ");
        if (w)
            System.out.print(words + " ");
        if (c)
            System.out.print(chars + " ");

        System.out.print(file + "\n");
    }

    private static void outputTotal() {
        if (l)
            System.out.print(totalLines + " ");
        if (w)
            System.out.print(totalWords + " ");
        if (c)
            System.out.print(totalChars + " ");

        System.out.print("total\n");
    }
}
