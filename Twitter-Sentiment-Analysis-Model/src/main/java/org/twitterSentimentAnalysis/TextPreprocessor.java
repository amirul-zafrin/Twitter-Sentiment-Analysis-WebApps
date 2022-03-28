package org.twitterSentimentAnalysis;

import java.io.*;
import java.util.*;

public class TextPreprocessor {

    static String delimiter = ",";
    private boolean train = true;
    public static ArrayList<String> data;
    public static ArrayList<String> label;
    public static int dataLen;
    public static int cursor = 0;

    public static String twitterPreprocessor(String s) throws IOException {
        s = s.toLowerCase();
        s = removeURL(s);
        s = removeNumber(s);
        s = removeTag(s);
        s = removeSpecialCharac(s);
        s = normalizeSounds(s, Tatabahasa.getNormalizeWord());
        s = normalizeDateReplace(s, Tatabahasa.getNormalizedDateString());
        s = removeStopwordsTXT(s);
        s = removeTataStopwords(s, Tatabahasa.getTatabahasaStopwords());
//            s = stemHujung(s,Tatabahasa.getHujung());
//            s = stemAwal(s, Tatabahasa.getAwal());

        return s;
    }

    public static ArrayList<String> readCSV(String fileName) throws IOException {
        String line = "";
        ArrayList<String> data = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(fileName));

        while ((line = br.readLine()) != null){
            String txt = twitterPreprocessor(line.split(delimiter)[0]);
            if(txt.length() > 2) {
                data.add(txt);
            }
        }
        return data;
    }

    public static ArrayList<String> readTXT(String fileName) throws IOException {
        String line = "";
        ArrayList<String> data = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(fileName));

        while ((line = br.readLine()) != null){
            String txt = twitterPreprocessor(line);
            data.add(txt);
        }
        return data;
    }

    public static ArrayList<String> readTXT(File fileName) throws IOException {
        String line = "";
        ArrayList<String> data = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(fileName));

        while ((line = br.readLine()) != null){
            String txt = twitterPreprocessor(line);
            if(txt.length() > 2) {
                data.add(txt);
            }
        }
        return data;
    }

    public static ArrayList<String> readLabelCSV(String fileName) throws IOException {
        String line = "";
        ArrayList<String> data = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(fileName));

        while ((line = br.readLine()) != null){
            data.add(line.split(delimiter)[1].toLowerCase());
        }
        return data;
    }

    private static String removeURL(String s){
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
//        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(s);
//        int i = 0;
//        while (m.find()) {
//            s = s.replaceAll(m.group(i),"").trim();
//            i++;
//        }
//        return s;
        return s.replaceAll(urlPattern,"").trim();
    }

    public static String removeNumber(String s) {
        s = s.replaceAll("[0-9]", "").trim();
        return s;
    }

    public static String removeTag(String s) {
        String tagPattern = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
        return s.replaceAll(tagPattern,"@user").trim();
    }

    public static String removeSpecialCharac(String s) {
        s = s.replaceAll("[^\\p{ASCII}]", "");
        s = s.replace(".", " ");
        s = s.replace("/", " ");
        s = s.replace("-", " ");
        s = s.replace(";", " ");
        s = s.replace("!", " ");
        s = s.replace(":", " ");
        s = s.replace("?", " ");
        s = s.replace(",", " ");
        s = s.replace("(", " ");
        s = s.replace(")", " ");
        s = s.replace("[", " ");
        s = s.replace("]", " ");
        s = s.replace("\"", " ");
        s = s.replace("'", "");
        s = s.replace("#", " ");
        s = s.replace("&", " ");
        s = s.replace("=", " ");
        s = s.replace("|", " ");
        s = s.replace(">", " ");
        s = s.replace("<", " ");
        s = s.replace("*", " ");
        s = s.replace("_", " ");
        s = s.replace("%", " ");
//        s = s.replace("@", " ");
        s = s.replace("\\", " ");
        s = s.replace("{", " ");
        s = s.replace("}", " ");
        s = s.replace("^", " ");
        s = s.replace("$", " ");
        s = s.replace("+", " ");
        s = s.replace("*", " ");
        s = s.replace("~", " ");
        s = s.replace("`", " ");

        return s.trim();
    }
//
    public static String normalizeSounds(String s, HashMap<String, String> sounds) {
        String[] sSplit = s.split(" ");
        String lines = "";
        for(String sl : sSplit){
            if(sounds.containsKey(sl)) {
                lines = lines + sounds.get(sl) + " ";
            } else {
                lines = lines + sl +" ";
                }
            }
        return lines.trim();
    }

    public static String normalizeDateReplace(String s, HashMap<String, String> dateReplace) {
        String[] sSplit = s.split(" ");
        String lines = "";
        for(String sl : sSplit){
            if(dateReplace.containsKey(sl)) {
                lines = lines + dateReplace.get(sl) + " ";
            } else {
                lines = lines + sl +" ";
            }
        }
        return lines.trim();
    }

    public static String stemHujung(String s, String[] hjg){
        String listString = String.join("|",hjg);
        String hjgPattern = "(\\w+)"+listString+"\\b";
        return s.replaceAll(hjgPattern,"").trim();
    }

    public static String stemAwal(String s, String[] awal) {
        String listString = String.join("|",awal);
        String hjgPattern = "\\b("+listString+")(\\w+)";
        return s.replaceAll(hjgPattern,"\2").trim();
    }

    public static String removeTataStopwords(String s, Set<String> stopword) {
        String[] sSplit = s.split(" ");
        String lines = "";
        for(String sl : sSplit){
            if(!stopword.contains(sl)) {
                lines = lines + sl +" ";
            }
        }
        return lines.trim();
    }

    public static String removeStopwordsTXT(String s) throws IOException {
        BufferedReader bufReader = new BufferedReader(new FileReader("C:/Users/zafri/OneDrive/Desktop/NLP-Project/dataset/malay-stopword.txt"));
        ArrayList<String> listOfLines = new ArrayList<>();

        String line = bufReader.readLine();
        while (line != null) {
            listOfLines.add(line);
            line = bufReader.readLine();
        }
        bufReader.close();
        String result = "";
        String[] sSplit = s.split(" ");
        for(String sl : sSplit) {
            if(!listOfLines.contains(sl)) {
                result = result + sl +" ";
            }
        }
        return result;
    }

}
