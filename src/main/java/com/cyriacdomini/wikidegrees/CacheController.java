package com.cyriacdomini.wikidegrees;

import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

public class CacheController {

    // String systemTempDir;
    String tempDirPath;
    File tempDir;

    public CacheController(){
        this.tempDirPath = System.getProperty("java.io.tmpdir").concat("wikidegrees");
        this.tempDir = new File(this.tempDirPath);
        // if(!this.tempDir.exists()){
        this.tempDir.mkdir(); //no need to check if folder exists
        // }
    }

    public static String encodeFilename(String s){
        try{
            return java.net.URLEncoder.encode(s, "UTF-8");
        }
        catch (java.io.UnsupportedEncodingException e){
            throw new RuntimeException("UTF-8 is an unknown encoding!?");
        }
    }

    public boolean isChached(String name){
        // System.out.println(this.tempDirPath + File.separator + encodeFilename(name));
        File file = new File(this.tempDirPath + File.separator + name);
        // System.out.println(file.exists());
        return file.exists();
    }

    public LinkedList<String> getFromCache(String name){
        // System.out.println(this.tempDirPath + File.separator + encodeFilename(name));
        File file = new File(this.tempDirPath + File.separator + encodeFilename(name));
        LinkedList<String> values = new LinkedList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuffer fileContents = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                values.add(line);;
                line = br.readLine();
            }
            br.close();
        } catch(Exception e){

        }
        return values;
    }    

    public void addToChache(String name, LinkedList<String> values){
        // System.out.println(this.tempDirPath + File.separator + encodeFilename(name));
        File file = new File(this.tempDirPath + File.separator + encodeFilename(name));
        try{
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            // System.out.println("Writing to file: \n "+ String.join("\\n",values));
            writer.write(String.join("\n",values));
            writer.close();
        }catch(Exception e){
            // e.printStackTrace();
        }
    }
}