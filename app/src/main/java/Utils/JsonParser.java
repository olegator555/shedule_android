package Utils;

import Model.ServerAnswerModel;
import android.content.Context;
import com.olegator555.rasp.DB.DBManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser  {
    private JSONArray topLevelJsonArray;
    private List<ServerAnswerModel> stations_list;
    Context context;
    String selected_country, selected_region;

    public JsonParser(String json_string, Context context) {
        try {
            JSONObject top_levelObject = new JSONObject(json_string);
            this.topLevelJsonArray = top_levelObject.getJSONArray("countries");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.context = context;
    }

    public ArrayList<String> getCountriesList() {
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i< topLevelJsonArray.length(); i++) {
            try {
                String country = topLevelJsonArray.getJSONObject(i).getString("title");
                result.add(country);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public ArrayList<String> getRegionsList(String selected_country) {
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i< topLevelJsonArray.length(); i++) {
            try {
                String country = topLevelJsonArray.getJSONObject(i).getString("title");
                if(country.equals(selected_country)) {
                    JSONObject regionsLevelJsonObject = topLevelJsonArray.getJSONObject(i);
                    JSONArray regionsLevelJsonArray = regionsLevelJsonObject.getJSONArray("regions");
                    for (int j = 0; j < regionsLevelJsonArray.length(); j++) {
                        String region = regionsLevelJsonArray.getJSONObject(j).getString("title");
                        result.add(region);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    private void parseJson() {
        stations_list = new ArrayList<>();
        for(int i = 0; i< topLevelJsonArray.length(); i++){
            try {
                String country = topLevelJsonArray.getJSONObject(i).getString("title");
                if(country.equals(selected_country)) {
                    JSONObject regionsLevelJsonObject = topLevelJsonArray.getJSONObject(i);
                    JSONArray regionsLevelJsonArray = regionsLevelJsonObject.getJSONArray("regions");
                    for(int j = 0; j<regionsLevelJsonArray.length();j++){
                        String region = regionsLevelJsonArray.getJSONObject(j).getString("title");
                        if(region.equals(selected_region)) {
                            JSONObject settlementsLevelJsonObject = regionsLevelJsonArray.getJSONObject(j);
                            JSONArray settlementsLevelJsonArray = settlementsLevelJsonObject.getJSONArray("settlements");
                            for(int k=0; k<settlementsLevelJsonArray.length(); k++) {
                                String settlement = settlementsLevelJsonArray.getJSONObject(k).getString("title");
                                JSONObject stationsLevelJsonObject = settlementsLevelJsonArray.getJSONObject(k);
                                JSONArray stationsLevelJsonArray = stationsLevelJsonObject.getJSONArray("stations");
                                for(int z=0; z<stationsLevelJsonArray.length(); z++) {
                                    String station_name = stationsLevelJsonArray.getJSONObject(z).getString("title");
                                    String direction = stationsLevelJsonArray.getJSONObject(z).getString("direction");
                                    if(!direction.equals("")) {
                                        JSONObject codeLevelJsonObject = stationsLevelJsonArray.getJSONObject(z);
                                        JSONObject yandex_code_jbject = codeLevelJsonObject.getJSONObject("codes");
                                        String yandex_code = yandex_code_jbject.getString("yandex_code");
                                        ServerAnswerModel serverAnswerModel = new ServerAnswerModel(country, region, settlement,
                                                direction, station_name, yandex_code);
                                        stations_list.add(serverAnswerModel);
                                    }
                                }
                            }

                                
                        }


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    public void writeToDb(String selected_country, String selected_region) {
        this.selected_country = selected_country;
        this.selected_region = selected_region;
        this.parseJson();
        new DBManager(context).insertToDb(stations_list);
    }

}
