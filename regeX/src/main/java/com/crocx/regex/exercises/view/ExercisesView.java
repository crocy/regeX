package com.crocx.regex.exercises.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crocx.regex.R;
import com.crocx.regex.exercises.ExercisesAction;
import com.crocx.regex.exercises.model.ExerciseItem;
import com.crocx.regex.ui.UiStateManager;

import java.util.List;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExercisesView extends LinearLayout {

    private ArrayAdapter<ExerciseItem> arrayAdapter;

    private ListView exercisesListView;

    public ExercisesView(Context context) {
        super(context);
    }

    public ExercisesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        exercisesListView = (ListView) findViewById(R.id.exercisesListView);
    }

    public void init(final UiStateManager uiStateManager) {
        arrayAdapter = new ArrayAdapter<ExerciseItem>(getContext(), 0) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView exerciseItemView;
                if (convertView == null) {
                    exerciseItemView = (TextView) View.inflate(getContext(), R.layout.exercise_list_item, null);
                } else {
                    exerciseItemView = (TextView) convertView;
                }

                exerciseItemView.setText(arrayAdapter.getItem(position).getName());

                return exerciseItemView;
            }
        };

        exercisesListView.setAdapter(arrayAdapter);
        exercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                uiStateManager.fireAction(ExercisesAction.EXERCISE_ITEM_CLICKED, arrayAdapter.getItem(position));
            }
        });
    }

    public void updateView(List<ExerciseItem> exerciseItems) {
        arrayAdapter.setNotifyOnChange(false);

        arrayAdapter.clear();
        for (ExerciseItem exerciseItem : exerciseItems) {
            arrayAdapter.add(exerciseItem);
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
