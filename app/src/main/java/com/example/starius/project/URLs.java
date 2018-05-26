package com.example.starius.project;

/**
 * Created by STARIUS on 1/20/2018.
 */

public class URLs {

    public  static final String HOST_URL = "http://projectmc.azurewebsites.net/";

    private static final String ROOT_URL = HOST_URL + "Api.php?apicall=";
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";

    public static final String INPUT = HOST_URL + "_Input.php";
    public static final String HISTORY = HOST_URL + "History.php";
    public static final String DELETE = HOST_URL + "_Delete.php";
    public static final String NUTRIENT = HOST_URL + "NutrientActivity.php";
}
