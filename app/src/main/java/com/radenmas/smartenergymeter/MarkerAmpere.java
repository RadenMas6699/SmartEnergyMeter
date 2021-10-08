package com.radenmas.smartenergymeter;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MarkerAmpere extends MarkerView {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm zz", Locale.getDefault());
    private final TextView tvVolt, tvTime;

    public MarkerAmpere(Context context, int layoutResource) {
        super(context, layoutResource);

        tvVolt = findViewById(R.id.tvValue);
        tvTime = findViewById(R.id.tvTime);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvVolt.setText(Utils.formatNumber(ce.getHigh(), 0, true) + " A");
        } else {
            float time = e.getX();
            String jam = sdf.format(time);
            tvTime.setText(jam);
            tvVolt.setText(Utils.formatNumber(e.getY(), 0, true) + " A");
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
