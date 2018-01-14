package com.example.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import example.base.util.LogUtil;

/**
 * Created by Silver on 24/10/2016.
 */

public class TextFormatUtil {
    private static SimpleDateFormat mDateFormat;
    private static SimpleDateFormat timeFormat;
    private static SimpleDateFormat dayFormat;
    private static NumberFormat mNumberFormat;

    public static SimpleDateFormat getDateFormat() {
        if (mDateFormat == null) {
            mDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        }

        return mDateFormat;
    }

    public static SimpleDateFormat getTimeFormat() {
        if (timeFormat == null) {
            timeFormat = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
        }

        return timeFormat;
    }

    public static SimpleDateFormat getDayFormat() {
        if (dayFormat == null) {
            dayFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        }

        return dayFormat;
    }

    public static NumberFormat getNumberFormat() {
        if (mNumberFormat == null) {
            mNumberFormat = new DecimalFormat();
        }

        return mNumberFormat;
    }

    public static void replace(List<String> strings) {
        ListIterator<String> iterator = strings.listIterator();
        while (iterator.hasNext()) {
            iterator.set(iterator.next().toUpperCase());
        }
    }

    public static String carPlate(String plate) {
        StringBuilder builder = new StringBuilder();
        builder.append(plate != null ? plate : "N.A");
        return builder.toString();
    }

    public static String carMileage(Integer mileage) {

        StringBuilder builder = new StringBuilder();
        builder.append(mileage != null ? getNumberFormat().format(mileage) : "N.A").append(" km");
        return builder.toString();
    }

    public static String carRegDate(Date regDate) {
        StringBuilder builder = new StringBuilder();
        builder.append(regDate != null ? getDateFormat().format(regDate) : "N.A");
        return builder.toString();
    }

    public static String status(String status) {
        StringBuilder builder = new StringBuilder();

        builder.append(status);

        return builder.toString();
    }

    public static String getQuotedText(Context context, String carPlate) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(context.getString(R.string.quote_desc_1));
        builder.append(" <b>");

        SpannableString redSpannable = new SpannableString(carPlate);
        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, carPlate.length(), 0);
        builder.append(redSpannable);

        builder.append("</b> ");
        builder.append(context.getString(R.string.quote_recently));

        return builder.toString();
    }

    public static String getActiveAuctionText(Context context, String carPlate) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(context.getString(R.string.quote_active_listing1));
        builder.append(" <b>");

        SpannableString redSpannable = new SpannableString(carPlate);
        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, carPlate.length(), 0);
        builder.append(redSpannable);

        builder.append("</b> ");
        builder.append(context.getString(R.string.quote_active_listing2));

        return builder.toString();
    }

    public static String getNobodyQuote(Context context, int quoteAmount) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(context.getString(R.string.quote_nobody));
        builder.append(" <b'>");

        String currency = getNumberInCurrency(quoteAmount);
        SpannableString redSpannable = new SpannableString(currency);
        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, currency.length(), 0);
        builder.append(redSpannable);

        builder.append("</b>.");

        return builder.toString();
    }

    public static String getNobodyQuoteShort(Context context, String carPlate) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(context.getString(R.string.quote_nobody_short));
        builder.append(" <b'>");

        SpannableString redSpannable = new SpannableString(carPlate);
        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, carPlate.length(), 0);
        builder.append(redSpannable);

        builder.append("</b>.");

        return builder.toString();
    }

    public static String carPlateDot(String carPlate) {
        StringBuilder builder = new StringBuilder();

        if (!TextUtils.isEmpty(carPlate)) {
            builder.append(carPlate);
            builder.append(" ");
        }

        return builder.toString();
    }

    public static String mileageDot(String mileage) {

        StringBuilder builder = new StringBuilder();

        if (!TextUtils.isEmpty(mileage)) {
            builder.append(getNumberFormat().format(Double.parseDouble(mileage)));
            builder.append(" km");
        }

        return builder.toString();
    }

    public static String getNumberInCurrency(int number) {
        String price = "";

        try {
            String symbol = "$";

            price = symbol + getNumberFormat().format(number);
        } catch (Exception ex) {
            LogUtil.e(ex);
        }

        return price;
    }

    public static String getNumberInCurrency(Double number) {
        String price = "";

        try {
            String symbol = "$";

            price = symbol + getNumberFormat().format(number);
        } catch (Exception ex) {
            LogUtil.e(ex);
        }

        return price;
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    private static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) {
            return format(Long.MIN_VALUE + 1);
        }
        if (value < 0) {
            return "-" + format(-value);
        }
        if (value < 1000) {
            return Long.toString(value); //deal with easy case
        }

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String getTimeDifferences(Date calen) {

        if (new Date().after(calen)) {
            return new StringBuilder()
                    .append("Expired").toString();
        } else {
            return new StringBuilder()
                    .append(DateUtils.getRelativeTimeSpanString(calen.getTime(),
                            Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS)).toString();
        }
    }
}
