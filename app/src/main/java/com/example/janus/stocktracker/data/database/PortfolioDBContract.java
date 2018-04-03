package com.example.janus.stocktracker.data.database;

import android.provider.BaseColumns;

// Not to be confused with an MVP View/Presenter contract. This 'contract' defines the database name and columns for the portfolio database.

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
