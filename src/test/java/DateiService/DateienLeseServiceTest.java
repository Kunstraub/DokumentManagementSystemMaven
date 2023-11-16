package DateiService;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DateienLeseServiceTest{
@Test
public void testLeseTextFile() throws IOException {
    File tempFile = File.createTempFile("test",".txt");
    Files.write(tempFile.toPath(),"Zeile 1\nZeile 2\nZeile 3".getBytes());

    DateienLeseService dateienLeseService = new DateienLeseService();
    List<String> testList = dateienLeseService.leseTextFile(tempFile);

    assertEquals(3, testList.size());
    assertEquals("Zeile 1", testList.get(0));
    assertEquals("Zeile 2", testList.get(1));
    assertEquals("Zeile 3", testList.get(2));
    tempFile.delete();
     }
@Test
public void testLesePdfFileAlleZeilen() throws IOException {
    File tempPdfFile = File.createTempFile("test",".pdf");
    String pdfInhalt = "Hier benutze ich eine Testzeile für die PDF-Datei!";

    bauePdfMitInhalt(pdfInhalt,tempPdfFile);

    DateienLeseService dateienLeseService = new DateienLeseService();
    List<String> testList = dateienLeseService.lesePdfFileAlleZeilen(tempPdfFile);

    assertEquals(1,testList.size());
    assertEquals(pdfInhalt, testList.get(0));
    tempPdfFile.delete();
    }

    private void bauePdfMitInhalt(String inhalt, File outputFile) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.newLineAtOffset(10, 700); // Set starting position
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.showText(inhalt);
            contentStream.endText();
        }

        document.save(outputFile);
        document.close();
    }
}