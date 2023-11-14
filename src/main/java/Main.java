

import DateiService.DateienLeseService;
import DateiService.DateienManipulationsService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class Main {
    private static final String FILE_SEP = File.separator;
    private static final String INPUT_PATH = String.format("src%smain%sjava%sinput", FILE_SEP, FILE_SEP, FILE_SEP);
    public static final String IMPORT_PATH = String.format("src%smain%sjava%simport", FILE_SEP, FILE_SEP, FILE_SEP, FILE_SEP);
    public static void main(String[] args) throws IOException {
        List<Dokument> dokumentenListe = new ArrayList<>();
        final Logger logger = Logger.getLogger(OrdnerSystem.class.getName());
        OrdnerSystem ordnerSystem = new OrdnerSystem(logger);
        File inputOrdner = new File(INPUT_PATH);
        File importOrdner = new File(IMPORT_PATH);
        for (File file : Objects.requireNonNull(inputOrdner.listFiles())){
            dokumentenListe.add(new Dokument(file.getName(), file.getParent()));
        }
        for (Dokument dokument : dokumentenListe){
            ordnerSystem.speicherDokument(dokument, importOrdner);
        }
        List<String> dataFromImportPDFFiles;
        List<String> dataFromImportTextFiles;
        for (File file : Objects.requireNonNull(importOrdner.listFiles())){
            if (file.getName().endsWith("pdf")){
                dataFromImportPDFFiles = new DateienLeseService().readPdfFileAllLines(file);
                new DateienManipulationsService().writeToPdf(dataFromImportPDFFiles);
                dataFromImportPDFFiles.clear();
            }
            if (file.getName().endsWith("txt") || file.getName().endsWith("log")){
                dataFromImportTextFiles = new DateienLeseService().readTextFile(file);
                new DateienManipulationsService().writeToText(dataFromImportTextFiles);
                dataFromImportTextFiles.clear();
            }

        }



    }


}