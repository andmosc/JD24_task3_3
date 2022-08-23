package netology.task3_3;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * В данной задаче необходимо произвести обратную операцию по разархивации архива и десериализации файла сохраненной игры в Java класс.
 * <p>
 * Таким образом, для выполнения задания потребуется проделать следующие шаги:
 * <p>
 * Произвести распаковку архива в папке savegames.
 * Произвести считывание и десериализацию одного из разархивированных файлов save.dat.
 * Вывести в консоль состояние сохранненой игры.
 */
public class Main {
    public static void main(String[] args) {

        FileSystemView f = FileSystemView.getFileSystemView();
        String homePathDirectory = (f.getHomeDirectory()) + "\\Games";

        File saveGamesPath = new File(homePathDirectory + "\\savegames");

        File dirZip = new File(homePathDirectory, "save.zip");

        boolean unpack = openZip(dirZip, saveGamesPath);

        if (unpack) {
            for (File item: saveGamesPath.listFiles()) {
                System.out.println(openProgress(item));
            }

        }
    }

    private static GameProgress openProgress(File dirSaveFile) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dirSaveFile))) {

            return (GameProgress)inputStream.readObject();

        } catch (FileNotFoundException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean openZip(File dirZip, File saveGamesPath) {
        if (!saveGamesPath.exists()) {
            saveGamesPath.mkdir();
        }
        boolean unZipEx = false;
        try {
            ZipFile zip = new ZipFile(dirZip);

            Enumeration entries = zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                write(zip.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(entry.getName())));
            }
            zip.close();
            unZipEx = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return unZipEx;
    }

    private static void write(InputStream in, OutputStream out) {
        try {
            byte[] buffer = new byte[in.available()];
            int len;
            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer,0,len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
