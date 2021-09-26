package com.example.thegolfclubcat2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.thegolfclubcat2.helper.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClubActivity extends AppCompatActivity {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener((parent, v, groupPosition, id) -> false);

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(groupPosition -> Toast.makeText(getApplicationContext(),
                listDataHeader.get(groupPosition) + "",
                Toast.LENGTH_SHORT).show());

        // Listview Group collapsed listener
        expListView.setOnGroupCollapseListener(groupPosition -> Toast.makeText(getApplicationContext(),
                listDataHeader.get(groupPosition) + "",
                Toast.LENGTH_SHORT).show());

        // Listview on child click listener
        expListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            // TODO Auto-generated method stub
            Toast.makeText(
                    getApplicationContext(),
                    listDataHeader.get(groupPosition)
                            + " : "
                            + listDataChild.get(
                            listDataHeader.get(groupPosition)).get(
                            childPosition), Toast.LENGTH_SHORT)
                    .show();
            return false;
        });
    }

    private void prepareListData() {

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding Parent Data
        listDataHeader.add("Lagny Golf Club");
        listDataHeader.add("Paris Golf Club");
        listDataHeader.add("Jossigny Golf Club");
        listDataHeader.add("Troyes Golf Club");
        listDataHeader.add(" Club");
        listDataHeader.add("Limuru Country Club");

        // Adding child data
        List<String> royal_nairobi_golf_club = new ArrayList<>();
        royal_nairobi_golf_club.add("Address: Ngong Rd, Nairobi");
        royal_nairobi_golf_club.add("Website: https://www.royalnairobigc.com/");
        royal_nairobi_golf_club.add("Hours: 8am - 5pm weekdays, 8am - 12pm saturdays");
        royal_nairobi_golf_club.add("Contact: 0718 810810");

        List<String> karen_country_club = new ArrayList<>();
        karen_country_club.add("Address: Karen, Karen Rd, Nairobi");
        karen_country_club.add("Website: https://www.karencountryclub.org/");
        karen_country_club.add("Hours: 8am - 5pm weekdays, 8am - saturdays");
                karen_country_club.add("contact 020 3882801");

        List<String> sigona_golf_club = new ArrayList<>();
        sigona_golf_club.add("Address: Kikuyu Nairobi, Naivasha highway, Kiambu");
        sigona_golf_club.add("Website: https://sigonagolfclub.com/");
        sigona_golf_club.add("Hours: 8am - 5pm weekdays, 8am - 12pm saturdays");
                sigona_golf_club.add("Contact: 020 2020518");

        List<String> windsor_golf_hotel_country_club = new ArrayList<>();
        windsor_golf_hotel_country_club.add("Address: Kigwa Lane, Off Kiambu Rd, Nairobi");
        windsor_golf_hotel_country_club.add("Website http://www.windsorgolfresort.com/");
        windsor_golf_hotel_country_club.add("Hours: weekdays & saturdays");
        windsor_golf_hotel_country_club.add("Contact: 020 8647014");

        List<String> muthaiga_country_club = new ArrayList<>();
        muthaiga_country_club.add("Address: opposite Saudi Arabia Embassy, Muthaiga Rd, Nairobi");
        muthaiga_country_club.add("Contact: https://www.mcc.co.ke/");
        muthaiga_country_club.add("Hours: 8am - 5pm daily");
        muthaiga_country_club.add("Contact: (+254) (0) 20 7229000 / (+254) (0) 1111 90000");

        List<String> limuru_country_club = new ArrayList<>();
        limuru_country_club.add("Address: Kabuku-Tigoni Road, Limuru");
        limuru_country_club.add("Website: https://limurucountryclub.co.ke/");
        limuru_country_club.add("Hours: 8am - 5pm daily");
        limuru_country_club.add("Contact: +254 722 645941");

        listDataChild.put(listDataHeader.get(0), royal_nairobi_golf_club); // Header, Child data
        listDataChild.put(listDataHeader.get(1), karen_country_club);
        listDataChild.put(listDataHeader.get(2), sigona_golf_club);
        listDataChild.put(listDataHeader.get(3), windsor_golf_hotel_country_club);
        listDataChild.put(listDataHeader.get(4), muthaiga_country_club);
        listDataChild.put(listDataHeader.get(5), limuru_country_club);
    }
}