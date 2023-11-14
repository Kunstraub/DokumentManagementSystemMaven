package DateiService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DateienLeseService {

    final Logger logger = Logger.getLogger(DateienLeseService.class.getName());

    public List<String> readTextFile(File textFile){
        Path path = textFile.toPath();
        List<String> textFileList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            String aktuelleZeile;
            while ((aktuelleZeile = reader.readLine()) != null){
                textFileList.add(aktuelleZeile);
            }
        } catch (IOException e) {
            logger.severe("Beim lesen der Text- oder Logdatei ist ein Fehler aufgetreten!! "+e.getMessage());
        }
        return textFileList;
    }

    public String readPdfFileAsWholeText(File pdfFile){
        String text = "";
       try{
           PDDocument document = PDDocument.load(pdfFile);
           PDFTextStripper pdfTextStripper = new PDFTextStripper();
           text = pdfTextStripper.getText(document);
           document.close();
       } catch (IOException e) {
           logger.severe("Beim lesen der PDF-Datei ist ein Fehler aufgetreten!! "+e.getMessage());
       }
       return text;
    }

    public List<String> readPdfFileAllLines(File pdfFile){
        List<String> lines = new ArrayList<>();
        try{
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            for (int page = 0; page < document.getNumberOfPages(); page++){
                pdfTextStripper.setStartPage(page + 1);
                pdfTextStripper.setEndPage(page + 1);
                String textFromPage = pdfTextStripper.getText(document);
                String[] textLines = textFromPage.split("\\r?\\n");
                for (String line : textLines) {
                    lines.add(line);
                }
            }
            document.close();
        }
        catch (IOException e) {
            logger.severe("Beim lesen der PDF-Datei ist ein Fehler aufgetreten!! "+e.getMessage());
        }
        return lines;
    }

}
