package com.example.david.david_pset3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncStats;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    TextView left_button, middle_button, right_button, description, price;
    Button home, order;
    ListView listview;
    ArrayAdapter theAdapter;
    ImageView imageView;
    List<String> list_text = new ArrayList<String>();
    List<String> ordered_list = new ArrayList<String>();
    List<Integer> ordered_amounts = new ArrayList<Integer>();
    Integer longclicked;
    String keeptrack = "";
    String mImageURLString = "";
    String descriptionText="";
    String priceText="";


    String Chicken = "";
    Integer ChickenInt = 0;
    String Italian ="";
    Integer ItalianInt = 0;

    String Spaghetti = "";
    Integer SpaghettiInt = 0;

    String Pizza ="";
    Integer PizzaInt = 0;

    String Grilled = "";
    Integer GrilledInt = 0;

    String Pesto ="";
    Integer PestoInt = 0;

    int layer = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        left_button = findViewById(R.id.left_button);
        middle_button = findViewById(R.id.middle_button);
        right_button = findViewById(R.id.right_button);
        home = findViewById(R.id.home);
        order = findViewById(R.id.order);
        imageView = findViewById(R.id.imageView);
        listview = findViewById(R.id.listview);
        left_button.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        right_button.setVisibility(View.INVISIBLE);
        listview.setOnItemClickListener(new clicklistener());
        theAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_text);
        listview.setAdapter(theAdapter);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price_view);
        list_text.add("loading....");

        go_deeper0();
        loadFromSharedPrefs();
    }
    public void get_img(String imageUrl) {

        RequestQueue queue = Volley.newRequestQueue(this);
        // bron: https://www.programcreek.com/javi-api-examples/index.php?api=com.android.volley.toolbox.ImageRequest
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }

        },
                0,
                0,
                null,
                Bitmap.Config.ALPHA_8,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No image available.", Toast.LENGTH_SHORT).show();
                        imageView.setVisibility(View.INVISIBLE);
                    }
                }
        );

        queue.add(imageRequest);
    }


    public void go_deeper0() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://resto.mprog.nl/categories";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("**********************************");

                        try {
                            JSONArray group = response.getJSONArray("categories");
                            list_text.clear();
                            for (int i = 0; i < group.length(); i++) {
                                list_text.add(group.getString(i));

                            }
                            theAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            left_button.setText("did not work");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        left_button.setText("Eroor");

                    }
                });

        // Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest);


    }

    public void go_deeper_1(final String given_text) {

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://resto.mprog.nl/menu";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray group = response.getJSONArray("items");
                            list_text.clear();
                            for (int i = 0; i < group.length(); i++) {
                                if (group.getJSONObject(i).getString("category").equals(given_text)) {
                                    list_text.add(group.getJSONObject(i).getString("name"));
                                }

                            }
                            theAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            middle_button.setText("did not work");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        middle_button.setText("yeah did not work");

                    }
                });

        // Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest);

    }
    public void go_deeper_2(final String given_text) {


        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://resto.mprog.nl/menu";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray group = response.getJSONArray("items");
                            list_text.clear();
                            for (int i = 0; i < group.length(); i++){
                                if (group.getJSONObject(i).getString("name").equals(given_text)) {
                                    list_text.add(group.getJSONObject(i).getString("name"));
                                    priceText = (group.getJSONObject(i).getString("price"));
                                    descriptionText = group.getJSONObject(i).getString("description");
                                    mImageURLString = group.getJSONObject(i).getString("image_url");
                                    price.setText("€" + priceText);
                                    description.setText(descriptionText);
                                    get_img(mImageURLString);
                                    System.out.println("xxxxxxxxxxxxx" +  descriptionText);


                                }

                            }
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>." + list_text);
                            theAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            middle_button.setText("did not work");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        middle_button.setText("Eroor");

                    }
                });

        // Access the RequestQueue
        queue.add(jsObjRequest);



    }



    public void home_execute(View view) {
        left_button.setVisibility(View.INVISIBLE);
        right_button.setVisibility(View.INVISIBLE);
        home.setBackgroundColor(Color.BLACK);
        home.setTextColor(Color.WHITE);
        order.setBackgroundColor(Color.GRAY);
        order.setTextColor(Color.WHITE);
        listview.setOnItemClickListener(new clicklistener());
        layer = 0;
        listview.setOnItemLongClickListener(null);

        select_layer("");

}
    public void array_to_strings() {
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Chicken = "";
        Italian = "";
        Spaghetti = "";
        Pizza = "";
        Grilled = "";
        Pesto="";
        ChickenInt = 0;
        ItalianInt = 0;
        SpaghettiInt = 0;
        PizzaInt = 0;
        GrilledInt = 0;
        for (int i = 0; i < ordered_list.size(); i++) {
            if (ordered_list.get(i).equals("Chicken Noodle Soup")) {
                Chicken = "Chicken Noodle Soup";
                ChickenInt = ordered_amounts.get(i);

            } if (ordered_list.get(i).equals("Italian Salad")) {
                Italian = "Italian Salad";
                ItalianInt = ordered_amounts.get(i);
            }  if (ordered_list.get(i).equals("Spaghetti and Meatballs")) {
                System.out.println("ASHDDHAJKSHDLKSHDIUWHKSADLKWUDH");
                Spaghetti = "Spaghetti and Meatballs";
                SpaghettiInt = ordered_amounts.get(i);

            }  if (ordered_list.get(i).equals("Margherita Pizza")) {
                Pizza = "Margherita Pizza";
                PizzaInt = ordered_amounts.get(i);

            }  if (ordered_list.get(i).equals("Grilled Steelhead Trout Sandwich")) {
                Grilled = "Grilled Steelhead Trout Sandwich";
                GrilledInt = ordered_amounts.get(i);

            }  if (ordered_list.get(i).equals("Pesto Linguini")) {
                Pesto = "Pesto Linguini";
                PestoInt = ordered_amounts.get(i);

            }


        }
    }


    public void safeArray() {
        array_to_strings();
        SharedPreferences prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        System.out.println("))))))))))))))))))))))))" + Spaghetti + "(((((((((((((((((");
        editor.putInt("int1", SpaghettiInt);
        editor.putInt("int2", ChickenInt);
        editor.putInt("int3", PizzaInt);
        editor.putInt("int4", GrilledInt);
        editor.putInt("int5", PestoInt);
        editor.putInt("int6", ItalianInt);
        editor.putString("orderList1", Spaghetti);
        editor.putString("orderList2", Chicken);
        editor.putString("orderList3", Pizza);
        editor.putString("orderList4", Grilled);
        editor.putString("orderList5", Pesto);
        editor.putString("orderList6", Italian);

        editor.commit();
    }

    public void loadFromSharedPrefs() {
        SharedPreferences prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);
        Spaghetti = prefs.getString("orderList1", Spaghetti);
        Chicken = prefs.getString("orderList2", Chicken);
        Pizza = prefs.getString("orderList3", Pizza);
        Grilled = prefs.getString("orderList4", Grilled);
        Pesto = prefs.getString("orderList5", Pesto);
        Italian = prefs.getString("orderList6", Italian);
        SpaghettiInt = prefs.getInt("int1", SpaghettiInt);
        ChickenInt = prefs.getInt("int2", ChickenInt);
        PizzaInt = prefs.getInt("int3", PizzaInt);
        GrilledInt = prefs.getInt("int4", GrilledInt);
        PestoInt = prefs.getInt("int5", PestoInt);
        ItalianInt = prefs.getInt("int6", ItalianInt);

        ordered_list.clear();
        if (Spaghetti != ""){
            ordered_list.add(Spaghetti);
            ordered_amounts.add(SpaghettiInt);
        }
        if (Chicken != ""){
            ordered_list.add(Chicken);
            ordered_amounts.add(ChickenInt);
        }
        if (Pizza != ""){
            ordered_list.add(Pizza);
            ordered_amounts.add(PizzaInt);
        }
        if (Grilled != ""){
            ordered_list.add(Grilled);
            ordered_amounts.add(GrilledInt);
        }
        if (Pesto != ""){
            ordered_list.add(Pesto);
            ordered_amounts.add(PestoInt);
        }
        if (Italian != ""){
            ordered_list.add(Italian);
            ordered_amounts.add(ItalianInt);
        }

    }
    public void order_execute(View view) {

        left_button.setVisibility(View.INVISIBLE);
        right_button.setText("order menu");
        right_button.setTextColor(Color.BLUE);
        order.setBackgroundColor(Color.BLACK);
        order.setTextColor(Color.WHITE);
        home.setBackgroundColor(Color.GRAY);
        listview.setOnItemLongClickListener(new longClickListener());
        home.setTextColor(Color.WHITE);
        listview.setOnItemClickListener(null);
        if (ordered_list.size() == 0){
            right_button.setVisibility(View.INVISIBLE);
            description.setText("you have nothing ordered yet!");
            description.setVisibility(View.VISIBLE);
        }
        else{
            right_button.setVisibility(View.VISIBLE);
        }


        refresh_order();

    }

    public void middle_execute(View view) {


    }

    public void right_execute(View view) {

        if (right_button.getText().equals("order this!")) {
            String text = (middle_button.getText().toString());
            boolean added = false;
            for (int i = 0; i < ordered_list.size(); i++){
                if (ordered_list.get(i).startsWith(text)) {
                    ordered_amounts.set(i, ordered_amounts.get(i) + 1);
                    added = true;
                }
            }
            if (!added)
            {
                ordered_list.add(text);
            }
            ordered_amounts.add(1);
            layer = 0;
            select_layer("nothing");
            refresh_order();
        }
        if (right_button.getText().equals("order menu")) {
            return_time();
            list_text.clear();
            ordered_list.clear();
            ordered_amounts.clear();
            right_button.setVisibility(View.INVISIBLE);
            refresh_order();
            safeArray();
        }
        safeArray();
    }
    public void return_time() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://resto.mprog.nl/order";


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            description.setText("Your estimated waiting time is " + response.getString("preparation_time") + " minutes... Sorry!");
                            description.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "error in ordering menu", Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue
        queue.add(jsObjRequest);
    }


    public void left_execute(View view) {

        if (left_button.getText().equals("back")) {
            layer -= 1;
            middle_button.setText(keeptrack);
            select_layer(keeptrack);
        }
        else if (left_button.getText().equals("delete this")) {
            String deleted = ordered_list.get(longclicked);
            ordered_amounts.set((int) longclicked, ordered_amounts.get((int) longclicked) - 1);
            if (ordered_amounts.get((int) longclicked) == 0) {
                left_button.setVisibility(View.INVISIBLE);
                ordered_amounts.remove((int) longclicked);
                ordered_list.remove((int) longclicked);
                right_button.setVisibility(View.INVISIBLE);
                description.setText("you have nothing ordered yet!");
                description.setVisibility(View.VISIBLE);
            }
                if (ordered_list.size() == 0){

            }

            Toast.makeText(MainActivity.this, "you deleted one " +  deleted, Toast.LENGTH_SHORT).show();

            refresh_order();
        }
        safeArray();
    }
    public void refresh_order(){
        list_text.clear();
        for (int i = 0; i < ordered_list.size(); i++){
            list_text.add(ordered_list.get(i) + "   x" + ordered_amounts.get(i));
        }
        theAdapter.notifyDataSetChanged();



    }

    class clicklistener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            System.out.println(layer);
            if (layer < 2) {
                layer += 1;
                select_layer(String.valueOf(parent.getItemAtPosition(position)));

            }


        }
    }
    private class longClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


            left_button.setText("delete this");
            left_button.setVisibility(View.VISIBLE);
            left_button.setTextColor(Color.BLUE);
            longclicked = (int) id;
            return true;
        }
    }


    private void select_layer(String given_text) {
        switch (layer) {
            case 0:
                go_deeper0();
                middle_button.setText("Menu");
                right_button.setVisibility(View.INVISIBLE);
                left_button.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.VISIBLE);
                price.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);
                keeptrack = "";
                break;
            case 1:
                go_deeper_1(given_text);
                middle_button.setText(given_text);
                keeptrack = given_text;
                left_button.setVisibility(View.VISIBLE);
                left_button.setText("back");
                right_button.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.VISIBLE);
                price.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);

                break;
            case 2:
                go_deeper_2(given_text);
                middle_button.setText(given_text);
                left_button.setText("back");
                left_button.setVisibility(View.VISIBLE);
                right_button.setVisibility(View.VISIBLE);
                right_button.setText("order this!");

                listview.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);

                price.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                System.out.println("xxxxxxxxxxxxx" +  descriptionText);




        }
    }
}









