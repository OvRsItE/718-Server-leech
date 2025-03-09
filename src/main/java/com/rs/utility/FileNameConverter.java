package com.rs.utility;

import java.io.File;
import java.io.PrintStream;

public class FileNameConverter
{
  private static final String directory = "C:\\Users\\Corey\\.exora\\runescape\\";
  
  public static void main(String[] args)
  {
    File[] files = new File("C:\\Users\\Corey\\.exora\\runescape\\").listFiles();
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = files).length;
    for (int i = 0; i < j; i++)
    {
      File file = arrayOfFile1[i];
      if (file.getName().endsWith("idx"))
      {
        int index = Integer.parseInt(file.getName().replaceFirst(".idx", ""));
        File newName = new File("C:\\Users\\Corey\\Desktop\\converted\\main_file_cache.idx" + index);
        System.out.println("Renamed - " + file.getName() + " to " + newName.getName());
        file.renameTo(newName);
      }
    }
    System.out.println("Done");
  }
}
