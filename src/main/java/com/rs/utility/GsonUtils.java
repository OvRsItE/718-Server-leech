package com.rs.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author ReverendDread
 * Created 3/15/2021 at 3:25 AM
 * @project 718---Server
 */
public class GsonUtils {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}
