import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Integer.*;

class DataModel {
    private String name;
    private String surname;
    private String patronymic;  // father's name
    private int age;
    private char sex;
    private GregorianCalendar born;
    private StringBuffer ITN;
    private StringBuffer postcode;
    private String country;
    private String region;
    private String city;
    private String street;
    private int home;
    private int flat;

    private DataModel() {
        setAll();
    }

    private void setAll() {
        setRandSex();
        setRandName();
        setRandSurname();
        setRandPatronymic();
        setRandBorn();
        setRandAge();
        setRandITN();
        setRandPostcode();
        setRandCountry();
        setRandRegion();
        setRandCity();
        setRandStreet();
        setRandHome();
        setRandFlat();
    }

    String getName(){
        return name;
    }

    String getSurname() {
        return surname;
    }

    String getPatronymic() {
        return patronymic;
    }

    int getAge() {
        return age;
    }

    char getSex() {
        return sex;
    }

    GregorianCalendar getBorn() {
        return born;
    }

    StringBuffer getITN() {
        return ITN;
    }

    StringBuffer getPostcode() {
        return postcode;
    }

    String getCountry() {
        return country;
    }

    String getRegion() {
        return region;
    }

    String getCity() {
        return city;
    }

    String getStreet() {
        return street;
    }

    int getHome() {
        return home;
    }

    int getFlat() {
        return flat;
    }

    private String setRandObjects(String fileName) {
        fileName = "src/main/resources/" + fileName;
        List<String> objects = null;
        try {
            objects = FileWorker.read(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Random rnd = new Random(System.currentTimeMillis());
        assert objects != null;
        return objects.get(rnd.nextInt(objects.size()));
    }

    private void setRandName() {
        String fileName = (sex == 'M') ? "Names male.txt" : "Names female.txt";
        name = setRandObjects(fileName);
    }

    private void setRandSurname() {
        String fileName = (sex == 'M') ? "Surnames male.txt" : "Surnames female.txt";
        surname = setRandObjects(fileName);
    }

    private void setRandPatronymic() {
        String fileName = (sex == 'M') ? "Patronymics male.txt" : "Patronymics female.txt";
        patronymic = setRandObjects(fileName);
    }

    private void setRandBorn() {
        Random rnd = new Random(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        born = new GregorianCalendar(calendar.get(Calendar.YEAR) - 100 + rnd.nextInt(100),
                rnd.nextInt(12), rnd.nextInt(29));
    }

    private void setRandAge() {
        Calendar calendar = Calendar.getInstance();
        age = calendar.get(Calendar.YEAR)- born.get(Calendar.YEAR);
    }

    private void setRandSex() {
        Random rnd = new Random(System.currentTimeMillis());
        sex = (rnd.nextInt(128) % 2 != 0) ? 'M' : 'F';
    }

    private void setRandITN() {
        ITN = new StringBuffer();
        ITN.append(7);
        ITN.append(7); // number of Moscow region
        Random rnd = new Random(System.currentTimeMillis());
        for(int i = 0; i < 8; i++)
            ITN.append(rnd.nextInt(10));
        ITN.append(getControlDigit1());
        ITN.append(getControlDigit2());
    }

    private Integer getControlDigit1() {
        int i = -1;
        int controlDigit = (7 * valueOf(ITN.charAt(++i)) + 2 * valueOf(ITN.charAt(++i)) +
                4 * valueOf(ITN.charAt(++i)) + 10 * valueOf(ITN.charAt(++i)) +
                3 * valueOf(ITN.charAt(++i)) + 5 * valueOf(ITN.charAt(++i)) +
                9 * valueOf(ITN.charAt(++i)) + 4 * valueOf(ITN.charAt(++i)) +
                6 * valueOf(ITN.charAt(++i)) + 8 * valueOf(ITN.charAt(++i))) % 11;
        controlDigit = controlDigit != 10 ? controlDigit : 0;
        return controlDigit;
    }

    private Integer getControlDigit2() {
        int i = -1;
        int controlDigit =  (3* valueOf(ITN.charAt(++i)) + 7* valueOf(ITN.charAt(++i)) +
                2* valueOf(ITN.charAt(++i)) + 4* valueOf(ITN.charAt(++i)) +
                10* valueOf(ITN.charAt(++i)) + 3* valueOf(ITN.charAt(++i)) +
                5* valueOf(ITN.charAt(++i)) + 9* valueOf(ITN.charAt(++i)) +
                4* valueOf(ITN.charAt(++i)) + 6* valueOf(ITN.charAt(++i)) +
                8* valueOf(ITN.charAt(++i))) % 11;
        controlDigit = controlDigit != 10 ? controlDigit : 0;
        return controlDigit;
    }

    private void setRandPostcode() {
        postcode = new StringBuffer();
        Random rnd = new Random(System.currentTimeMillis());
        for(int i = 0; i < 6; i++)
            postcode.append(rnd.nextInt(10));
    }

    private void setRandCountry() {
        country = setRandObjects("Countries.txt");
    }

    private void setRandRegion() {
        region = setRandObjects("Regions.txt");
    }

    private void setRandCity() {
        city = setRandObjects("Cities.txt");
    }

    private void setRandStreet() {
        street = setRandObjects("Streets.txt");
    }

    private void setRandHome() {
        Random rnd = new Random(System.currentTimeMillis());
        home = 1 + rnd.nextInt(250);
    }

    private void setRandFlat() {
        Random rnd = new Random(System.currentTimeMillis());
        flat = 1 + rnd.nextInt(1000);
    }

    static List<DataModel> fillData() {
        List<DataModel> dataModels = new ArrayList<>();
        Random rnd = new Random(System.currentTimeMillis());
        int quantData =  1 + rnd.nextInt(30);

        for(int i = 0; i < quantData; i++)
            dataModels.add(new DataModel());
        return dataModels;
    }
}
