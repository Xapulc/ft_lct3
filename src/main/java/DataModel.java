import java.io.FileNotFoundException;
import java.util.*;

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

    DataModel(boolean isNewData) {
        if (isNewData)
            setAll();
    }

    DataModel() {}

    DataModel(String name, String surname, String patronymic, char sex,
              StringBuffer postcode, String country, String region, String city, String street) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.sex = sex;
        setRandBorn();
        setRandAge();
        setRandITN();
        this.postcode = postcode;
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        setRandHome();
        setRandFlat();
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

    private int getIntITN(int i) {
        return Integer.parseInt(""+ITN.charAt(i));
    }

    private Integer getControlDigit1() {
        int i = -1;
        int controlDigit = (7 * getIntITN(++i) + 2 * getIntITN(++i)
                + 4 * getIntITN(++i) + 10 * getIntITN(++i)
                + 3 * getIntITN(++i) + 5 * getIntITN(++i)
                + 9 * getIntITN(++i) + 4 * getIntITN(++i)
                + 6 * getIntITN(++i) + 8 * getIntITN(++i)) % 11;
        controlDigit = (controlDigit != 10) ? controlDigit : 0;
        return controlDigit;
    }

    private Integer getControlDigit2() {
        int i = -1;
        int controlDigit = (3 * getIntITN(++i) + 7 * getIntITN(++i)
                + 2 * getIntITN(++i) + 4 * getIntITN(++i)
                + 10 * getIntITN(++i) + 3 * getIntITN(++i)
                + 5 * getIntITN(++i) + 9 * getIntITN(++i)
                + 4 * getIntITN(++i) + 6 * getIntITN(++i)
                + 8 * getIntITN(++i)) % 11;
        controlDigit = (controlDigit != 10) ? controlDigit : 0;
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

    static int[] aligning(List<DataModel> dataModels) {
        int columnNum = 14;
        int[] maxSizeColumn = new int[columnNum];
        getDefaultMaxSizeColumn(maxSizeColumn);

        for (DataModel dataModel : dataModels) {
            int i = -1;
            maxSizeColumn[++i] = (dataModel.getName().length() > maxSizeColumn[i])
                    ? dataModel.getName().length()
                    : maxSizeColumn[i];

            maxSizeColumn[++i] = (dataModel.getSurname().length() > maxSizeColumn[i])
                    ? dataModel.getSurname().length()
                    : maxSizeColumn[i];

            maxSizeColumn[++i] = (dataModel.getPatronymic().length() > maxSizeColumn[i])
                    ? dataModel.getPatronymic().length()
                    : maxSizeColumn[i];

            String strAge = "" + dataModel.getAge();
            maxSizeColumn[++i] = (strAge.length() > maxSizeColumn[i])
                    ? strAge.length()
                    : maxSizeColumn[i];

            String strSex = "" + dataModel.getSex();
            maxSizeColumn[++i] = (strSex.length() > maxSizeColumn[i])
                    ? strSex.length()
                    : maxSizeColumn[i];

            String strBorn = "" + dataModel.getBorn().get(Calendar.DATE)
                    + "." + (1 + dataModel.getBorn().get(Calendar.MONTH))
                    + "." + dataModel.getBorn().get(Calendar.YEAR);
            maxSizeColumn[++i] = (strBorn.length() > maxSizeColumn[i])
                    ? strBorn.length()
                    : maxSizeColumn[i];

            maxSizeColumn[++i] = (dataModel.getITN().length() > maxSizeColumn[i])
                    ? dataModel.getITN().length()
                    : maxSizeColumn[i];

            maxSizeColumn[++i] = (dataModel.getPostcode().length() > maxSizeColumn[i])
                    ? dataModel.getPostcode().length()
                    : maxSizeColumn[i];

            maxSizeColumn[++i] = (dataModel.getCountry().length() > maxSizeColumn[i])
                    ? dataModel.getCountry().length()
                    : maxSizeColumn[i];

            maxSizeColumn[++i] = (dataModel.getRegion().length() > maxSizeColumn[i])
                    ? dataModel.getRegion().length()
                    : maxSizeColumn[i];

            maxSizeColumn[++i] = (dataModel.getCity().length() > maxSizeColumn[i])
                    ? dataModel.getCity().length()
                    : maxSizeColumn[i];

            maxSizeColumn[++i] = (dataModel.getStreet().length() > maxSizeColumn[i])
                    ? dataModel.getStreet().length()
                    : maxSizeColumn[i];

            String strHome = "" + dataModel.getHome();
            maxSizeColumn[++i] = (strHome.length() > maxSizeColumn[i])
                    ? strHome.length()
                    : maxSizeColumn[i];

            String strFlat = "" + dataModel.getFlat();
            maxSizeColumn[++i] = (strFlat.length() > maxSizeColumn[i])
                    ? strFlat.length()
                    : maxSizeColumn[i];
        }

        return maxSizeColumn;
    }

    private static void getDefaultMaxSizeColumn(int[] maxSizeColumn) {
        int i = -1;

        maxSizeColumn[++i] = "Имя".length();
        maxSizeColumn[++i] = "Фамилия".length();
        maxSizeColumn[++i] = "Отчество".length();
        maxSizeColumn[++i] = "Возраст".length();
        maxSizeColumn[++i] = "Пол".length();
        maxSizeColumn[++i] = "Дата рождения".length();
        maxSizeColumn[++i] = "ИНН".length();
        maxSizeColumn[++i] = "Почтовый индекс".length();
        maxSizeColumn[++i] = "Страна".length();
        maxSizeColumn[++i] = "Область".length();
        maxSizeColumn[++i] = "Город".length();
        maxSizeColumn[++i] = "Улица".length();
        maxSizeColumn[++i] = "Дом".length();
        maxSizeColumn[++i] = "Квартира".length();
    }

    @Override
    public String toString() {
        return "DataModel"
                + "Name = " + name + "\n"
                + "Surname = " + surname + "\n"
                /*+ born.get(Calendar.DATE) +
                "." + (1 + born.get(Calendar.MONTH)) +
                "." + born.get(Calendar.YEAR)*/;
    }
}
