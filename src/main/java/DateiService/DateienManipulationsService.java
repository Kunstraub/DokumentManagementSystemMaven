package DateiService;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


import javax.sound.midi.Patch;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DateienManipulationsService {
    private static final String FILE_SEP = File.separator;
    final Logger logger = Logger.getLogger(DateienManipulationsService.class.getName());
    private final static String VERARBEITET_PATH = String.format("src%smain%sjava%sverarbeitet",FILE_SEP,FILE_SEP,FILE_SEP);

    public void writeToPdf(List<String> list){
        List<String> manipulierteListe = replaceTeamName(list);
        try{
            Document document = new Document(new PdfDocument(new PdfWriter(VERARBEITET_PATH+"/manipuliertePDFDatei.pdf")));
            for (String line : manipulierteListe){
                document.add(new Paragraph(line));
            }
            document.close();
            logger.info("PDF wurde erfolgreich erstellt unter: "+VERARBEITET_PATH);
        }
        catch (IOException e){
            logger.severe("Beim schreiben in die Pdf Datei in den VerarbeitungsOrdner" +
                    " ist ein Fehler passiert!! "+e.getMessage());
        }
    }

    public void writeToText(List<String> list){
        List<String> manipulierteListe = replaceJobName(list);
        Path pathVerarbeitet = new File(VERARBEITET_PATH+"/manipulierteTextDatei.txt").toPath();
        try(BufferedWriter writer = Files.newBufferedWriter(pathVerarbeitet)) {
            for (String line : manipulierteListe){
                writer.write(line);
            }
        } catch (IOException e) {
            logger.severe("Beim schreiben in die Text Datei in den VerarbeitungsOrdner" +
                    " ist ein Fehler passiert!! "+e.getMessage());
        }
    }
    private List<String> replaceTeamName(List<String> list){
        return list.stream().map(
                line -> line.replace("Projektteam", "Team")
        ).toList();
    }

    private List<String> replaceJobName(List<String> list){
       return list.stream().
               map(line -> line.replace("Junior-Java-Entwickler","Senior-Java-Entwickler Vollprofi"))
                .toList();
    }
}
