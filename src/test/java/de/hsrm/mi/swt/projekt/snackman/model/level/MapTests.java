package de.hsrm.mi.swt.projekt.snackman.model.level;

import de.hsrm.mi.swt.projekt.snackman.configuration.MapGenerationConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class MapTests {

    Map map;
    int w = 15;
    int h = 10;

    @BeforeEach
    void setUp() {
        map = new Map(w, h);
    }

    /**
     * tests weather middle tile is free
     */
    @Test
    void testGhostSpawn() {
        Assertions.assertEquals(OccupationType.FREE, map.getAllTiles()[h / 2][w / 2].getOccupationType());
    }

    /**
     * tests weather all corner tiles are free
     */
    @Test
    void testPlayerSpawn() {
        Assertions.assertEquals(OccupationType.FREE, map.getAllTiles()[1][1].getOccupationType());
        Assertions.assertEquals(OccupationType.FREE, map.getAllTiles()[h - 2][1].getOccupationType());
        Assertions.assertEquals(OccupationType.FREE, map.getAllTiles()[1][w - 2].getOccupationType());
        Assertions.assertEquals(OccupationType.FREE, map.getAllTiles()[h - 2][w - 2].getOccupationType());
    }

    /**
     * tests map size
     */
    @Test
    void testDimensions() {
        Assertions.assertEquals(h, map.getAllTiles().length);
        Assertions.assertEquals(w, map.getAllTiles()[0].length);
    }

    /**
     * tests weather map is surrounded by wall
     */
    @Test
    void testOuterWall() {
        Tile[][] tiles = map.getAllTiles();
        for (int row = 0; row < h; row++) {
            if (row > 0 && row < h - 1) {
                Assertions.assertEquals(OccupationType.WALL, tiles[row][0].getOccupationType());
                Assertions.assertEquals(OccupationType.WALL, tiles[row][w - 1].getOccupationType());
            } else {
                for (int col = 0; col < w; col++) {
                    Assertions.assertEquals(OccupationType.WALL, tiles[row][col].getOccupationType());
                }
            }
        }
    }

    /**
     * test weather new file is created and in the correct directory during saving
     */
    @Test
    void testSaveAsCSVCreatesNewFile() {
        File dir = new File(MapGenerationConfig.SAVED_MAPS_PATH);
        if (dir.exists() && dir.isDirectory()) {
            int numFilesBefore = Objects.requireNonNull(dir.list()).length;
            map.saveAsCSV();
            int numFilesAfter = Objects.requireNonNull(dir.listFiles()).length;
            Assertions.assertEquals(numFilesBefore + 1, numFilesAfter);

            if (!Objects.requireNonNull(Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                    .max(Comparator.comparingLong(File::lastModified))
                    .orElse(null)).delete()) {
                fail("file could not be deleted");
            }

        } else {
            fail("directory not found");
        }
    }

    /**
     * tests weather saved csv file is correct
     */
    @Test
    void testSaveAsCSVSavedFileIsCorrect() {
        map.saveAsCSV();

        File dir = new File(MapGenerationConfig.SAVED_MAPS_PATH);
        if (dir.exists() && dir.isDirectory()) {

            File newFile = Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                    .max(Comparator.comparingLong(File::lastModified))
                    .orElse(null);

            Assertions.assertNotNull(newFile);
            try (BufferedReader reader = new BufferedReader(new FileReader(newFile.getPath()))) {
                String line;
                int lines = 0;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    Assertions.assertEquals(w, tokens.length);

                    for (int col = 0; col < w; col++) {
                        switch (map.getTileAt(col, lines).getOccupationType()) {
                            case FREE -> Assertions.assertEquals("0", tokens[col]);
                            case WALL -> Assertions.assertEquals("-1", tokens[col]);
                            case ITEM -> Assertions.assertEquals("1", tokens[col]);
                            default -> fail("unknown OccupationType in map: " + map.getTileAt(col, lines).getOccupationType());
                        }
                    }
                    lines++;
                }
                Assertions.assertEquals(h, lines);
                if (!newFile.delete()) {
                    fail("file could not be deleted");
                }
            } catch (IOException e) {
                fail("Exception: " + e.getMessage());
            }

        } else {
            fail("directory not found");
        }
    }

    /**
     * tests weather map is correctly build based on test file
     */
    @Test
    void testFileConstructor() {
        map = new Map(MapGenerationConfig.SAVED_MAPS_PATH + "testFile.csv");

        Assertions.assertEquals(15, w);
        Assertions.assertEquals(10, h);

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                if (row == 0 || col == 0 || row == h - 1 || col == w - 1) {
                    Assertions.assertEquals(OccupationType.WALL, map.getTileAt(col, row).getOccupationType());
                } else {
                    if ((col + row) % 2 == 0) {
                        Assertions.assertEquals(OccupationType.FREE, map.getTileAt(col, row).getOccupationType());
                    } else {
                        Assertions.assertEquals(OccupationType.ITEM, map.getTileAt(col, row).getOccupationType());
                    }
                }
            }
        }
    }
}
