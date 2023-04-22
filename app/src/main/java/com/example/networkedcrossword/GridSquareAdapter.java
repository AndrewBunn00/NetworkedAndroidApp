package com.example.networkedcrossword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

// Referenced https://www.geeksforgeeks.org/gridview-in-android-with-example/ for help with adapters
public class GridSquareAdapter extends ArrayAdapter<GridSquareModel> {

    public GridSquareAdapter(@NonNull Context context, ArrayList<GridSquareModel> arrOfSquares) {
        super(context, 0, arrOfSquares);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convView, @NonNull ViewGroup parent) {
        View gridSquareView = convView;

        if(gridSquareView == null) {
            // inflates the stuff to be displayed in the view
            gridSquareView = LayoutInflater.from(getContext()).inflate(R.layout.grid_square_item, parent, false);
        }

        GridSquareModel currentGridSquare = getItem(position);

        TextView gridSquareTextView = gridSquareView.findViewById(R.id.squareText);
        gridSquareTextView.setText(Integer.toString(currentGridSquare.getId()));

        return gridSquareView;
    }
}
