/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.ltst2023air9.ui.fragments.tableview;

import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.ui.fragments.tableview.model.Cell;
import com.example.ltst2023air9.ui.fragments.tableview.model.ColumnHeader;
import com.example.ltst2023air9.ui.fragments.tableview.model.RowHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class TableViewModel {

    public static final String FLAT_TYPE_ALL_WITHOUT_MOP = "Все помещения кроме МОП";
    public static final String FLAT_TYPE_ALL = "Все помещения";
    public static final String FLAT_TYPE_KITCHEN = "Жилая/Кухня";
    public static final String FLAT_TYPE_BATHROOM = "Ванная";
    public static final String FLAT_TYPE_MOP = "МОП";


    public static final String SURFACE_FLOOR = "Пол";
    public static final String SURFACE_WALL = "Стена";
    public static final String SURFACE_ROOF = "Потолок";
    public static final String SURFACE_NONE = "-";

    public static final String CLASS_NO = "Нет отделки";
    public static final String CLASS_DARK = "Черновая";
    public static final String CLASS_WHITE = "Чистовая";
    public static final String CLASS_DOOR = "Двери";
    public static final String CLASS_TRASH = "Мусор";
    public static final String CLASS_SWITCH = "Розетки и выключатели";
    public static final String CLASS_WINDOW = "Отделка окна";
    public static final String CLASS_RADIATOR = "Установленная батарея";
    public static final String CLASS_KITCHEN = "Кухня";
    public static final String CLASS_TOILET = "Унитаз";
    public static final String CLASS_BATHROOM = "Ванна";
    public static final String CLASS_cockleshell = "Раковина";

    public static final String METRIC_PERCENT_WITH_STATE = "Доля помещений с этим состоянием";
    public static final String METRIC_FACT = "Факт наличия";
    public static final String METRIC_AMOUNT = "Количество";
    public static final String METRIC_PERCENT_WITH_WINDOW = "Доля от общего числа окон";
    public static final String METRIC_PERCENT_WINDOW = "Доля от общего числа окон";
    public static final String METRIC_PERCENT_BATHROOM = "Доля от ванных комнат";
    public static final String METRIC_PERCENT_DOOR = "Доля от дверных проемов";




    // Columns indexes
    public static final int MOOD_COLUMN_INDEX = 3;
    public static final int GENDER_COLUMN_INDEX = 4;

    // Constant values for icons
    public static final int SAD = 1;
    public static final int HAPPY = 2;
    public static final int BOY = 1;
    public static final int GIRL = 2;

    // Constant size for dummy data sets
    private static final int COLUMN_SIZE = 5;
    private static final int ROW_SIZE = 27;

    // Drawables
    @DrawableRes
    private final int mBoyDrawable;
    @DrawableRes
    private final int mGirlDrawable;
    @DrawableRes
    private final int mHappyDrawable;
    @DrawableRes
    private final int mSadDrawable;

    List<List<Cell>> list = new ArrayList<>();
    public TableViewModel() {
        // initialize drawables
        mBoyDrawable = R.drawable.ic_male;
        mGirlDrawable = R.drawable.ic_female;
        mHappyDrawable = R.drawable.ic_happy;
        mSadDrawable = R.drawable.ic_sad;
    }

    @NonNull
    private List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
//        for (int i = 0; i < ROW_SIZE; i++) {
//            RowHeader header = new RowHeader(String.valueOf(i), "row " + i);
//            list.add(header);
//        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    @NonNull
    private List<ColumnHeader> getRandomColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();

        list.add(new ColumnHeader(String.valueOf(1), "Тип помещения"));
        list.add(new ColumnHeader(String.valueOf(2), "Поверхность"));
        list.add(new ColumnHeader(String.valueOf(3), "Класс"));
        list.add(new ColumnHeader(String.valueOf(4), "Метрика"));
        list.add(new ColumnHeader(String.valueOf(5), "Готовность Модель"));

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    @NonNull
    private List<List<Cell>> getCellListForSortingTest() {
        List<String>typeList = new ArrayList<>();
        typeList.add(FLAT_TYPE_ALL_WITHOUT_MOP);
        typeList.add(FLAT_TYPE_ALL_WITHOUT_MOP);
        typeList.add(FLAT_TYPE_ALL_WITHOUT_MOP);
        typeList.add(FLAT_TYPE_ALL_WITHOUT_MOP);
        typeList.add(FLAT_TYPE_ALL_WITHOUT_MOP);
        typeList.add(FLAT_TYPE_ALL_WITHOUT_MOP);
        typeList.add(FLAT_TYPE_ALL_WITHOUT_MOP);
        typeList.add(FLAT_TYPE_ALL_WITHOUT_MOP);
        typeList.add(FLAT_TYPE_ALL_WITHOUT_MOP);
        typeList.add(FLAT_TYPE_ALL);
        typeList.add(FLAT_TYPE_ALL);
        typeList.add(FLAT_TYPE_ALL);
        typeList.add(FLAT_TYPE_KITCHEN);
        typeList.add(FLAT_TYPE_KITCHEN);
        typeList.add(FLAT_TYPE_KITCHEN);
        typeList.add(FLAT_TYPE_BATHROOM);
        typeList.add(FLAT_TYPE_BATHROOM);
        typeList.add(FLAT_TYPE_BATHROOM);
        typeList.add(FLAT_TYPE_MOP);
        typeList.add(FLAT_TYPE_MOP);
        typeList.add(FLAT_TYPE_MOP);
        typeList.add(FLAT_TYPE_MOP);
        typeList.add(FLAT_TYPE_MOP);
        typeList.add(FLAT_TYPE_MOP);
        typeList.add(FLAT_TYPE_MOP);
        typeList.add(FLAT_TYPE_MOP);
        typeList.add(FLAT_TYPE_MOP);


        List<String>surfaceList = new ArrayList<>();
        surfaceList.add(SURFACE_FLOOR);
        surfaceList.add(SURFACE_FLOOR);
        surfaceList.add(SURFACE_FLOOR);
        surfaceList.add(SURFACE_WALL);
        surfaceList.add(SURFACE_WALL);
        surfaceList.add(SURFACE_WALL);
        surfaceList.add(SURFACE_ROOF);
        surfaceList.add(SURFACE_ROOF);
        surfaceList.add(SURFACE_ROOF);
        surfaceList.add(SURFACE_NONE);
        surfaceList.add(SURFACE_NONE);
        surfaceList.add(SURFACE_NONE);
        surfaceList.add(SURFACE_NONE);
        surfaceList.add(SURFACE_NONE);
        surfaceList.add(SURFACE_NONE);
        surfaceList.add(SURFACE_NONE);
        surfaceList.add(SURFACE_NONE);
        surfaceList.add(SURFACE_NONE);
        surfaceList.add(SURFACE_FLOOR);
        surfaceList.add(SURFACE_FLOOR);
        surfaceList.add(SURFACE_FLOOR);
        surfaceList.add(SURFACE_WALL);
        surfaceList.add(SURFACE_WALL);
        surfaceList.add(SURFACE_WALL);
        surfaceList.add(SURFACE_ROOF);
        surfaceList.add(SURFACE_ROOF);
        surfaceList.add(SURFACE_ROOF);

        List<String>classList = new ArrayList<>();
        classList.add(CLASS_NO);
        classList.add(CLASS_DARK);
        classList.add(CLASS_WHITE);
        classList.add(CLASS_NO);
        classList.add(CLASS_DARK);
        classList.add(CLASS_WHITE);
        classList.add(CLASS_NO);
        classList.add(CLASS_DARK);
        classList.add(CLASS_WHITE);
        classList.add(CLASS_DOOR);
        classList.add(CLASS_TRASH);
        classList.add(CLASS_SWITCH);
        classList.add(CLASS_WINDOW);
        classList.add(CLASS_RADIATOR);
        classList.add(CLASS_KITCHEN);
        classList.add(CLASS_TOILET);
        classList.add(CLASS_BATHROOM);
        classList.add(CLASS_cockleshell);
        classList.add(CLASS_NO);
        classList.add(CLASS_DARK);
        classList.add(CLASS_WHITE);
        classList.add(CLASS_NO);
        classList.add(CLASS_DARK);
        classList.add(CLASS_WHITE);
        classList.add(CLASS_NO);
        classList.add(CLASS_DARK);
        classList.add(CLASS_WHITE);

        List<String>metricList = new ArrayList<>();
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_DOOR);
        metricList.add(METRIC_FACT);
        metricList.add(METRIC_AMOUNT);
        metricList.add(METRIC_PERCENT_WITH_WINDOW);
        metricList.add(METRIC_PERCENT_WITH_WINDOW);
        metricList.add(METRIC_AMOUNT);
        metricList.add(METRIC_PERCENT_BATHROOM);
        metricList.add(METRIC_PERCENT_BATHROOM);
        metricList.add(METRIC_PERCENT_BATHROOM);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);
        metricList.add(METRIC_PERCENT_WITH_STATE);

        //List<List<Cell>> list = new ArrayList<>();
        for (int row = 0; row < ROW_SIZE; row++) {
            List<Cell> cellList = new ArrayList<>();
            for (int column = 0; column < COLUMN_SIZE; column++) {
                Object text = "";//"cell " + String.valueOf(column) + " " + String.valueOf(row);

                //final int random = new Random().nextInt();
                switch (column) {
                    case 0:
                        text = typeList.get(row);
                        break;
                    case 1:
                        text = surfaceList.get(row);
                        break;
                    case 2:
                        text = classList.get(row);
                        break;
                    case 3:
                        text = metricList.get(row);
                        break;
                }

                // Create dummy id.
                String id = column + "-" + row;

                Cell cell;

                    cell = new Cell(id, text);

                cellList.add(cell);
            }
            list.add(cellList);
        }

        return list;
    }

    @DrawableRes
    public int getDrawable(int value, boolean isGender) {
        if (isGender) {
            return value == BOY ? mBoyDrawable : mGirlDrawable;
        } else {
            return value == SAD ? mSadDrawable : mHappyDrawable;
        }
    }

    @NonNull
    public void initCellList() {
         getCellListForSortingTest();
    }

    @NonNull
    public List<List<Cell>> getCellList() {
        return list;
    }

    @NonNull
    public List<RowHeader> getRowHeaderList() {
        return getSimpleRowHeaderList();
    }

    @NonNull
    public List<ColumnHeader> getColumnHeaderList() {
        return getRandomColumnHeaderList();
    }

    public void updateRow(int row, String str) {
        //Log.d("list", "size: " + list.size());
        //Log.d("list", "coordinate: col " + 4 + ", row " + row + " = " + list.get(row).get(4).getData());

        Cell cell = new Cell(4 + "-" + row, str);
        list.get(row).set(4, cell);
        //list.get(row).add(4, cell);
    }
}
