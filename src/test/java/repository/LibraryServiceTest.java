package repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.types.StandardEntryType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibraryServiceTest {
    LibraryService libraryService;

    @BeforeEach
    public void setupService(@TempDir Path workingDir) throws IOException, URISyntaxException {
        workingDir = Paths.get("C:\\test");
        this.libraryService = new LibraryService(workingDir);
        Path lib1 = Paths.get(LibraryServiceTest.class.getClassLoader().getResource("repository/lib1.bib").toURI());
        Path lib2 = Paths.get(LibraryServiceTest.class.getClassLoader().getResource("repository/lib2.bib").toURI());
        Files.copy(lib1, workingDir.resolve("lib1.bib"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(lib2, workingDir.resolve("lib2.bib"), StandardCopyOption.REPLACE_EXISTING);
        assert Files.exists(workingDir.resolve("lib1.bib"));
        assert Files.exists(workingDir.resolve("lib2.bib"));
    }

    @Test
    public void getLibraryNames() throws IOException {
        assertEquals(List.of("lib1.bib", "lib2.bib"), libraryService.getLibraryNames());
    }

    @Test
    public void getLibraryEntriesOnExistingLibrary() throws IOException {
        List<BibEntry> entries = libraryService.getLibraryEntries("lib1.bib");
        List<BibEntry> expectedEntries = getEntriesLib1();
        entries.sort(Comparator.comparing(o -> o.getCitationKey().orElse("")));
        assertEquals(expectedEntries, entries);
    }

    @Test
    public void getLibraryEntriesOnNonExistingLibrary() {
        assertThrows(IOException.class, () -> libraryService.getLibraryEntries("nonexistinglibrary.bib"));
    }

    @Test
    public void checkExistenceOfExistingLibrary() {
        assertTrue(libraryService.libraryExists("lib1.bib"));
    }

    @Test
    public void checkExistenceOfExistingLibraryWithoutExtension() {
        assertTrue(libraryService.libraryExists("lib1"));
    }

    @Test
    public void checkExistenceOfNonExistingLibrary() {
        assertFalse(libraryService.libraryExists("nonexistinglibrary.bib"));
    }

    @Test
    public void deleteLibrary() throws IOException {
        assertTrue(libraryService.libraryExists("lib2"));
        assertTrue(libraryService.deleteLibrary("lib2"));
        assertFalse(libraryService.libraryExists("lib2"));
    }

    @Test
    public void deleteNonExistingLibrary() throws IOException {
        assertFalse(libraryService.libraryExists("nonexistinglibrary.bib"));
        assertFalse(libraryService.deleteLibrary("nonexistinglibrary.bib"));
    }

    @Test
    public void addNewEntryToLibrary() throws IOException {
        BibEntry newEntry = new BibEntry(StandardEntryType.Book)
                .withCitationKey("Harrer2018java")
                .withField(StandardField.AUTHOR, "Harrer, S. and Lenhard, J. and Dietz, L.")
                .withField(StandardField.DATE, "2018-03-20")
                .withField(StandardField.TITLE, "Java by Comparison: Become a Java Craftsman in 70 Examples");
        newEntry.setChanged(true);
        libraryService.addEntryToLibrary("lib1", newEntry);

        List<BibEntry> currentEntries = libraryService.getLibraryEntries("lib1");
        List<BibEntry> expected = getModifiedEntriesLib1();
        currentEntries.sort(Comparator.comparing(o -> o.getCitationKey().orElse("")));

        assertEquals(expected, currentEntries);
    }

    private List<BibEntry> getModifiedEntriesLib1() {
        BibEntry newEntry = new BibEntry(StandardEntryType.Book)
                .withCitationKey("Harrer2018java")
                .withField(StandardField.AUTHOR, "Harrer, S. and Lenhard, J. and Dietz, L.")
                .withField(StandardField.DATE, "2018-03-20")
                .withField(StandardField.TITLE, "Java by Comparison: Become a Java Craftsman in 70 Examples");
        BibEntry entry1 = new BibEntry(StandardEntryType.Article)
                .withCitationKey("Saha2018")
                .withField(StandardField.AUTHOR, "Prashanta Saha and Upulee Kanewala")
                .withField(StandardField.DATE, "2018-02-20")
                .withField(StandardField.TITLE, "Fault Detection Effectiveness of Source Test Case Generation Strategies for Metamorphic Testing");
        BibEntry entry2 = new BibEntry(StandardEntryType.Article)
                .withCitationKey("Sanchez2016")
                .withField(StandardField.AUTHOR, "Jimi Sanchez")
                .withField(StandardField.DATE, "2016-06-01")
                .withField(StandardField.TITLE, "A Review of Pair-wise Testing");
        BibEntry entry3 = new BibEntry(StandardEntryType.Article)
                .withCitationKey("Wu2007")
                .withField(StandardField.AUTHOR, "Cheng-Wen Wu")
                .withField(StandardField.DATE, "2007-10-25")
                .withField(StandardField.TITLE, "SOC Testing Methodology and Practice");
        BibEntry entry4 = new BibEntry(StandardEntryType.Article)
                .withCitationKey("Zhu2019")
                .withField(StandardField.AUTHOR, "Hong Zhu and Ian Bayley and Dongmei Liu and Xiaoyu Zheng")
                .withField(StandardField.DATE, "2019-12-20")
                .withField(StandardField.TITLE, "Morphy: A Datamorphic Software Test Automation Tool");
        return List.of(newEntry, entry1, entry2, entry3, entry4);
    }

    private List<BibEntry> getEntriesLib1() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Article)
                .withCitationKey("Saha2018")
                .withField(StandardField.AUTHOR, "Prashanta Saha and Upulee Kanewala")
                .withField(StandardField.DATE, "2018-02-20")
                .withField(StandardField.TITLE, "Fault Detection Effectiveness of Source Test Case Generation Strategies for Metamorphic Testing");
        BibEntry entry2 = new BibEntry(StandardEntryType.Article)
                .withCitationKey("Sanchez2016")
                .withField(StandardField.AUTHOR, "Jimi Sanchez")
                .withField(StandardField.DATE, "2016-06-01")
                .withField(StandardField.TITLE, "A Review of Pair-wise Testing");
        BibEntry entry3 = new BibEntry(StandardEntryType.Article)
                .withCitationKey("Wu2007")
                .withField(StandardField.AUTHOR, "Cheng-Wen Wu")
                .withField(StandardField.DATE, "2007-10-25")
                .withField(StandardField.TITLE, "SOC Testing Methodology and Practice");
        BibEntry entry4 = new BibEntry(StandardEntryType.Article)
                .withCitationKey("Zhu2019")
                .withField(StandardField.AUTHOR, "Hong Zhu and Ian Bayley and Dongmei Liu and Xiaoyu Zheng")
                .withField(StandardField.DATE, "2019-12-20")
                .withField(StandardField.TITLE, "Morphy: A Datamorphic Software Test Automation Tool");
        return List.of(entry1, entry2, entry3, entry4);
    }
}
