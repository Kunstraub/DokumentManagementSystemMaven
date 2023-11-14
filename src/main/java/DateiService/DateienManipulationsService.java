package DateiService;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class DateienManipulationsService {
    private static final String FILE_SEP = File.separator;
    final Logger logger = Logger.getLogger(DateienManipulationsService.class.getName());
    private final static String VERARBEITET_PATH = String.format("src%smain%sjava%sverarbeitet",FILE_SEP,FILE_SEP,FILE_SEP);

    public void writeToPdf(List<String> list){
        List<String> manipulierteListe = replaceTeamName(list);

        try{
            Document document = new Document(new PdfDocument(new PdfWriter(VERARBEITET_PATH)));
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
