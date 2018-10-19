/**
 * Я использую 3 API для генерации данных пользователя
 * В первом я получаю ФИО, пол, почтовый индекс и улицу
 * Во втором я по почтовому индексу получаю регион и город
 * В третьем я получаю страну
 * Из 3х Json'в я собираю его и произвожу десериализацию в свой класс
 */

import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.varia.NullAppender;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.http.impl.client.HttpClientBuilder.*;

class APIWorker implements JsonDeserializer<DataModel> {
    APIWorker() {}

    private String json = "";
    private StringBuffer jsonBuffer = new StringBuffer();

    DataModel fromJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DataModel.class, new APIWorker())
                .create();
        return gson.fromJson(json, DataModel.class);
    }

    public DataModel deserialize(JsonElement jsonElement,
            Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        String name = jsonElement.getAsJsonObject()
                .get("fname").getAsString();

        String surname = jsonElement.getAsJsonObject()
                .get("lname").getAsString();

        String patronymic = jsonElement.getAsJsonObject()
                .get("patronymic").getAsString();

        char sex = (jsonElement.getAsJsonObject().get("gender")
                .getAsString().charAt(0) == 'w') ? 'F' : 'M';

        StringBuffer postcode = new StringBuffer(jsonElement.getAsJsonObject()
                .get("postcode").getAsString());

        String city = jsonElement.getAsJsonObject()
                .get("city").getAsString();

        String street = jsonElement.getAsJsonObject()
                .get("street").getAsString();

        String region = jsonElement.getAsJsonObject()
                .get("region").getAsString();

        String country = jsonElement.getAsJsonObject()
                .get("country").getAsString();

        return new DataModel(
                name,
                surname,
                patronymic,
                sex,
                postcode,
                country,
                region,
                city,
                street
        );
    }

    private int findBeginInJson(StringBuffer src, String obj) {
        return src.indexOf(obj) + obj.length() + 3;
    }

    private String findObj(StringBuffer src, String obj) {
        return src.substring(findBeginInJson(src, obj),
                src.indexOf("\"", findBeginInJson(src, obj)));
    }

    boolean createData() {
        if (!(isJsonFilled("https://randus.org/api.php")))
            return false;
        json.replace("city", "uselessCity");
        jsonBuffer.append(json, 0, json.indexOf("house")-1);

        if (!(isJsonFilled("http://api.print-post.com/api/index/v2/?index="
                + findObj(jsonBuffer, "postcode"))))
            return false;
        if (json.contains("okrug")) {
            json.replace("region", "uselessRegion");
            json.replace("okrug", "region");
        }
        /*
          иногда генерирует строку с пустым регионом,
          тогда добавляется поле okrug, в котором
          фактически и содержится регион
         */
        jsonBuffer.append(json, json.indexOf("region\"")-1, json.indexOf("string")-1);

        if (!(isJsonFilled("http://uinames.com/api/")))
            return false;
        /*
          иногда может возвращать сообщение об ошибке
          в связи с большим количеством запросов
         */
        jsonBuffer.append("\"country").append(json, json.indexOf("region")+6, json.length());
        json = new String(jsonBuffer);

        return true;
    }

    private boolean isJsonFilled(String url) {
        //везде проверяется, есть ли связь
        BasicConfigurator.configure(new NullAppender());
        HttpClient client = create().build();
        HttpGet request = new HttpGet(url);

        if (!(isConnectionExist(url)))
            return false;

        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (NullPointerException | IOException e) {
            return false;
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        } catch (IOException e) {
            return false;
        }

        String line;
        json = "";
        try {
            while ((line = reader.readLine()) != null) json += line;
        } catch (IOException e) {
            return false;
        }

        if (json.contains("error")) {
            if (url.contains("print-post")) {
                //иногда первое API дает невалидный почтовый индекс
                if (!(isJsonFilled("https://randus.org/api.php")))
                    return false;
                jsonBuffer = new StringBuffer();
                jsonBuffer.append(json, 0, json.indexOf("house") - 1);

                return isJsonFilled("http://api.print-post.com/api/index/v2/?index="
                        + findObj(jsonBuffer, "postcode"));
            } else {
                //иногда третье API возвращает сообщение об ошибке
                return false;
            }
        }

        return true;
    }

    private static boolean isConnectionExist(String u) {
        URL url;
        try {
            url = new URL(u);
        } catch (MalformedURLException e) {
            return false;
        }
        HttpURLConnection http;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            return false;
        }
        try {
            return http.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }
}
