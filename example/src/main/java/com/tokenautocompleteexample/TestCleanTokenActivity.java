package com.tokenautocompleteexample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TagTokenizer;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.Arrays;

/**
 * Created by mgod on 11/29/17.
 *
 * If you're looking for a sample implementation, please look at TokenActivity.
 * This class is used in tests.
 */

public class TestCleanTokenActivity extends AppCompatActivity {
    ContactsCompletionView completionView;
    Person[] people;
    ArrayAdapter<Person> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();
        tabs.addTab(tabs.newTabSpec("Contacts").setContent(R.id.contactsFrame).setIndicator("Contacts"));
        tabs.addTab(tabs.newTabSpec("Composer").setContent(R.id.hashtagsFrame).setIndicator("Composer"));

        people = new Person[]{
                new Person("Marshall Weir", "marshall@example.com"),
                new Person("Margaret Smith", "margaret@example.com"),
                new Person("Max Jordan", "max@example.com"),
                new Person("Meg Peterson", "meg@example.com"),
                new Person("Amanda Johnson", "amanda@example.com"),
                new Person("Terry Anderson", "terry@example.com"),
                new Person("Siniša Damianos Pilirani Karoline Slootmaekers",
                        "siniša_damianos_pilirani_karoline_slootmaekers@example.com")
        };

        adapter = new FilteredArrayAdapter<Person>(this, R.layout.person_layout, people) {
            @Override
            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    assert(l != null);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                Person p = getItem(position);
                assert(p != null);
                ((TextView)convertView.findViewById(R.id.name)).setText(p.getName());
                ((TextView)convertView.findViewById(R.id.email)).setText(p.getEmail());

                return convertView;
            }

            @Override
            protected boolean keepObject(Person person, String mask) {
                mask = mask.toLowerCase();
                return person.getName().toLowerCase().startsWith(mask) || person.getEmail().toLowerCase().startsWith(mask);
            }
        };

        completionView = findViewById(R.id.searchView);
        completionView.setAdapter(adapter);
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);


        if (savedInstanceState == null) {
            completionView.setPrefix("To: ", Color.parseColor("blue"));
        }

        TagCompletionView tagView = findViewById(R.id.composeView);
        tagView.performBestGuess(false);
        tagView.preventFreeFormText(false);
        tagView.setTokenizer(new TagTokenizer(Arrays.asList('@', '#')));
        tagView.setAdapter(new TagAdapter(this, R.layout.tag_layout, Tag.sampleTags()));
        tagView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
        tagView.setThreshold(1);
    }
}
