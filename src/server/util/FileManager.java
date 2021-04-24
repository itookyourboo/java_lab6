package server.util;

import common.Config;
import common.exceptions.NoAccessToFileException;
import common.model.StudyGroup;
import common.xml.StudyGroups;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeSet;

public class FileManager {

    private final String envVariable;

    public FileManager(String envVariable) {
        this.envVariable = envVariable;
    }

    /**
     * Writes collection to a file.
     * @param collection Collection to write.
     */
    public void writeCollection(TreeSet<StudyGroup> collection) {
        if (System.getenv().get(envVariable) != null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(StudyGroups.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                StudyGroups result = new StudyGroups();
                result.setStudyGroups(collection);
                marshaller.marshal(result, getBufferedWriter());

                System.out.println("Коллекция успешно сохранена в файл!");
            } catch (JAXBException | IOException e) {
                System.out.println("Ошибка сохранения в файл!");
            } catch (NoAccessToFileException e) {
                System.out.println("Нет доступа к файлу!");
            }
        } else System.out.println("Системная переменная с загрузочным файлом не найдена!");
    }

    /**
     * Reads collection from a file.
     * @return Readed collection.
     */
    public TreeSet<StudyGroup> readCollection() {
        if (System.getenv().get(envVariable) != null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(StudyGroups.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                unmarshaller.unmarshal(getInputStreamReader());
                StudyGroups studyGroups = (StudyGroups) unmarshaller.unmarshal(new File(System.getenv(envVariable)));
                TreeSet<StudyGroup> collection = studyGroups.getStudyGroups();
                System.out.println("Коллекция успешно загружена! Размер: " + collection.size());
                return collection;
            } catch (NoSuchElementException exception) {
                System.out.println("Загрузочный файл пуст!");
            } catch (NullPointerException exception) {
                System.out.println("В загрузочном файле не обнаружена необходимая коллекция!");
            } catch (IllegalStateException exception) {
                System.out.println("Непредвиденная ошибка!");
            } catch (JAXBException e) {
                System.out.println("Ошибка прочтения XML-файла");
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден");
            } catch (NoAccessToFileException e) {
                System.out.println("Нет доступа к файлу!");
            }
        } else System.out.println("Системная переменная с загрузочным файлом не найдена!");
        System.out.println("Проверьте переменную окружения " + envVariable + " и запустите заново.");
        return new TreeSet<>();
    }

    public static String[] getLoginData() {
        try {
            Scanner scanner = new Scanner(new FileReader(Config.TOP_SECRET_FILE));
            return new String[] {scanner.nextLine(), scanner.nextLine()};
        } catch (FileNotFoundException e) {
            System.out.println("Не найден файл с данными для доступа к базе данных.");
        }
        return null;
    }

    private InputStreamReader getInputStreamReader() throws FileNotFoundException, NoAccessToFileException {
        File file = new File(System.getenv(envVariable));
        if (file.exists() && !file.canRead()) throw new NoAccessToFileException();
        return new InputStreamReader(new FileInputStream(file));
    }

    private BufferedWriter getBufferedWriter() throws IOException, NoAccessToFileException {
        File file = new File(System.getenv(envVariable));
        if (file.exists() && !file.canWrite()) throw new NoAccessToFileException();
        return new BufferedWriter(new FileWriter(new File(System.getenv(envVariable))));
    }
}