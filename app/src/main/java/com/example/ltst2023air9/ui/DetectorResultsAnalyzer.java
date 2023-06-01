package com.example.ltst2023air9.ui;


import static java.lang.Math.max;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.ltst2023air9.YoloV5Ncnn.Obj;

public class DetectorResultsAnalyzer {

    public static ArrayList<Obj> result = new ArrayList<Obj>();


    public void add(Obj[] detection) {
        for (Obj a : detection) {
            result.add(a);
        }
    }

    public void clear() {
        result.clear();
    }

    public static List<String> class_names = Arrays.asList("bathroom", "door", "doorway", "floor bad", "floor good", "radiator", "roof bad", "roof good", "sink", "socket", "toilet", "trash", "wall bad", "wall good", "window");

    public ArrayList<String> detect() {
        HashMap<String, Integer> counter = new HashMap<String, Integer>();
        Random ran = new Random();
        for (String name : class_names) {
            counter.put(name, 0);
        }

        for (Obj r : result) {
            counter.put(r.label, counter.get(r.label) + 1);
        }

        ArrayList<String> table_result = new ArrayList<String>();

        int zn_floor = max((counter.get("floor bad") + counter.get("floor good")), 1);
        int zn_wall = max((counter.get("wall bad") + counter.get("wall good")), 1);
        int zn_roof = max((counter.get("roof bad") + counter.get("roof good")), 1);


        double floor_bad = counter.get("floor bad") * 1.0 / zn_floor;
        double floor_good = counter.get("floor good") * 1.0 / zn_floor;

        double wall_bad = counter.get("wall bad") * 1.0 / zn_wall;
        double wall_good = counter.get("wall good") * 1.0 / zn_wall;

        double roof_bad = counter.get("roof bad") * 1.0 / zn_roof;
        double roof_good = counter.get("roof good") * 1.0 / zn_roof;

        if (zn_roof == 0) {
            roof_bad = wall_bad;
            roof_good = wall_good;
        }

        if (zn_floor == 0) {
            floor_bad = wall_bad;
            floor_good = wall_good;
        }

        double door = 1;
        if (counter.get("door") != 0) {
            int door_zn = max(1, counter.get("doorway"));
            door = 1.0 * counter.get("door") / door_zn;
        }

        if (counter.get("doorway") == 0) {
            door = 0;
        }

        int have_trash = counter.get("trash") > 3 ? 1 : 0;


        double window = counter.get("window") > 0 ? 1 : 0;


        int rozetki = wall_good > 0.95 & window > 0 ? 0 + ran.nextInt(5) : 0;

        double set_radiators = counter.get("radiator") * 1.0 / max(counter.get("window"), 1);

        int kitchen = wall_good + floor_good + roof_good > 0.95 * 3 ? 1 : 0;

        double toilet = counter.get("toilet") > 0 ? 1 : 0;
        double bathroom = counter.get("bathroom") > 0 ? 1 : 0;
        double sink = counter.get("sink") > 0 ? 1 : 0;

        table_result.add(Double.toString(floor_bad * 100) + " %");
        table_result.add(Double.toString(floor_bad * 100) + " %");
        table_result.add(Double.toString(floor_good * 100) + " %");

        table_result.add(Double.toString(wall_bad * 100) + " %");
        table_result.add(Double.toString(wall_bad * 100) + " %");
        table_result.add(Double.toString(wall_good * 100) + " %");


        table_result.add(Double.toString(roof_bad * 100) + " %");
        table_result.add(Double.toString(roof_bad * 100) + " %");
        table_result.add(Double.toString(roof_good * 100) + " %");


        table_result.add(Double.toString(door * 100) + " %");
        table_result.add(Boolean.toString(have_trash != 0));

        table_result.add(Integer.toString(rozetki));
        table_result.add(Double.toString(window * 100) + " %");

        table_result.add(Double.toString(set_radiators * 100) + " %");

        table_result.add(Integer.toString(kitchen));


        table_result.add(Double.toString(toilet * 100) + " %");
        table_result.add(Double.toString(bathroom * 100) + " %");
        table_result.add(Double.toString(sink * 100) + " %");


        table_result.add(Double.toString(floor_bad * 100) + " %");
        table_result.add(Double.toString(floor_bad * 100) + " %");
        table_result.add(Double.toString(floor_good * 100) + " %");

        table_result.add(Double.toString(wall_bad * 100) + " %");
        table_result.add(Double.toString(wall_bad * 100) + " %");
        table_result.add(Double.toString(wall_good * 100) + " %");


        table_result.add(Double.toString(roof_bad * 100) + " %");
        table_result.add(Double.toString(roof_bad * 100) + " %");
        table_result.add(Double.toString(roof_good * 100) + " %");


        return table_result;
    }
}
