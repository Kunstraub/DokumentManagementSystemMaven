

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;


public class OrdnerSystemTest {

    private OrdnerSystem ordnerSystem;
    private final static String IMPORT_PFAD = String.format("src%smain%sjava%simport", File.separator, File.separator, File.separator);
    private final static String TESTORDNER_PFAD = String.format("src%stestOrdner",File.separator);
    private static final String ERROR_PFAD = String.format("src%smain%sjava%serror", File.separator, File.separator, File.separator);
    private static final String LOGGER_PFAD = String.format("src%smain%sjava%slog", File.separator, File.separator, File.separator);
    @Mock
    private Logger logger;
    private File testOrdner;
    private File importer = new File(IMPORT_PFAD);
    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        ordnerSystem = new OrdnerSystem(logger);
        testOrdner = new File(TESTORDNER_PFAD);
        testOrdner.mkdirs();
        File testfile = new File(testOrdner,"gueltig.txt");
        File InvalidTestfile = new File(testOrdner,"ungültig.txt");
        InvalidTestfile.createNewFile();
        testfile.createNewFile();
    }

    @After
    public void tearDown() {
        for (File file : Objects.requireNonNull(testOrdner.listFiles())) {
            file.delete();
        }
        testOrdner.delete();
        for (File testfileSearch : Objects.requireNonNull(importer.listFiles())) {
            if (testfileSearch.getName().equals("gueltig.txt")) {
                testfileSearch.delete();
            }
            if (testfileSearch.getName().equals("ung_ltig.txt")) {
                testfileSearch.delete();
            }
        }
        for (File errorFile : Objects.requireNonNull(new File(ERROR_PFAD).listFiles())) {
            errorFile.delete();
        }
        for (File logFile : Objects.requireNonNull(new File(LOGGER_PFAD).listFiles())) {
            logFile.delete();
        }
    }
    @Test
    public void testSpeicherDokumentErfolgreich() {
        Dokument dokument = new Dokument("gueltig.txt", TESTORDNER_PFAD);
        File importerFolder = mock(File.class);
        AblageReferenz ablageReferenz = mock(AblageReferenz.class);

        when(importerFolder.isDirectory()).thenReturn(true);
        when(importerFolder.exists()).thenReturn(true);
        when(importerFolder.getPath()).thenReturn(IMPORT_PFAD);
        when(ablageReferenz.getPfadUndDatei()).thenReturn(dokument.getOrdnerName()+File.separator+dokument.getDateiName());
        when(ablageReferenz.getAblageName()).thenReturn("gueltig.txt");

        ordnerSystem.speicherDokument(dokument, importerFolder);

        verify(logger).info("Die Datei: "+ dokument.getDateiName() +" wurde erfolgreich in den ImportOrdner verschoben!");
    }

    @Test
    public void testInvalideDateiZuValideUndInvalideImInputOrdnerGeloescht() {
        Dokument dokument = new Dokument("ungültig.txt", TESTORDNER_PFAD);
        File importerFolder = mock(File.class);
        AblageReferenz ablageReferenz = mock(AblageReferenz.class);

        when(importerFolder.isDirectory()).thenReturn(true);
        when(importerFolder.exists()).thenReturn(true);
        when(importerFolder.getPath()).thenReturn(IMPORT_PFAD);
        when(ablageReferenz.getPfadUndDatei()).thenReturn(dokument.getOrdnerName()+File.separator+dokument.getDateiName());
        when(ablageReferenz.getAblageName()).thenReturn("ungültig.txt");

        ordnerSystem.speicherDokument(dokument, importerFolder);

        verify(logger).severe("Dateiname von: "+dokument.getDateiName()+" ist leider nicht gültig und wird nun angepasst!!");
        verify(logger).info("Die Datei: "+ordnerSystem.getValideDateiname(dokument.getDateiName())+" wurde erfolgreich in den ImportOrdner verschoben!");
        verify(logger).info("Die Datei mit dem Namen: " + dokument.getDateiName() + " wurde erfolgreich im InputOrdner gelöscht!!");
    }

    @Test
    public void testKeinImporterOrdner() {
        Dokument dokument = new Dokument("gueltig.txt", TESTORDNER_PFAD);
        File importerFolder = mock(File.class);

        when(importerFolder.isDirectory()).thenReturn(false);
        when(importerFolder.exists()).thenReturn(true);
        when(importerFolder.getName()).thenReturn("import");

        ordnerSystem.speicherDokument(dokument, importerFolder);

        verify(logger).severe("Der Ordner: "+importerFolder.getName()+" scheint nicht zu existieren oder ist kein Ordner!! Bitte überprüfen!!");
    }

    @Test
    public void testExceptionWirdAusgeloestDurchDuplikatInImportOrdner(){
        Dokument dokument = mock(Dokument.class);
        when(dokument.getDateiName()).thenReturn("test.txt");

        ordnerSystem.speicherDokument(dokument, new File(IMPORT_PFAD));

        verify(logger).severe("Fehler beim Speichern des Dokuments! Datei existiert bereits im ImportOrdner!");

    }


}
