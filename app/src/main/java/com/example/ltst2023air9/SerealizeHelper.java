package com.example.ltst2023air9;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SerealizeHelper {

    public static String getJsonFromFile(File file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream);
        scanner.useDelimiter("\\A");

        //Log.d("FILE_STRING", streamString);

        return scanner.hasNext() ? scanner.next() : "";
    }

    /*
                    SettingsData groups = new SettingsData(ctx.presetGroups);

                String jsonString = new Gson().toJson(groups);
                Log.i("JSON", jsonString);


                SettingsData list = new Gson().fromJson(jsonString, SettingsData.class);
                Log.i("JSON_REVERSE", list.getGroups().get(1).getName());


                String jsonString2 = new Gson().toJson(ctx.presetGroups);
                Type userListType = new TypeToken<ArrayList<PresetGroup>>(){}.getType();
                List<PresetGroup> pureList = new Gson().fromJson(jsonString2, userListType);
                Log.i("JSON_REVERSE3", pureList.get(3).getName());
     */
}
