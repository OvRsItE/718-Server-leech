package com.rs.utility.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Provides a lightweight external data logger
 *
 * @author Octave
 * @date 2/1/13
 */

public class FileLogger {

    /**
     * Log file path
     */
    private static final String FILE_PATH = "./data/logs/";

    /**
     * Formats data before appending to log
     *
     * @param file - File to write data to
     * @param data - Data to written
     * @throws IOException
     */
    public static void logData(String file, String[] data) {
        try {
            writeData(file, formatStringArray(data));
        } catch (Exception e) {
            System.out.println("Fatal error while logging Data!");
        }
    }

    public static void logData(String file, String data) {
        try {
            writeData(file, data);
        } catch (Exception e) {
            System.out.println("Fatal error while logging Data!");
        }
    }

    /**
     * Format string array to proper log syntax
     *
     * @param origin - string to format
     * @return Array to string, formatted
     */
    private static String formatStringArray(String[] origin) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < origin.length; i++) {
            result.append(origin[i] + "\t");
        }
        String mynewstring = result.toString();
        return mynewstring;
    }

    /**
     * Fetches system time and formats it appropriately
     *
     * @return Formatted time
     */
    private static String getTime() {
        Date getDate = new Date();
        String timeFormat = "M/d/yy hh:mma";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return "[" + sdf.format(getDate) + "]\t";
    }

    /**
     * Writes formatted string to text file
     *
     * @param file - File to write data to
     * @param data - Data to written
     * @throws IOException
     */
    private static void writeData(String file, String writable)
            throws IOException {
        FileWriter fw = new FileWriter(FILE_PATH + file + ".txt", true);
        try {
            fw.write(getTime() + writable + "\t");
            fw.write("\n");
            fw.close();
        } catch (Exception e) {
            fw.write("Error processing data: " + e);
        }
    }

    public static void eraseFile(String name) {
        try {
            File file = new File(FILE_PATH + name + ".txt");
            file.delete();
            System.out.println(name + " logs automatically cleaned.");
            writeData(name,
                    "\t <----------------- File automatically cleaned ----------------->");
        } catch (Exception e) {
            System.out.println("Failed to clean logs.");
        }
    }

}