package com.example.janus.stocktracker.data;

import android.provider.BaseColumns;


public final class PortfolioDBContract {

    private PortfolioDBContract () {};

    public static class PortfolioEntry implements BaseColumns {

        // table name
        public static final String TABLE_NAME = "portfolio";

        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_STOCK = "stock";

    }

}
