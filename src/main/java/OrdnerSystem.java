

import DateiService.DateienLeseService;
import DateiService.DateienManipulationsService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class OrdnerSystem {

    private final Logger logger;
    private static final String FILE_SEP = File.separator;
    private static final String ERROR_LOG_PATH = String.format("src%smain%sjava%serror%serror.log", FILE_SEP, FILE_SEP, FILE_SEP, FILE_SEP);
    private static final String LOGGER_LOG_PATH = String.format("src%smain%sjava%slog%slogger.log", FILE_SEP, FILE_SEP, FILE_SEP, FILE_SEP);



    public OrdnerSystem(Logger logger) throws IOException {
        this.logger = logger;
        setupLogger();
    }

    private void setupLogger() throws IOException {
        FileHandler errorFileHandler = new FileHandler(ERROR_LOG_PATH);
        FileHandler loggerFileHandler = new FileHandler(LOGGER_LOG_PATH);
        errorFileHandler.setFormatter(new SimpleFormatter());
        loggerFileHandler.setFormatter(new SimpleFormatter());
        errorFileHandler.setLevel(Level.SEVERE);
        loggerFileHandler.setLevel(Level.INFO);
        logger.addHandler(errorFileHandler);
        logger.addHandler(loggerFileHandler);
        if (logger.getClass().getName().contains("Mockito")){
            errorFileHandler.close();
            loggerFileHandler.close();
        }

    }
    public void verschiebeDateienVonInputZuImport(File inputOrdner, File importOrdner){
        List<Dokument> dokumentenListe = new ArrayList<>();
        for (File file : Objects.requireNonNull(inputOrdner.listFiles())){
            dokumentenListe.add(new Dokument(file.getName(), file.getParent()));
        }
        for (Dokument dokument : dokumentenListe){
            speicherDokument(dokument, importOrdner);
        }
    }

    public void manipuliereDateienVonImport(File importOrdner){
        List<String> pdfDatenVomImport;
        List<String> textDatenVomImport;
        for (File file : Objects.requireNonNull(importOrdner.listFiles())){
            try{
                if (file.getName().endsWith("pdf")){
                    pdfDatenVomImport = new DateienLeseService().lesePdfFileAlleZeilen(file);
                    new DateienManipulationsService().pdfErstellen(pdfDatenVomImport, file);
                    pdfDatenVomImport.clear();
                }
                if (file.getName().endsWith("txt") || file.getName().endsWith("log")){
                    textDatenVomImport = new DateienLeseService().leseTextFile(file);
                    new DateienManipulationsService().textdateiErstellen(textDatenVomImport, file);
                    textDatenVomImport.clear();
                }
            }
            catch (IOException e){
                logger.severe("Beim lesen der Datei ist ein Fehler aufgetreten!! " +
                        "Folgende Datei: "+file.getName()+" Exakte Exception: "+e.getMessage());
            }
        }
    }

    public void speicherDokument(Dokument dokument, File importOrdner) {
        try {
            if (istGueltigerOrdner(importOrdner)) {
            if (istValideDateiname(dokument.getDateiName())) {
                AblageReferenz ablageReferenz = new AblageReferenz(dokument.getDateiName(), dokument.getOrdnerName());
                    Files.move(Paths.get(ablageReferenz.getPfadUndDatei()), Paths.get(importOrdner.getPath() +FILE_SEP+ ablageReferenz.getAblageName()));
                    logger.info("Die Datei: " + dokument.getDateiName() + " wurde erfolgreich in den ImportOrdner verschoben!");
            } else {
                logger.severe("Dateiname von: " + dokument.getDateiName() + " ist leider nicht gültig und wird nun angepasst!!");
                String valideDateiName = getValideDateiname(dokument.getDateiName());
                File frischeFile = new File(dokument.getOrdnerName() + FILE_SEP + valideDateiName);
                baueValideDatei(frischeFile);
            }
            File invalideDatei = new File(dokument.getOrdnerName() + FILE_SEP + dokument.getDateiName());
            loescheDatei(invalideDatei);
            } else {
                logger.severe("Der Ordner: " + importOrdner.getName() + " scheint nicht zu existieren oder ist kein Ordner!! Bitte überprüfen!!");
            }
        } catch (IOException e) {
            logger.severe("Fehler beim Speichern des Dokuments! Datei existiert bereits im ImportOrdner!");
        }
    }

    private void baueValideDatei(File frischeDatei) throws IOException {
        if (frischeDatei.createNewFile()) {
            logger.info("Neue Datei erstellt: " + frischeDatei);
            Dokument newDoku = new Dokument(frischeDatei.getName(), frischeDatei.getParent());
            speicherDokument(newDoku, new File(Main.IMPORT_PATH));
        } else {
            logger.severe("Die Datei existiert bereits: " + frischeDatei);
        }
    }

    private boolean istGueltigerOrdner(File importOrdner) {
        return importOrdner.isDirectory() && importOrdner.exists();
    }

    private void loescheDatei(File zuLöschendeDatei){
        if (zuLöschendeDatei.exists() && !istValideDateiname(zuLöschendeDatei.getName())) {
            if (zuLöschendeDatei.delete()) {
                logger.info("Die Datei mit dem Namen: " + zuLöschendeDatei.getName() + " wurde erfolgreich im InputOrdner gelöscht!!");
            } else {
                logger.severe("Fehler beim Löschen der Datei: " + zuLöschendeDatei.getPath() + "/" + zuLöschendeDatei.getName());
            }
        }
    }
    public String getValideDateiname(String dateiName) {
        return dateiName.replaceAll("[^a-zA-Z0-9.]", "_");
    }

    private boolean istValideDateiname(String dateiName){
        return dateiName.replaceAll("[^a-zA-Z0-9.]","_").equals(dateiName);
    }
}
