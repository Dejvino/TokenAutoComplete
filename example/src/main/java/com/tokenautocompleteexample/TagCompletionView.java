package com.tokenautocompleteexample;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import androidx.annotation.NonNull;

import com.tokenautocomplete.TokenCompleteTextView;

public class TagCompletionView extends TokenCompleteTextView<Tag> {

    InputConnection testAccessibleInputConnection;

    public TagCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Tag object) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = (TokenTextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);
        token.setText(object.getFormattedValue());
        return token;
    }

    @Override
    protected Tag defaultObject(String completionText) {
        if (completionText.length() == 1) {
            return null;
        } else {
            return new Tag(completionText.charAt(0), completionText.substring(1, completionText.length()));
        }
    }

    @NonNull
    @Override
    public InputConnection onCreateInputConnection(@NonNull EditorInfo outAttrs) {
        testAccessibleInputConnection = super.onCreateInputConnection(outAttrs);
        return testAccessibleInputConnection;
    }
}
