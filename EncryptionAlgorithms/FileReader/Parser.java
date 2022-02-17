package FileReader;

import java.io.*;
import java.util.ArrayList;

public class Parser {
    private String filename;
    private InputStream file;
    public Parser(){

    }
    public Parser(String fn){
        filename = fn;
    }

    public String read(String pathname){
        BufferedReader br = null;
        FileReader fr = null;

        ArrayList<String> lines = new ArrayList<>();

        try {

            fr = new FileReader(pathname);
            br = new BufferedReader(fr);

            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        } finally {
            try {
                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                System.err.format("IOException: %s%n", ex);
            }
        }

        StringBuilder sb = new StringBuilder();
        for(String line : lines){
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    public void writeToTextFile(String content, String fn){
        if(fn != null){
            try{
                FileWriter fw = new FileWriter(fn+".txt");
                fw.write(content);
                fw.close();
            }catch (IOException exception){
                exception.printStackTrace();
            }
        }
    }

    public void writeToCustomFile(String content, String fn, String ext){
        if(fn != null){
            try{
                FileWriter fw = new FileWriter(fn+"."+ext);
                fw.write(content);
                fw.close();
            }catch (IOException exception){
                exception.printStackTrace();
            }
        }
    }

    public String reformat(String content){
        StringBuilder sb = new StringBuilder();

        int length = 120;
        for(int i = 1; i < content.length(); i++){
            if(i*length < content.length()){
                sb.append(content, (i-1)*length, i*length).append("\n");
            }
            else {
                if((i-1)*length < content.length()) {
                    sb.append(content.substring((i - 1) * length)).append("\n");
                }
            }
        }

        return sb.toString();
    }

    public String noNewLines(String content){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i <content.length(); i++){
            if(content.charAt(i) != '\n') sb.append(content.charAt(i));
        }
        return sb.toString();
    }

    public void reformatTextFile(String filename, String content){
        String reformattedContent = reformat(content);
        writeToTextFile(reformattedContent, filename.substring(0, filename.length()-4));
    }
    public void reformatCustomFile(String filename, String content, String ext){
        String reformattedContent = reformat(content);
        writeToCustomFile(reformattedContent, filename, ext);
    }

    public int lastDirectoryFileIndex(String dir){
        int index = 0;
        for(int i = dir.length()-1; i >= 0; i--){
            if(dir.charAt(i) == '\\'){
                index = i;
                break;
            }
        }
        return index;
    }
    public String getDestinationFileName(String dir){
        int index = lastDirectoryFileIndex(dir);
        return dir.substring(index+1);
    }

    public String getDestinationPathToFile(String dir){
        int substringIndex = lastDirectoryFileIndex(dir);
        return dir.substring(0, substringIndex+1);
    }

}